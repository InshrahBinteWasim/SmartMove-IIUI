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
            Announcement("1", "staff_1", "Welcome to SmartMove!", "We are live! Track your IIUI buses in real-time now.", Timestamp.now(), true),
            Announcement("2", "staff_1", "Route #2 Delay", "Due to fog, Route 2 (Saddar) is running 15 mins late.", Timestamp.now(), true)
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
}
