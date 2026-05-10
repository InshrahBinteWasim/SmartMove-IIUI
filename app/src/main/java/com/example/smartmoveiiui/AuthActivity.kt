package com.example.smartmoveiiui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.smartmoveiiui.data.model.User
import com.example.smartmoveiiui.databinding.ActivityAuthBinding
import com.example.smartmoveiiui.model.AppRole
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.authLoginButton.setOnClickListener {
            performLogin()
        }

        binding.btnGoogleSignin.setOnClickListener {
            signInWithGoogle()
        }

        binding.authForgotPasswordText.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun performLogin() {
        val email = binding.authEmailEdit.text.toString().trim()
        val password = binding.authPasswordEdit.text.toString().trim()

        var hasError = false

        if (email.isEmpty()) {
            binding.authEmailLayout.error = getString(R.string.auth_error_empty)
            hasError = true
        } else if (!email.endsWith("@iiu.edu.pk") && email != "maryam.bsse4526@gmail.com") {
            binding.authEmailLayout.error = getString(R.string.auth_email_error)
            hasError = true
        } else {
            binding.authEmailLayout.error = null
        }

        if (password.isEmpty()) {
            binding.authPasswordLayout.error = getString(R.string.auth_error_empty)
            hasError = true
        } else if (password.length < 8) {
            binding.authPasswordLayout.error = getString(R.string.auth_password_hint)
            hasError = true
        } else {
            binding.authPasswordLayout.error = null
        }

        if (hasError) return

        binding.authProgress.visibility = View.VISIBLE
        binding.authLoginButton.isEnabled = false

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    fetchUserRoleAndNavigate(auth.currentUser?.uid ?: "")
                } else {
                    binding.authProgress.visibility = View.GONE
                    binding.authLoginButton.isEnabled = true
                    Toast.makeText(baseContext, getString(R.string.auth_error_wrong_credentials),
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInWithGoogle() {
        val credentialManager = CredentialManager.create(this)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(this@AuthActivity, request)
                handleGoogleSignInResult(result)
            } catch (e: GetCredentialException) {
                Log.e("Auth", "Google sign in failed", e)
                Toast.makeText(this@AuthActivity, "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleGoogleSignInResult(result: GetCredentialResponse) {
        val credential = result.credential
        if (credential is GoogleIdTokenCredential) {
            val googleIdToken = credential.idToken
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            
            binding.authProgress.visibility = View.VISIBLE
            auth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        firebaseUser?.let { 
                            checkAndCreateGoogleUser(it.uid, it.email ?: "", it.displayName ?: "User")
                        }
                    } else {
                        binding.authProgress.visibility = View.GONE
                        Toast.makeText(this, "Firebase auth failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun checkAndCreateGoogleUser(uid: String, email: String, name: String) {
        // --- CREDIT SAVING STRATEGY: Targeted Fetch ---
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    navigateToMain(document.getString("role") ?: AppRole.COMMUTER.name)
                } else {
                    val newUser = User(uid, name, email, AppRole.COMMUTER.name)
                    db.collection("users").document(uid).set(newUser)
                        .addOnSuccessListener { navigateToMain(AppRole.COMMUTER.name) }
                }
            }
    }

    private fun fetchUserRoleAndNavigate(uid: String) {
        // --- CREDIT SAVING STRATEGY: Targeted Fetch ---
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                binding.authProgress.visibility = View.GONE
                val role = document.getString("role") ?: AppRole.COMMUTER.name
                navigateToMain(role)
            }
            .addOnFailureListener {
                binding.authProgress.visibility = View.GONE
                navigateToMain(AppRole.COMMUTER.name)
            }
    }

    private fun navigateToMain(roleName: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(AppRole.EXTRA_NAME, roleName)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
        finish()
    }
}
