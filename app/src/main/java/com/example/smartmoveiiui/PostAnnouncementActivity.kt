package com.example.smartmoveiiui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmoveiiui.data.model.Announcement
import com.example.smartmoveiiui.databinding.ActivityPostAnnouncementBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class PostAnnouncementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostAnnouncementBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostAnnouncementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.postAnnToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnPublish.setOnClickListener {
            publishAnnouncement()
        }
    }

    private fun publishAnnouncement() {
        val title = binding.annTitleEdit.text.toString().trim()
        val desc = binding.annDescEdit.text.toString().trim()
        val userId = auth.currentUser?.uid ?: return

        if (title.isEmpty()) {
            binding.annTitleLayout.error = "Title required"
            return
        }
        binding.annTitleLayout.error = null

        if (desc.isEmpty()) {
            binding.annDescLayout.error = "Message required"
            return
        }
        binding.annDescLayout.error = null

        binding.postAnnProgress.visibility = View.VISIBLE
        binding.btnPublish.isEnabled = false

        val annId = UUID.randomUUID().toString()
        val announcement = Announcement(
            announcementId = annId,
            staffId = userId,
            title = title,
            description = desc,
            publishDate = Timestamp.now(),
            isPublished = true
        )

        // --- CREDIT SAVING STRATEGY: Targeted Write ---
        db.collection("announcements").document(annId)
            .set(announcement)
            .addOnSuccessListener {
                Toast.makeText(this, "Broadcast successful", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                binding.postAnnProgress.visibility = View.GONE
                binding.btnPublish.isEnabled = true
                Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
