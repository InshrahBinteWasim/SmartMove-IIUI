package com.example.smartmoveiiui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmoveiiui.databinding.ActivityAuthBinding
import com.example.smartmoveiiui.model.AppRole
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.authLoginButton.setOnClickListener {
            performLogin()
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
        } else if (!email.endsWith("@iiu.edu.pk") && !email.contains("admin")) {
            // Flexible check: allow IIUI domain or specific admin emails
            if (email != "maryam.bsse4526@gmail.com") {
                binding.authEmailLayout.error = getString(R.string.auth_email_error)
                hasError = true
            } else {
                binding.authEmailLayout.error = null
            }
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
                binding.authProgress.visibility = View.GONE
                binding.authLoginButton.isEnabled = true
                if (task.isSuccessful) {
                    navigateToMain()
                } else {
                    Toast.makeText(baseContext, getString(R.string.auth_error_wrong_credentials),
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToMain() {
        // Default to COMMUTER role as per user preference for general login
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(AppRole.EXTRA_NAME, AppRole.COMMUTER.name)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
        finish()
    }
}
