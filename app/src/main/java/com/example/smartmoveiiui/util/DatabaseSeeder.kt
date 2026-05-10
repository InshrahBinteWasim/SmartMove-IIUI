package com.example.smartmoveiiui.util

import android.util.Log
import com.example.smartmoveiiui.data.model.User
import com.example.smartmoveiiui.model.AppRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object DatabaseSeeder {
    fun seedUsers() {
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
                            db.collection("users").document(firebaseUser.uid)
                                .set(userModel)
                                .addOnSuccessListener {
                                    Log.d("Seeder", "User registered and added to Firestore: $email")
                                }
                        }
                    } else {
                        Log.e("Seeder", "Failed to register $email: ${task.exception?.message}")
                        // If user already exists, we might still want to ensure Firestore is updated
                        // But for a seeder, we usually just log it.
                    }
                }
        }
    }
}
