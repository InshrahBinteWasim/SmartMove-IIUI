package com.example.smartmoveiiui

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings

class SmartMoveApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // --- CREDIT SAVING STRATEGY #1: Offline Persistence ---
        // This ensures that Firestore saves data locally. 
        // Subsequent reads of the same data cost 0 credits.
        val db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(PersistentCacheSettings.newBuilder().build())
            .build()
        db.firestoreSettings = settings
    }
}
