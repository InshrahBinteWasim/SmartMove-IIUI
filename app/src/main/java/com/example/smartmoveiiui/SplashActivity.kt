package com.example.smartmoveiiui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.max

class SplashActivity : AppCompatActivity() {

    private val splashHoldMs = 2500L
    private var progressAnimator: ObjectAnimator? = null
    private var busAnimator: ObjectAnimator? = null
    private val navigateRunnable = Runnable { navigateForward() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val progressFill = findViewById<View>(R.id.splash_progress_fill)
        val miniBus = findViewById<View>(R.id.splash_mini_bus)
        val busLane = findViewById<View>(R.id.splash_bus_lane)

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

        window.decorView.postDelayed(navigateRunnable, splashHoldMs)
    }

    private fun navigateForward() {
        if (isFinishing) return
        startActivity(Intent(this, OnboardingActivity::class.java))
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
