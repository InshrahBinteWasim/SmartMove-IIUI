package com.example.smartmoveiiui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartmoveiiui.data.model.Announcement
import com.example.smartmoveiiui.databinding.ActivityAnnouncementsBinding
import com.example.smartmoveiiui.ui.adapter.AnnouncementAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class AnnouncementsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnnouncementsBinding
    private lateinit var db: FirebaseFirestore
    private var listener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnnouncementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.annToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupRecyclerView()
        startListeningForAnnouncements()
    }

    private fun setupRecyclerView() {
        binding.rvAnnouncements.layoutManager = LinearLayoutManager(this)
    }

    private fun startListeningForAnnouncements() {
        // --- CREDIT SAVING STRATEGY: Snapshot Listeners ---
        // addSnapshotListener is more efficient than repeated .get() calls.
        // It also leverages local caching automatically.
        binding.annProgress.visibility = View.VISIBLE
        listener = db.collection("announcements")
            .whereEqualTo("isPublished", true)
            .orderBy("publishDate", Query.Direction.DESCENDING)
            .limit(20) // CREDIT SAVING: Don't load 1000s of old announcements
            .addSnapshotListener { snapshot, error ->
                binding.annProgress.visibility = View.GONE
                if (error != null) {
                    binding.tvNoAnnouncements.text = getString(R.string.ann_error_loading)
                    binding.tvNoAnnouncements.visibility = View.VISIBLE
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val announcementList = snapshot.toObjects(Announcement::class.java)
                    if (announcementList.isEmpty()) {
                        binding.tvNoAnnouncements.visibility = View.VISIBLE
                    } else {
                        binding.tvNoAnnouncements.visibility = View.GONE
                        binding.rvAnnouncements.adapter = AnnouncementAdapter(announcementList)
                    }
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.remove() // Important to stop listening when activity is destroyed
    }
}
