package com.example.smartmoveiiui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmoveiiui.model.AppRole
import com.example.smartmoveiiui.util.DatabaseSeeder
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.max

class SplashActivity : AppCompatActivity() {

    private val splashHoldMs = 2500L
    private var progressAnimator: ObjectAnimator? = null
    private var busAnimator: ObjectAnimator? = null
    private val navigateRunnable = Runnable { performSystemCheckAndNavigate() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val progressFill = findViewById<View>(R.id.splash_progress_fill)
        val miniBus = findViewById<View>(R.id.splash_mini_bus)
        val busLane = findViewById<View>(R.id.splash_bus_lane)
        val splashContent = findViewById<View>(R.id.splash_content)

        // Fade and Scale animation for splash content
        splashContent.alpha = 0f
        splashContent.scaleX = 0.8f
        splashContent.scaleY = 0.8f
        val fadeScaleAnimator = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(splashContent, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(splashContent, View.SCALE_X, 0.8f, 1f),
                ObjectAnimator.ofFloat(splashContent, View.SCALE_Y, 0.8f, 1f)
            )
            duration = 1000
            interpolator = DecelerateInterpolator()
        }
        fadeScaleAnimator.start()

        progressFill.post {
            progressFill.pivotX = 0f
            progressFill.pivotY = progressFill.height * 0.5f
            progressFill.scaleX = 0f

            progressAnimator = ObjectAnimator.ofFloat(progressFill, View.SCALE_X, 1f).apply {
                duration = splashHoldMs
                interpolator = DecelerateInterpolator(1.85f)
            }
            progressAnimator?.start()
        }

        busLane.post {
            val margin = 16
            val travel =
                max(miniBus.width + margin, busLane.width - margin) - miniBus.width / 2
            miniBus.translationX = -(miniBus.width * 0.85f)

            busAnimator =
                ObjectAnimator.ofFloat(miniBus, View.TRANSLATION_X, miniBus.translationX, travel.toFloat()).apply {
                    duration = splashHoldMs
                    interpolator = LinearInterpolator()
                }
            busAnimator?.start()
        }

        // Initialize predefined users and sample data for testing
        DatabaseSeeder.seedAll(this)

        window.decorView.postDelayed(navigateRunnable, splashHoldMs)
    }

    private fun performSystemCheckAndNavigate() {
        if (isFinishing) return

        // Perform "system health check" (placeholder)
        val isSystemHealthy = true // Imagine checking connectivity or Firebase config here
        
        if (!isSystemHealthy) {
            Toast.makeText(this, "System connectivity check failed. Please check your internet.", Toast.LENGTH_LONG).show()
            // Graceful error feedback
            return
        }

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val email = currentUser.email ?: ""
            if (email.endsWith("@iiu.edu.pk") || email == "maryam.bsse4526@gmail.com") {
                // Already logged in with authorized domain or admin email
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(AppRole.EXTRA_NAME, AppRole.COMMUTER.name)
                }
                startActivity(intent)
            } else {
                startActivity(Intent(this, OnboardingActivity::class.java))
            }
        } else {
            startActivity(Intent(this, OnboardingActivity::class.java))
        }

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun onDestroy() {
        progressAnimator?.cancel()
        busAnimator?.cancel()
        window.decorView.removeCallbacks(navigateRunnable)
        super.onDestroy()
    }
}
