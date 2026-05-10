package com.example.smartmoveiiui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmoveiiui.data.model.Query
import com.example.smartmoveiiui.databinding.ActivitySubmitQueryBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class SubmitQueryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubmitQueryBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmitQueryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setupCategoryDropdown()

        binding.queryToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSubmitQuery.setOnClickListener {
            submitQuery()
        }
    }

    private fun setupCategoryDropdown() {
        val categories = arrayOf("Bus Delay", "Route Suggestion", "App Issue", "Lost & Found", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        binding.categoryDropdown.setAdapter(adapter)
    }

    private fun submitQuery() {
        val description = binding.descriptionEdit.text.toString().trim()
        val category = binding.categoryDropdown.text.toString()
        val userId = auth.currentUser?.uid ?: return

        if (description.isEmpty()) {
            binding.descriptionLayout.error = "Please provide details"
            return
        }
        binding.descriptionLayout.error = null

        binding.btnSubmitQuery.isEnabled = false
        val queryId = UUID.randomUUID().toString()
        val query = Query(
            queryId = queryId,
            commuterId = userId,
            description = description,
            category = category,
            status = "Pending",
            time = Timestamp.now(),
        )

        db.collection("queries").document(queryId)
            .set(query)
            .addOnSuccessListener {
                Toast.makeText(this, "Query submitted successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to submit: ${e.message}", Toast.LENGTH_SHORT).show()
                binding.btnSubmitQuery.isEnabled = true
            }
    }
}
