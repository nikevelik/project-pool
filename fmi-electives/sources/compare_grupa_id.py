#!/usr/bin/env python3
"""Compare 'Група ИД' column values against an allowed list."""

import re
import sys
from html.parser import HTMLParser
from pathlib import Path

ALLOWED = {
    "Др.", "Д", "Други", "КП", "М", "M", "ОКН", "ПМ",
    "ПМ / Ст", "ПМ/ Стат", "ПМ/Стат", "ПМСтат", "С", "ЯКН", "ЯКН/ОКН",
}

HEADER_NAME = "Група ИД"


class TableParser(HTMLParser):
    def __init__(self):
        super().__init__()
        self.rows = []
        self._row = None
        self._cell = None
        self._in_td = False

    def handle_starttag(self, tag, attrs):
        if tag == "tr":
            self._row = []
        elif tag == "td":
            self._in_td = True
            self._cell = []

    def handle_endtag(self, tag):
        if tag == "tr" and self._row is not None:
            self.rows.append(self._row)
            self._row = None
        elif tag == "td" and self._in_td:
            self._row.append("".join(self._cell).strip())
            self._in_td = False
            self._cell = None

    def handle_data(self, data):
        if self._in_td:
            self._cell.append(data)


def main():
    path = Path(__file__).parent / "Zimen2026-2027.html"
    content = path.read_text(encoding="utf-8")

    parser = TableParser()
    parser.feed(content)

    rows = parser.rows
    if not rows:
        print("No rows found.")
        return

    header = rows[0]
    try:
        col_idx = header.index(HEADER_NAME)
    except ValueError:
        print(f"Column '{HEADER_NAME}' not found in header: {header}")
        sys.exit(1)

    mismatches = []
    seen_values = set()
    for row_num, row in enumerate(rows[1:], start=2):
        if col_idx >= len(row):
            continue
        value = row[col_idx].strip()
        if not value:
            continue
        seen_values.add(value)
        if value not in ALLOWED:
            discipline = row[1] if len(row) > 1 else ""
            mismatches.append((row_num, value, discipline))

    print(f"Total data rows: {len(rows) - 1}")
    print(f"Distinct values in '{HEADER_NAME}': {len(seen_values)}")
    print()

    if mismatches:
        print(f"Values NOT in allowed list ({len(mismatches)} rows):")
        for row_num, value, discipline in mismatches:
            print(f"  Row {row_num}: '{value}'  ({discipline})")
    else:
        print("All values are within the allowed list.")

    print()
    unknown_values = sorted({v for _, v, _ in mismatches})
    if unknown_values:
        print("Distinct unknown values:")
        for v in unknown_values:
            print(f"  '{v}'")


if __name__ == "__main__":
    main()
