package com.example.icola

/** Plain-data copy of an accessibility node, so detection can run without the Android framework. */
data class NodeSnapshot(
    val viewId: String?,
    val label: String?,
    val selected: Boolean,
    val children: List<NodeSnapshot> = emptyList()
)
