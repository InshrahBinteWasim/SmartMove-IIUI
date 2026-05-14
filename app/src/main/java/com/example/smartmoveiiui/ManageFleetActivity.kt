package com.example.smartmoveiiui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartmoveiiui.data.model.Bus
import com.example.smartmoveiiui.databinding.ActivityManageFleetBinding
import com.example.smartmoveiiui.ui.adapter.BusAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ManageFleetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageFleetBinding
    private lateinit var db: FirebaseFirestore
    private var listener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageFleetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.fleetToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.rvBuses.layoutManager = LinearLayoutManager(this)

        binding.fabAddBus.setOnClickListener {
            Toast.makeText(this, "Feature to add new bus coming soon", Toast.LENGTH_SHORT).show()
        }

        startListeningForFleet()
    }

    private fun startListeningForFleet() {
        // --- CREDIT SAVING STRATEGY: Snapshot Listener ---
        binding.fleetProgress.visibility = View.VISIBLE
        listener = db.collection("bus_locations") // In this model, locations and fleet details might be mixed or linked. Using bus_locations as registry for now.
            .addSnapshotListener { snapshot, error ->
                binding.fleetProgress.visibility = View.GONE
                if (error != null || snapshot == null) return@addSnapshotListener

                val busList = snapshot.toObjects(Bus::class.java)
                binding.rvBuses.adapter = BusAdapter(busList) { bus ->
                    Toast.makeText(this, "Editing ${bus.busNumber}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.remove()
    }
}
