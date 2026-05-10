package com.example.smartmoveiiui.navigation

import androidx.annotation.StringRes
import com.example.smartmoveiiui.R

/**
 * Stable feature IDs for navigation + placeholder titles (no dashboard widgets on homes).
 */
object FeatureCatalog {

    object Commuter {
        const val LIVE_TRACKING = "commuter_live_tracking"
        const val VIEW_SCHEDULE = "commuter_view_schedule"
        const val CHATBOT = "commuter_chatbot"
        const val SUBMIT_QUERY = "commuter_submit_query"
        const val ANNOUNCEMENTS = "commuter_announcements"
    }

    object Staff {
        const val LIVE_TRACKING = "staff_live_tracking"
        const val POST_ANNOUNCEMENTS = "staff_post_announcements"
        const val MANAGE_QUERIES = "staff_manage_queries"
    }

    object Admin {
        const val MANAGE_USERS = "admin_manage_users"
        const val MANAGE_BUSES = "admin_manage_buses"
        const val MANAGE_SCHEDULE = "admin_manage_schedule"
        const val MANAGE_ROUTES = "admin_manage_routes"
        const val LIVE_TRACKING = "admin_live_tracking"
    }

    @StringRes
    fun titleFor(featureId: String): Int = when (featureId) {
        Commuter.LIVE_TRACKING -> R.string.feat_commuter_live_tracking
        Commuter.VIEW_SCHEDULE -> R.string.feat_commuter_schedule
        Commuter.CHATBOT -> R.string.feat_commuter_chatbot
        Commuter.SUBMIT_QUERY -> R.string.feat_commuter_query
        Commuter.ANNOUNCEMENTS -> R.string.feat_commuter_announcements
        Staff.LIVE_TRACKING -> R.string.feat_staff_live_tracking
        Staff.POST_ANNOUNCEMENTS -> R.string.feat_staff_announcements
        Staff.MANAGE_QUERIES -> R.string.feat_staff_queries
        Admin.MANAGE_USERS -> R.string.feat_admin_users
        Admin.MANAGE_BUSES -> R.string.feat_admin_buses
        Admin.MANAGE_SCHEDULE -> R.string.feat_admin_schedule
        Admin.MANAGE_ROUTES -> R.string.feat_admin_routes
        Admin.LIVE_TRACKING -> R.string.feat_admin_live_tracking
        else -> R.string.feat_unknown
    }

    @StringRes
    fun subtitleFor(featureId: String): Int = when (featureId) {
        Commuter.LIVE_TRACKING -> R.string.feat_commuter_live_tracking_sub
        Commuter.VIEW_SCHEDULE -> R.string.feat_commuter_schedule_sub
        Commuter.CHATBOT -> R.string.feat_commuter_chatbot_sub
        Commuter.SUBMIT_QUERY -> R.string.feat_commuter_query_sub
        Commuter.ANNOUNCEMENTS -> R.string.feat_commuter_announcements_sub
        Staff.LIVE_TRACKING -> R.string.feat_staff_live_tracking_sub
        Staff.POST_ANNOUNCEMENTS -> R.string.feat_staff_announcements_sub
        Staff.MANAGE_QUERIES -> R.string.feat_staff_queries_sub
        Admin.MANAGE_USERS -> R.string.feat_admin_users_sub
        Admin.MANAGE_BUSES -> R.string.feat_admin_buses_sub
        Admin.MANAGE_SCHEDULE -> R.string.feat_admin_schedule_sub
        Admin.MANAGE_ROUTES -> R.string.feat_admin_routes_sub
        Admin.LIVE_TRACKING -> R.string.feat_admin_live_tracking_sub
        else -> R.string.feat_unknown_sub
    }
}
