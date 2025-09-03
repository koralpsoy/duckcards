package com.example.duckcards

import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val duck = findViewById<ImageView>(R.id.duckLogo)
        duck.scaleX = 0.2f
        duck.scaleY = 0.2f
        duck.alpha = 0f

        duck.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(900)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .start()
    }
}
