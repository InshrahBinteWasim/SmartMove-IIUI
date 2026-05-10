package com.example.smartmoveiiui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartmoveiiui.data.model.Schedule
import com.example.smartmoveiiui.databinding.ActivityScheduleBinding
import com.example.smartmoveiiui.ui.adapter.ScheduleAdapter
import com.google.firebase.firestore.FirebaseFirestore

class ScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.schToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupRecyclerView()
        fetchSchedules()
    }

    private fun setupRecyclerView() {
        binding.rvSchedules.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchSchedules() {
        binding.schProgress.visibility = View.VISIBLE
        db.collection("schedules")
            .get()
            .addOnSuccessListener { documents ->
                binding.schProgress.visibility = View.GONE
                val scheduleList = documents.toObjects(Schedule::class.java)
                binding.rvSchedules.adapter = ScheduleAdapter(scheduleList)
            }
            .addOnFailureListener {
                binding.schProgress.visibility = View.GONE
            }
    }
}
