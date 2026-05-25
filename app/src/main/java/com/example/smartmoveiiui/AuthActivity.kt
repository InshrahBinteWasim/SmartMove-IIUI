package com.example.smartmoveiiui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.smartmoveiiui.data.model.User
import com.example.smartmoveiiui.model.AppRole
import com.example.smartmoveiiui.ui.auth.SmartMoveLoginScreen
import com.example.smartmoveiiui.ui.theme.SmartMoveIIUITheme
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AuthActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        setContent {
            SmartMoveIIUITheme {
                SmartMoveLoginScreen(
                    onLogin = ::performLogin,
                    onGoogleSignIn = ::signInWithGoogle,
                    onForgotPassword = {
                        startActivity(Intent(this, ForgotPasswordActivity::class.java))
                    },
                    onDemoSuccess = {
                        navigateToMain(AppRole.COMMUTER.name)
                    }
                )
            }
        }
    }

    private fun performLogin(
        email: String,
        password: String,
        onLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit,
        onError: (String, Boolean) -> Unit
    ) {
        onLoading(true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    fetchUserRoleAndNavigate(auth.currentUser?.uid ?: "", onSuccess, onError)
                } else {
                    onLoading(false)
                    onError(getString(R.string.auth_error_wrong_credentials), true)
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
            
            auth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        firebaseUser?.let { 
                            checkAndCreateGoogleUser(it.uid, it.email ?: "", it.displayName ?: "User")
                        }
                    } else {
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

    private fun fetchUserRoleAndNavigate(
        uid: String,
        onSuccess: () -> Unit,
        onError: (String, Boolean) -> Unit
    ) {
        // --- CREDIT SAVING STRATEGY: Targeted Fetch ---
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                val role = document.getString("role") ?: AppRole.COMMUTER.name
                onSuccess()
                navigateToMain(role)
            }
            .addOnFailureListener {
                onError("Profile lookup failed. Opening commuter workspace.", false)
                onSuccess()
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
