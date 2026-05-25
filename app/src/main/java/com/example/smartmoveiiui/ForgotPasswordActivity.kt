package com.example.smartmoveiiui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.smartmoveiiui.ui.auth.ForgotPasswordFlowScreen
import com.example.smartmoveiiui.ui.theme.SmartMoveIIUITheme
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setContent {
            SmartMoveIIUITheme {
                ForgotPasswordFlowScreen(
                    onBack = { onBackPressedDispatcher.onBackPressed() },
                    onSendResetEmail = { email, onDone ->
                        auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Demo OTP shown. Firebase email failed: ${task.exception?.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                onDone()
                            }
                    },
                    onFinished = {
                        Toast.makeText(this, "Password recovery demo completed", Toast.LENGTH_LONG).show()
                        finish()
                    }
                )
            }
        }
    }
}
