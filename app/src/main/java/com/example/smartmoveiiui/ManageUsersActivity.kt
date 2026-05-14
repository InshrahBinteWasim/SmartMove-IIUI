package com.example.smartmoveiiui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartmoveiiui.data.model.User
import com.example.smartmoveiiui.databinding.ActivityManageUsersBinding
import com.example.smartmoveiiui.ui.adapter.UserAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ManageUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageUsersBinding
    private lateinit var db: FirebaseFirestore
    private var listener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.usersToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.rvUsers.layoutManager = LinearLayoutManager(this)

        startListeningForUsers()
    }

    private fun startListeningForUsers() {
        // --- CREDIT SAVING STRATEGY: Snapshot Listener + Limited Fetch ---
        binding.usersProgress.visibility = View.VISIBLE
        listener = db.collection("users")
            .limit(50) // Don't fetch thousands of users at once
            .addSnapshotListener { snapshot, error ->
                binding.usersProgress.visibility = View.GONE
                if (error != null || snapshot == null) return@addSnapshotListener

                val userList = snapshot.toObjects(User::class.java)
                binding.rvUsers.adapter = UserAdapter(userList)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.remove()
    }
}
