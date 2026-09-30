package com.example.icola

object InstagramPackages {
    const val INSTAGRAM = "com.instagram.android"

    // Our own heads-up notification is reported by SystemUI; it must not reset tracker state.
    private val IGNORED = setOf("com.android.systemui")

    fun leavesInstagram(pkg: String, ownPackage: String): Boolean =
        pkg != INSTAGRAM && pkg != ownPackage && pkg !in IGNORED
}
