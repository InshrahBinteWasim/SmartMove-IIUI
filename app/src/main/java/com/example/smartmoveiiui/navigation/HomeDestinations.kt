package com.example.smartmoveiiui.navigation

object HomeDestinations {
    const val COMMUTER_HOME = "home_commuter"
    const val STAFF_HOME = "home_staff"
    const val ADMIN_HOME = "home_admin"
    const val PLACEHOLDER = "feature/{featureId}"
    fun placeholder(featureId: String) = "feature/$featureId"
}
