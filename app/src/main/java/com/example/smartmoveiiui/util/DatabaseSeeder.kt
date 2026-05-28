package com.example.smartmoveiiui.util

import android.content.Context
import androidx.core.content.edit
import com.example.smartmoveiiui.data.model.*
import com.example.smartmoveiiui.model.AppRole
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object DatabaseSeeder {
    private const val PREFS_NAME = "SmartMovePrefs"
    private const val KEY_IS_SEEDED = "is_database_seeded"
    private const val KEY_DEMO_DATA_SEEDED = "is_demo_transport_data_seeded_v2"

    fun seedAll(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_IS_SEEDED, false)) {
            // --- CREDIT SAVING STRATEGY: Zero-Read Check ---
            // Local flag is set, so we don't even check Firebase. Saves 1 read per app open.
            return
        }

        seedUsers()
        seedAnnouncements()
        seedSchedules()
        seedBusLocations()

        // Set the flag so we don't run this again on this device
        prefs.edit(commit = true) { putBoolean(KEY_IS_SEEDED, true) }
    }

    private fun seedUsers() {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        val usersToRegister = listOf(
            Triple("inshrah.bsse4518@iiu.edu.pk", "3740203014500", AppRole.COMMUTER.name),
            Triple("ayesha.bsse4509@iiu.edu.pk", "iiui12345", AppRole.TRANSPORT_STAFF.name),
            Triple("maryam.bsse4526@gmail.com", "iiui12345", AppRole.SYSTEM_ADMINISTRATOR.name)
        )

        usersToRegister.forEach { (email, password, role) ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = task.result?.user
                        if (firebaseUser != null) {
                            val userModel = User(
                                userId = firebaseUser.uid,
                                name = email.substringBefore("@"),
                                email = email,
                                role = role
                            )
                            db.collection("users").document(firebaseUser.uid).set(userModel)
                        }
                    }
                }
        }
    }

    private fun seedAnnouncements() {
        val db = FirebaseFirestore.getInstance()
        val sampleAnnouncements = listOf(
            Announcement("1", "staff_1", "Welcome to SmartMove!", "We are live! Track your IIUI buses in real-time now.", "All commuters", "Normal", "", Timestamp.now(), true),
            Announcement("2", "staff_1", "Route #2 Delay", "Due to fog, Route 2 (Saddar) is running 15 mins late.", "Saddar commuters", "High", "", Timestamp.now(), true)
        )
        sampleAnnouncements.forEach { db.collection("announcements").document(it.announcementId).set(it) }
    }

    private fun seedSchedules() {
        val db = FirebaseFirestore.getInstance()
        val sampleSchedules = listOf(
            Schedule("sch_1", "IIUI-042", "Route 1", Timestamp.now(), mapOf("Saddar" to "07:30 AM", "IIUI" to "08:15 AM")),
            Schedule("sch_2", "IIUI-088", "Route 2", Timestamp.now(), mapOf("Faizabad" to "07:45 AM", "IIUI" to "08:30 AM"))
        )
        sampleSchedules.forEach { db.collection("schedules").document(it.scheduleId).set(it) }
    }

    private fun seedBusLocations() {
        val db = FirebaseFirestore.getInstance()
        val sampleLocations = listOf(
            BusLocation("IIUI-042", 33.6844, 73.0479, Timestamp.now()),
            BusLocation("IIUI-088", 33.6860, 73.0490, Timestamp.now())
        )
        sampleLocations.forEach { db.collection("bus_locations").document(it.busId).set(it) }
    }

    fun seedDemoTransportData(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_DEMO_DATA_SEEDED, false)) return

        val db = FirebaseFirestore.getInstance()
        val batch = db.batch()

        val buses = listOf(
            Bus("bus_03", "Bus 03", 52, "Arshad Ali", "Active", "GPS-03"),
            Bus("bus_05", "Bus 05", 48, "Tariq Usman", "Delayed", "GPS-05"),
            Bus("bus_07", "Bus 07", 52, "Arshad Ali", "Active", "GPS-07"),
            Bus("bus_11", "Bus 11", 45, "Nasir Mehmood", "Traffic", "GPS-11"),
            Bus("bus_14", "Bus 14", 50, "Tariq Usman", "Weak GPS", "GPS-14")
        )
        buses.forEach { batch.set(db.collection("buses").document(it.busNumber), it) }

        val routes = listOf(
            Route("route_g10", "G-10 / F-10", listOf(Stop("g10", "G-10 Markaz", 1), Stop("f10", "F-10 Gate", 2), Stop("iiui", "IIUI Gate 2", 3))),
            Route("route_faizabad", "Faizabad", listOf(Stop("faizabad", "Faizabad", 1), Stop("i8", "I-8", 2), Stop("iiui", "IIUI H-10", 3))),
            Route("route_saddar", "Rawalpindi Saddar", listOf(Stop("saddar", "Saddar", 1), Stop("committee", "Committee Chowk", 2), Stop("iiui", "IIUI H-10", 3))),
            Route("route_bhara", "Bhara Kahu", listOf(Stop("bhara", "Bhara Kahu", 1), Stop("tramri", "Tramri", 2), Stop("iiui", "IIUI H-10", 3)))
        )
        routes.forEach { batch.set(db.collection("routes").document(it.routeId), it) }

        val schedules = listOf(
            Schedule("sch_g10_male", "Bus 07", "G-10 / F-10", Timestamp.now(), mapOf("Morning" to "06:00 AM -> 08:15 AM", "Afternoon" to "01:40 PM -> 03:30 PM", "Evening" to "07:20 PM -> 09:00 PM")),
            Schedule("sch_female", "Bus 11", "Faizabad", Timestamp.now(), mapOf("Morning" to "06:00 AM -> 08:15 AM", "Afternoon" to "12:40 PM -> 02:30 PM", "Friday" to "01:30 PM -> 03:20 PM", "Evening" to "05:40 PM -> 08:00 PM"))
        )
        schedules.forEach { batch.set(db.collection("schedules").document(it.scheduleId), it) }

        val announcements = listOf(
            Announcement("ann_delay_g9", "staff_demo", "G-9 route delay", "F-8 and G-9 buses may leave 8 minutes late due to traffic near Kashmir Highway.", "G-9/F-8 commuters", "High", "", Timestamp.now(), true),
            Announcement("ann_gate2", "staff_demo", "Evening departure from Gate 2", "Evening buses will depart from IIUI Gate 2 today.", "All commuters", "Normal", "", Timestamp.now(), true)
        )
        announcements.forEach { batch.set(db.collection("announcements").document(it.announcementId), it) }

        val notifications = listOf(
            AppNotification("not_bus07", "all", "Bus 07 arriving", "G-10 stop - approximately 4 minutes.", "GPS", false, Timestamp.now()),
            AppNotification("not_delay", "all", "Delay warning", "Faizabad route slowed near Murree Road.", "Delay", false, Timestamp.now())
        )
        notifications.forEach { batch.set(db.collection("notifications").document(it.notificationId), it) }

        val favorites = listOf(
            FavoriteRoute("fav_g10", "demo_user", "G-10 / F-10", "G-10 Markaz", "Bus 07", 4),
            FavoriteRoute("fav_f10", "demo_user", "G-10 / F-10", "F-10 Gate", "Bus 03", 9)
        )
        favorites.forEach { batch.set(db.collection("favorites").document(it.favoriteId), it) }

        val locations = listOf(
            BusLocation("Bus 07", 33.6844, 73.0479, Timestamp.now()),
            BusLocation("Bus 11", 33.6860, 73.0490, Timestamp.now()),
            BusLocation("Bus 05", 33.5969, 73.0528, Timestamp.now())
        )
        locations.forEach { batch.set(db.collection("bus_locations").document(it.busId), it) }

        val audits = listOf(
            AuditLog("audit_schedule", "admin_demo", "Schedule update", "Friday female slot changed to 01:30 PM", Timestamp.now()),
            AuditLog("audit_route", "admin_demo", "Route edit", "Tramri stop order changed", Timestamp.now())
        )
        audits.forEach { batch.set(db.collection("audit_logs").document(it.logId), it) }

        batch.commit()
            .addOnSuccessListener { prefs.edit(commit = true) { putBoolean(KEY_DEMO_DATA_SEEDED, true) } }
    }
}
