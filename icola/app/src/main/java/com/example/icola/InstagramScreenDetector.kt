package com.example.icola

object InstagramScreenDetector {

    const val HOME_TAB_ID = "${InstagramPackages.INSTAGRAM}:id/feed_tab"
    const val REELS_TAB_ID = "${InstagramPackages.INSTAGRAM}:id/clips_tab"

    fun detect(root: NodeSnapshot): Screen =
        byId(root) ?: freshHome(root) ?: byLabel(root) ?: Screen.OTHER

    /**
     * A freshly started Instagram may not mark any tab as selected yet. If the Home tab is
     * present and no `_tab` node is selected, assume that untouched state means Home.
     */
    private fun freshHome(root: NodeSnapshot): Screen? {
        val tabs = mutableListOf<NodeSnapshot>()
        collectTabs(root, tabs)
        val hasHomeTab = tabs.any { it.viewId == HOME_TAB_ID }
        return if (hasHomeTab && tabs.none { it.selected }) Screen.HOME else null
    }

    private fun collectTabs(node: NodeSnapshot, out: MutableList<NodeSnapshot>) {
        if (node.viewId?.endsWith("_tab") == true) out.add(node)
        node.children.forEach { collectTabs(it, out) }
    }

    private fun byId(node: NodeSnapshot): Screen? {
        if (node.selected) {
            when (node.viewId) {
                HOME_TAB_ID -> return Screen.HOME
                REELS_TAB_ID -> return Screen.REELS
            }
        }
        return node.children.firstNotNullOfOrNull { byId(it) }
    }

    /** Fallback for renamed IDs: a selected node labelled "Home" or "Reels". */
    private fun byLabel(node: NodeSnapshot): Screen? {
        if (node.selected) {
            val label = node.label?.lowercase()
            when {
                label == null -> Unit
                label.startsWith("reels") -> return Screen.REELS
                label.startsWith("home") -> return Screen.HOME
            }
        }
        return node.children.firstNotNullOfOrNull { byLabel(it) }
    }
}
