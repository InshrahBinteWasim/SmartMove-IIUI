package com.example.smartmoveiiui.model

/**
 * Maps to documented SmartMove IIUI role modules (post authentication).
 */
enum class AppRole {
    COMMUTER,
    TRANSPORT_STAFF,
    SYSTEM_ADMINISTRATOR,
    ;

    companion object {
        const val EXTRA_NAME: String = "com.example.smartmoveiiui.APP_ROLE"

        fun fromIntentExtra(extra: String?): AppRole {
            if (extra.isNullOrBlank()) return COMMUTER
            return entries.find { it.name == extra } ?: COMMUTER
        }
    }
}
