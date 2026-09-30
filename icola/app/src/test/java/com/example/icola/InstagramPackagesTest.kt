package com.example.icola

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InstagramPackagesTest {

    private val own = "com.example.icola"

    @Test
    fun instagramItselfIsNotLeaving() {
        assertFalse(InstagramPackages.leavesInstagram(InstagramPackages.INSTAGRAM, own))
    }

    @Test
    fun systemUiIsIgnoredSoOurOwnNotificationDoesNotResetState() {
        assertFalse(InstagramPackages.leavesInstagram("com.android.systemui", own))
    }

    @Test
    fun ownPackageIsIgnored() {
        assertFalse(InstagramPackages.leavesInstagram(own, own))
    }

    @Test
    fun launcherAndOtherAppsCountAsLeaving() {
        assertTrue(InstagramPackages.leavesInstagram("com.sec.android.app.launcher", own))
        assertTrue(InstagramPackages.leavesInstagram("com.whatsapp", own))
    }
}
