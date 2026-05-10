package com.example.smartmoveiiui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartmoveiiui.data.model.Announcement
import com.example.smartmoveiiui.databinding.ActivityAnnouncementsBinding
import com.example.smartmoveiiui.ui.adapter.AnnouncementAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AnnouncementsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnnouncementsBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnnouncementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.annToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupRecyclerView()
        fetchAnnouncements()
    }

    private fun setupRecyclerView() {
        binding.rvAnnouncements.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchAnnouncements() {
        binding.annProgress.visibility = View.VISIBLE
        db.collection("announcements")
            .whereEqualTo("isPublished", true)
            .orderBy("publishDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                binding.annProgress.visibility = View.GONE
                val announcementList = documents.toObjects(Announcement::class.java)
                if (announcementList.isEmpty()) {
                    binding.tvNoAnnouncements.visibility = View.VISIBLE
                } else {
                    binding.tvNoAnnouncements.visibility = View.GONE
                    binding.rvAnnouncements.adapter = AnnouncementAdapter(announcementList)
                }
            }
            .addOnFailureListener {
                binding.annProgress.visibility = View.GONE
                binding.tvNoAnnouncements.text = "Error loading announcements"
                binding.tvNoAnnouncements.visibility = View.VISIBLE
            }
    }
}
