#!/usr/bin/env python3
"""Generate a regex-crossword HTML and/or export it to PDF.

Usage:
    python3 crosswords.py --generate              # produce index.html
    python3 crosswords.py --export                # produce crosswords.pdf from index.html
    python3 crosswords.py --generate --export     # do both in sequence
    python3 crosswords.py -g -e -o out.html -p out.pdf

Short flags:
    -g    --generate
    -e    --export

Options:
    -o, --output PATH   HTML output path (default: index.html)
    -p, --pdf PATH      PDF output path (default: crosswords.pdf)
    [pack.json ...]     Specific pack files to include (default: all in 01___info___copiedjsons/)

Page layout:
    Puzzles are assigned a weight W = rows + ceil(M / K) where M is the length
    of the longest bottom clue and K=8. Puzzles with W < 7 are paired two-per-page;
    heavier puzzles get their own page.
"""
import argparse
import html as htmllib
import json
import math
import sys
from pathlib import Path

HERE = Path(__file__).resolve().parent
PACKS_DIR = HERE / "01___info___copiedjsons"
DEFAULT_HTML = HERE / "index.html"
DEFAULT_PDF = HERE / "crosswords.pdf"

WEIGHT_K = 8
WEIGHT_T1 = 3   # W <= 2: up to 4 puzzles per page
WEIGHT_T2 = 5   # W <= 4: up to 3 puzzles per page
WEIGHT_T3 = 7   # W <= 6: up to 2 puzzles per page
                # W >= 7: 1 puzzle per page

WEIGHT_KH = 8   # chars-per-cell-width for right-clue length conversion
WEIGHT_TH = 4   # pages with W_horizontal < TH can be paired side by side

# ---------------------------------------------------------------------------
# CSS
# ---------------------------------------------------------------------------

CSS = """\
    body {
      font-family: monospace;
      background: #ccc;
      color: #000;
      margin: 40px;
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    .puzzle {
      page-break-after: always;
      margin-bottom: 60px;
      background: #fff;
      width: 210mm;
      min-height: 297mm;
      padding: 16mm;
      box-sizing: border-box;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      position: relative;
    }

    .puzzle h2 {
      margin-bottom: 16px;
      margin-top: 12px;
      font-size: 8px;
    }

    .page-number {
      font-family: "Lucida Console", monospace;
      font-size: 8px;
      font-weight: bold;
      letter-spacing: 2px;
      position: absolute;
      bottom: 10mm;
    }

    /* print: strip screen-only decoration so each puzzle is exactly one A4 page */
    @media print {
      body {
        margin: 0;
        background: #fff;
      }
      .page-pair {
        margin-bottom: 0;
      }
      .puzzle {
        margin-bottom: 0;
        border: none;
        width: auto;
        height: 297mm;
        min-height: 0;
      }
      .puzzle:last-child {
        page-break-after: auto;
      }
    }

    .page-pair {
      page-break-after: always;
      margin-bottom: 60px;
      display: flex;
      flex-direction: row;
      position: relative;
    }

    .page-pair > .page-number {
      position: absolute;
      bottom: 10mm;
      left: 50%;
      transform: translateX(-50%);
    }

    .page-pair::after {
      content: "";
      position: absolute;
      left: 50%;
      top: 16mm;
      bottom: 16mm;
      width: 4px;
      background: #000;
    }

    .page-pair .puzzle {
      page-break-after: auto;
      margin-bottom: 0;
      width: 105mm;
      min-height: 297mm;
      padding: 8mm;
      border-left: none;
      border-right: none;
    }

    .page-pair .puzzle + .puzzle {
      border-left: none;
    }

    hr.puzzle-divider {
      width: 80%;
      border: none;
      border-top: 4px solid #000;
      margin: 8mm 0;
    }

    table {
      border-collapse: separate;
      border-spacing: 4px;
      border: none;
    }

    /* grid (solution) cells */
    td.cell {
      width: 40px;
      height: 40px;
      min-width: 40px;
      border: 1px solid #aaa;
      border-radius: 7px;
      text-align: center;
      vertical-align: middle;
      font-size: 16px;
      padding: 0;
    }

    /* all clue cells: no border, monospace, single line */
    td.clue {
      border: none;
      padding: 0;
      font-family: "Roboto Mono", monospace;
      font-size: 12px;
      font-weight: 100;
      white-space: nowrap;
      overflow: visible;
    }

    .clue > span {
      display: flex;
    }

    /* horizontal clues (left/right of grid): fixed row height, text hugs the grid */
    .clue-left, .clue-right {
      height: 40px;
    }
    .clue-left > span {
      justify-content: flex-end;
      align-items: center;
      height: 40px;
      padding-right: 8px;
    }
    .clue-right > span {
      justify-content: flex-start;
      align-items: center;
      height: 40px;
      padding-left: 8px;
    }

    /* vertical clues (top/bottom of grid): rotated, content-sized */
    .clue-top, .clue-bottom {
      width: 40px;
      text-align: center;
    }
    .clue-top {
      vertical-align: bottom;
    }
    .clue-bottom {
      vertical-align: top;
    }
    .clue-top > span, .clue-bottom > span {
      writing-mode: vertical-lr;
      display: inline-block;
    }
    .clue-top > span {
      padding-bottom: 8px;
    }
    .clue-bottom > span {
      padding-top: 8px;
    }"""

# ---------------------------------------------------------------------------
# Distribution
# ---------------------------------------------------------------------------

def puzzle_weight(puzzle: dict) -> int:
    rows = len(puzzle["left_to_right"])
    bottom = [c for c in puzzle["down_to_up"] if c]
    M = max(len(c) for c in bottom) if bottom else 0
    return rows + math.ceil(M / WEIGHT_K) if M else rows


def max_per_page(w: int) -> int:
    if w < WEIGHT_T1: return 4
    if w < WEIGHT_T2: return 3
    if w < WEIGHT_T3: return 2
    return 1


def distribute(sections: list) -> list:
    """Return a list of pages; each page is a list of (pack_name, puzzle) tuples."""
    pages = []
    current = []   # puzzles accumulating on the current page
    current_cap = 4  # max puzzles allowed on the current page

    for pack_name, puzzles in sections:
        for puzzle in puzzles:
            w = puzzle_weight(puzzle)
            cap = max_per_page(w)
            # start a new page if this puzzle's capacity is lower than the page's,
            # or the page is already full
            if cap < current_cap or len(current) >= current_cap:
                if current:
                    pages.append(current)
                current = []
                current_cap = cap
            current.append((pack_name, puzzle))

    if current:
        pages.append(current)

    return pages


def page_h_weight(page: list) -> int:
    """Max horizontal weight across all puzzles on a page."""
    def puzzle_h_weight(puzzle):
        cols = len(puzzle["up_to_down"])
        right = [c for c in puzzle["right_to_left"] if c]
        M = max(len(c) for c in right) if right else 0
        return cols + math.ceil(M / WEIGHT_KH) if M else cols
    return max(puzzle_h_weight(p) for _, p in page)


def h_distribute(pages: list) -> list:
    """Pair adjacent pages side by side where both have W_horizontal < T_horizontal.
    Returns a list of items, each either a single page or a (left, right) pair."""
    result = []
    pending = None
    for page in pages:
        wh = page_h_weight(page)
        if wh >= WEIGHT_TH:
            if pending is not None:
                result.append(pending)
                pending = None
            result.append(page)
        else:
            if pending is not None:
                result.append((pending, page))
                pending = None
            else:
                pending = page
    if pending is not None:
        result.append(pending)
    return result

# ---------------------------------------------------------------------------
# HTML generation
# ---------------------------------------------------------------------------

def esc(s: str) -> str:
    return htmllib.escape(s, quote=False)


def render_puzzle_content(pack_name: str, puzzle: dict) -> str:
    """Render heading + table for one puzzle (no outer .puzzle div)."""
    top = puzzle["up_to_down"]
    bottom = puzzle["down_to_up"]
    left = puzzle["left_to_right"]
    right = puzzle["right_to_left"]

    ncols = len(top)
    nrows = len(left)
    has_bottom = any(bottom)
    has_right = any(right)

    out = []
    pack_num = pack_name.split("_")[0]
    out.append(f'    <h2>[{esc(pack_num)}] {esc(puzzle["title"])}</h2>')
    out.append("    <table>")

    out.append("      <!-- top clue row -->")
    out.append("      <tr>")
    out.append('        <td class="clue"></td>')
    for c in top:
        out.append(f'        <td class="clue clue-top"><span>{esc(c)}</span></td>')
    if has_right:
        out.append('        <td class="clue"></td>')
    out.append("      </tr>")

    cells = '<td class="cell"></td>' * ncols
    for i in range(nrows):
        out.append(f"      <!-- row {i + 1} -->")
        out.append("      <tr>")
        out.append(f'        <td class="clue clue-left"><span>{esc(left[i])}</span></td>')
        out.append(f"        {cells}")
        if has_right:
            out.append(f'        <td class="clue clue-right"><span>{esc(right[i])}</span></td>')
        out.append("      </tr>")

    if has_bottom:
        out.append("      <!-- bottom clue row -->")
        out.append("      <tr>")
        out.append('        <td class="clue"></td>')
        for c in bottom:
            out.append(f'        <td class="clue clue-bottom"><span>{esc(c)}</span></td>')
        if has_right:
            out.append('        <td class="clue"></td>')
        out.append("      </tr>")

    out.append("    </table>")
    return "\n".join(out)


def render_page(page: list, number: int, total: int, show_number: bool = True) -> str:
    """Wrap one or more puzzles in a single .puzzle div."""
    parts = []
    for i, (pack_name, p) in enumerate(page):
        if i > 0:
            parts.append('    <hr class="puzzle-divider">')
        parts.append(render_puzzle_content(pack_name, p))
    contents = "\n\n".join(parts)
    digits = len(str(total))
    label = f"--- {str(number).zfill(digits)} ---"
    number_div = f'    <div class="page-number">{label}</div>\n' if show_number else ""
    return (
        f'  <div class="puzzle">\n'
        f'{contents}\n'
        f'{number_div}'
        f'  </div>'
    )


def render_document(sections: list) -> str:
    pages = distribute(sections)
    layout = h_distribute(pages)
    total = len(layout)
    digits = len(str(total))

    blocks = []
    for i, item in enumerate(layout, 1):
        label = f"--- {str(i).zfill(digits)} ---"
        if isinstance(item, tuple):
            left, right = item
            left_div  = render_page(left,  i, total, show_number=False)
            right_div = render_page(right, i, total, show_number=False)
            blocks.append(
                f'  <div class="page-pair">\n'
                f'{left_div}\n'
                f'{right_div}\n'
                f'    <div class="page-number">{label}</div>\n'
                f'  </div>'
            )
        else:
            blocks.append(render_page(item, i, total))

    body = "\n\n".join(blocks)
    return (
        "<!DOCTYPE html>\n"
        '<html lang="en">\n'
        "<head>\n"
        '  <meta charset="UTF-8">\n'
        '  <meta name="viewport" content="width=device-width, initial-scale=1.0">\n'
        '  <link rel="preconnect" href="https://fonts.googleapis.com">\n'
        '  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>\n'
        '  <link href="https://fonts.googleapis.com/css2?family=Roboto+Mono:wght@100;300;400&display=swap" rel="stylesheet">\n'
        "  <title>Regex Crosswords</title>\n"
        "  <style>\n"
        f"{CSS}\n"
        "  </style>\n"
        "</head>\n"
        "<body>\n\n"
        f"{body}\n\n"
        "</body>\n"
        "</html>\n"
    )


def generate(pack_paths: list, out_path: Path) -> None:
    sections = []
    total = 0
    for pack_path in pack_paths:
        if not pack_path.exists():
            sys.exit(f"pack not found: {pack_path}")
        puzzles = json.loads(pack_path.read_text())
        sections.append((pack_path.stem, puzzles))
        total += len(puzzles)
    out_path.write_text(render_document(sections))
    names = ", ".join(p.stem for p in pack_paths)
    print(f"wrote {out_path} ({total} puzzles from {names})")

# ---------------------------------------------------------------------------
# PDF export
# ---------------------------------------------------------------------------

def export(html_path: Path, pdf_path: Path) -> None:
    from playwright.sync_api import sync_playwright
    url = html_path.resolve().as_uri()
    with sync_playwright() as p:
        browser = p.chromium.launch()
        page = browser.new_page()
        page.goto(url, wait_until="networkidle")
        page.pdf(
            path=str(pdf_path),
            format="A4",
            print_background=True,
            prefer_css_page_size=True,
            margin={"top": "0", "right": "0", "bottom": "0", "left": "0"},
        )
        browser.close()
    print(f"wrote {pdf_path}")

# ---------------------------------------------------------------------------
# CLI
# ---------------------------------------------------------------------------

def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--generate", "-g", action="store_true", help="generate HTML from JSON packs")
    parser.add_argument("--export", "-e", action="store_true", help="export HTML to PDF via Playwright")
    parser.add_argument("-o", "--output", type=Path, default=DEFAULT_HTML, metavar="PATH", help="HTML output path")
    parser.add_argument("-p", "--pdf", type=Path, default=DEFAULT_PDF, metavar="PATH", help="PDF output path")
    parser.add_argument("packs", nargs="*", type=Path, metavar="pack.json", help="pack files (default: all in 01___info___copiedjsons/)")
    args = parser.parse_args()

    if not args.generate and not args.export:
        parser.error("specify at least one of --generate or --export")

    if args.generate:
        pack_paths = args.packs if args.packs else sorted(PACKS_DIR.glob("*.json"))
        if not pack_paths:
            sys.exit(f"no packs found in {PACKS_DIR}")
        generate(pack_paths, args.output)

    if args.export:
        if not args.output.exists():
            sys.exit(f"HTML not found: {args.output} — run --generate first")
        export(args.output, args.pdf)


if __name__ == "__main__":
    main()
