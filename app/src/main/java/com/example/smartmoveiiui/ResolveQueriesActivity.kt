package com.example.smartmoveiiui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartmoveiiui.data.model.Query
import com.example.smartmoveiiui.databinding.ActivityResolveQueriesBinding
import com.example.smartmoveiiui.ui.adapter.QueryResolveAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ResolveQueriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResolveQueriesBinding
    private lateinit var db: FirebaseFirestore
    private var listener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResolveQueriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.resolveToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.rvResolveQueries.layoutManager = LinearLayoutManager(this)

        startListeningForQueries()
    }

    private fun startListeningForQueries() {
        // --- CREDIT SAVING STRATEGY: Snapshot Listener ---
        binding.resolveProgress.visibility = View.VISIBLE
        listener = db.collection("queries")
            .orderBy("time", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snapshot, error ->
                binding.resolveProgress.visibility = View.GONE
                if (error != null || snapshot == null) return@addSnapshotListener

                val queryList = snapshot.toObjects(Query::class.java)
                binding.rvResolveQueries.adapter = QueryResolveAdapter(queryList) { query ->
                    resolveQuery(query)
                }
            }
    }

    private fun resolveQuery(query: Query) {
        // Simple logic to mark as resolved for now to demonstrate Firestore write
        db.collection("queries").document(query.queryId)
            .update("status", "Resolved")
            .addOnSuccessListener {
                Toast.makeText(this, "Query marked as Resolved", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.remove()
    }
}
