package com.example.smartmoveiiui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartmoveiiui.data.model.Schedule
import com.example.smartmoveiiui.databinding.ActivityScheduleBinding
import com.example.smartmoveiiui.ui.adapter.ScheduleAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleBinding
    private lateinit var db: FirebaseFirestore
    private var listener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.schToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupRecyclerView()
        startListeningForSchedules()
    }

    private fun setupRecyclerView() {
        binding.rvSchedules.layoutManager = LinearLayoutManager(this)
    }

    private fun startListeningForSchedules() {
        // --- CREDIT SAVING STRATEGY: Snapshot Listeners + Cache ---
        binding.schProgress.visibility = View.VISIBLE
        listener = db.collection("schedules")
            .addSnapshotListener { snapshot, error ->
                binding.schProgress.visibility = View.GONE
                if (error != null) return@addSnapshotListener

                if (snapshot != null) {
                    val scheduleList = snapshot.toObjects(Schedule::class.java)
                    binding.rvSchedules.adapter = ScheduleAdapter(scheduleList)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.remove()
    }
}
