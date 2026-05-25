package com.example.smartmoveiiui.navigation

import androidx.annotation.StringRes
import com.example.smartmoveiiui.R

/**
 * Stable feature IDs for navigation + placeholder titles (no dashboard widgets on homes).
 */
object FeatureCatalog {

    object Commuter {
        const val ROLE_SWITCHER = "commuter_role_switcher"
        const val LIVE_TRACKING = "commuter_live_tracking"
        const val ROUTE_DETAIL = "commuter_route_detail"
        const val VIEW_SCHEDULE = "commuter_view_schedule"
        const val SCHEDULE_DETAIL = "commuter_schedule_detail"
        const val CHATBOT = "commuter_chatbot"
        const val SUBMIT_QUERY = "commuter_submit_query"
        const val MY_QUERIES = "commuter_my_queries"
        const val ANNOUNCEMENTS = "commuter_announcements"
        const val NOTIFICATIONS = "commuter_notifications"
        const val NOTIFICATION_SETTINGS = "commuter_notification_settings"
        const val FAVORITES = "commuter_favorites"
        const val PROFILE_SETTINGS = "commuter_profile_settings"
        const val FEEDBACK = "commuter_feedback"
    }

    object Staff {
        const val LIVE_TRACKING = "staff_live_tracking"
        const val LIVE_BUS_MONITOR = "staff_live_bus_monitor"
        const val POST_ANNOUNCEMENTS = "staff_post_announcements"
        const val ANNOUNCEMENT_HISTORY = "staff_announcement_history"
        const val MANAGE_QUERIES = "staff_manage_queries"
        const val STAFF_PROFILE = "staff_profile"
    }

    object Admin {
        const val MANAGE_USERS = "admin_manage_users"
        const val MANAGE_BUSES = "admin_manage_buses"
        const val MANAGE_SCHEDULE = "admin_manage_schedule"
        const val MANAGE_ROUTES = "admin_manage_routes"
        const val LIVE_TRACKING = "admin_live_tracking"
        const val REPORTS_LOGS = "admin_reports_logs"
        const val AUDIT_LOGS = "admin_audit_logs"
    }

    @StringRes
    fun titleFor(featureId: String): Int = when (featureId) {
        Commuter.ROLE_SWITCHER -> R.string.feat_role_switcher
        Commuter.LIVE_TRACKING -> R.string.feat_commuter_live_tracking
        Commuter.ROUTE_DETAIL -> R.string.feat_commuter_route_detail
        Commuter.VIEW_SCHEDULE -> R.string.feat_commuter_schedule
        Commuter.SCHEDULE_DETAIL -> R.string.feat_commuter_schedule_detail
        Commuter.CHATBOT -> R.string.feat_commuter_chatbot
        Commuter.SUBMIT_QUERY -> R.string.feat_commuter_query
        Commuter.MY_QUERIES -> R.string.feat_commuter_my_queries
        Commuter.ANNOUNCEMENTS -> R.string.feat_commuter_announcements
        Commuter.NOTIFICATIONS -> R.string.feat_commuter_notifications
        Commuter.NOTIFICATION_SETTINGS -> R.string.feat_commuter_notification_settings
        Commuter.FAVORITES -> R.string.feat_commuter_favorites
        Commuter.PROFILE_SETTINGS -> R.string.feat_commuter_profile
        Commuter.FEEDBACK -> R.string.feat_commuter_feedback
        Staff.LIVE_TRACKING -> R.string.feat_staff_live_tracking
        Staff.LIVE_BUS_MONITOR -> R.string.feat_staff_live_monitor
        Staff.POST_ANNOUNCEMENTS -> R.string.feat_staff_announcements
        Staff.ANNOUNCEMENT_HISTORY -> R.string.feat_staff_announcement_history
        Staff.MANAGE_QUERIES -> R.string.feat_staff_queries
        Staff.STAFF_PROFILE -> R.string.feat_staff_profile
        Admin.MANAGE_USERS -> R.string.feat_admin_users
        Admin.MANAGE_BUSES -> R.string.feat_admin_buses
        Admin.MANAGE_SCHEDULE -> R.string.feat_admin_schedule
        Admin.MANAGE_ROUTES -> R.string.feat_admin_routes
        Admin.LIVE_TRACKING -> R.string.feat_admin_live_tracking
        Admin.REPORTS_LOGS -> R.string.feat_admin_reports_logs
        Admin.AUDIT_LOGS -> R.string.feat_admin_audit_logs
        else -> R.string.feat_unknown
    }

    @StringRes
    fun subtitleFor(featureId: String): Int = when (featureId) {
        Commuter.ROLE_SWITCHER -> R.string.feat_role_switcher_sub
        Commuter.LIVE_TRACKING -> R.string.feat_commuter_live_tracking_sub
        Commuter.ROUTE_DETAIL -> R.string.feat_commuter_route_detail_sub
        Commuter.VIEW_SCHEDULE -> R.string.feat_commuter_schedule_sub
        Commuter.SCHEDULE_DETAIL -> R.string.feat_commuter_schedule_detail_sub
        Commuter.CHATBOT -> R.string.feat_commuter_chatbot_sub
        Commuter.SUBMIT_QUERY -> R.string.feat_commuter_query_sub
        Commuter.MY_QUERIES -> R.string.feat_commuter_my_queries_sub
        Commuter.ANNOUNCEMENTS -> R.string.feat_commuter_announcements_sub
        Commuter.NOTIFICATIONS -> R.string.feat_commuter_notifications_sub
        Commuter.NOTIFICATION_SETTINGS -> R.string.feat_commuter_notification_settings_sub
        Commuter.FAVORITES -> R.string.feat_commuter_favorites_sub
        Commuter.PROFILE_SETTINGS -> R.string.feat_commuter_profile_sub
        Commuter.FEEDBACK -> R.string.feat_commuter_feedback_sub
        Staff.LIVE_TRACKING -> R.string.feat_staff_live_tracking_sub
        Staff.LIVE_BUS_MONITOR -> R.string.feat_staff_live_monitor_sub
        Staff.POST_ANNOUNCEMENTS -> R.string.feat_staff_announcements_sub
        Staff.ANNOUNCEMENT_HISTORY -> R.string.feat_staff_announcement_history_sub
        Staff.MANAGE_QUERIES -> R.string.feat_staff_queries_sub
        Staff.STAFF_PROFILE -> R.string.feat_staff_profile_sub
        Admin.MANAGE_USERS -> R.string.feat_admin_users_sub
        Admin.MANAGE_BUSES -> R.string.feat_admin_buses_sub
        Admin.MANAGE_SCHEDULE -> R.string.feat_admin_schedule_sub
        Admin.MANAGE_ROUTES -> R.string.feat_admin_routes_sub
        Admin.LIVE_TRACKING -> R.string.feat_admin_live_tracking_sub
        Admin.REPORTS_LOGS -> R.string.feat_admin_reports_logs_sub
        Admin.AUDIT_LOGS -> R.string.feat_admin_audit_logs_sub
        else -> R.string.feat_unknown_sub
    }
}
