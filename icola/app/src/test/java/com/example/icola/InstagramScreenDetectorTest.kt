package com.example.icola

import org.junit.Assert.assertEquals
import org.junit.Test

class InstagramScreenDetectorTest {

    private fun node(
        viewId: String? = null,
        label: String? = null,
        selected: Boolean = false,
        children: List<NodeSnapshot> = emptyList()
    ) = NodeSnapshot(viewId, label, selected, children)

    private fun tabBar(homeSelected: Boolean, reelsSelected: Boolean) = node(
        children = listOf(
            node(InstagramScreenDetector.HOME_TAB_ID, "Home", homeSelected),
            node("com.instagram.android:id/search_tab", "Search and explore", false),
            node(InstagramScreenDetector.REELS_TAB_ID, "Reels", reelsSelected),
            node("com.instagram.android:id/profile_tab", "Profile", false)
        )
    )

    @Test
    fun selectedHomeTabIsHome() {
        assertEquals(Screen.HOME, InstagramScreenDetector.detect(tabBar(true, false)))
    }

    @Test
    fun selectedReelsTabIsReels() {
        assertEquals(Screen.REELS, InstagramScreenDetector.detect(tabBar(false, true)))
    }

    @Test
    fun tabBarWithNothingSelectedIsTreatedAsFreshHome() {
        assertEquals(Screen.HOME, InstagramScreenDetector.detect(tabBar(false, false)))
    }

    @Test
    fun nothingSelectedButNoHomeTabIsOther() {
        val root = node(
            children = listOf(
                node("com.instagram.android:id/search_tab", "Search", false),
                node("com.instagram.android:id/profile_tab", "Profile", false)
            )
        )
        assertEquals(Screen.OTHER, InstagramScreenDetector.detect(root))
    }

    @Test
    fun anySelectedTabPreventsFreshHome() {
        val root = node(
            children = listOf(
                node(InstagramScreenDetector.HOME_TAB_ID, "Home", false),
                node("com.instagram.android:id/some_inner_tab", null, true)
            )
        )
        assertEquals(Screen.OTHER, InstagramScreenDetector.detect(root))
    }

    @Test
    fun nonHomeNonReelsTabSelectedIsOther() {
        val root = node(
            children = listOf(
                node(InstagramScreenDetector.HOME_TAB_ID, "Home", false),
                node("com.instagram.android:id/profile_tab", "Profile", true)
            )
        )
        assertEquals(Screen.OTHER, InstagramScreenDetector.detect(root))
    }

    @Test
    fun deeplyNestedTabIsFound() {
        val root = node(children = listOf(node(children = listOf(node(children = listOf(
            node(InstagramScreenDetector.REELS_TAB_ID, null, true)
        ))))))
        assertEquals(Screen.REELS, InstagramScreenDetector.detect(root))
    }

    @Test
    fun labelFallbackWorksWhenIdsAreUnknown() {
        val home = node(children = listOf(node("x:id/renamed", "Home", true)))
        val reels = node(children = listOf(node("x:id/renamed", "Reels", true)))
        assertEquals(Screen.HOME, InstagramScreenDetector.detect(home))
        assertEquals(Screen.REELS, InstagramScreenDetector.detect(reels))
    }

    @Test
    fun labelFallbackIsCaseInsensitivePrefixMatch() {
        val root = node(children = listOf(node(null, "REELS, tab 3 of 5", true)))
        assertEquals(Screen.REELS, InstagramScreenDetector.detect(root))
    }

    @Test
    fun unselectedLabelledNodeIsIgnored() {
        val root = node(children = listOf(node(null, "Home", false)))
        assertEquals(Screen.OTHER, InstagramScreenDetector.detect(root))
    }

    @Test
    fun nodesWithNullIdAndNullLabelDoNotCrash() {
        val root = node(children = listOf(node(null, null, true), node(null, null, false)))
        assertEquals(Screen.OTHER, InstagramScreenDetector.detect(root))
    }

    @Test
    fun idMatchWinsOverLabelMatch() {
        val root = node(
            children = listOf(
                node(null, "Reels", true),
                node(InstagramScreenDetector.HOME_TAB_ID, null, true)
            )
        )
        assertEquals(Screen.HOME, InstagramScreenDetector.detect(root))
    }
}
