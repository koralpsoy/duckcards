package com.example.duckcards

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val g = intent.getIntExtra("green", 0)
        val y = intent.getIntExtra("yellow", 0)
        val r = intent.getIntExtra("red", 0)

        findViewById<TextView>(R.id.resultGreen).text = "Korrekt (grün): $g"
        findViewById<TextView>(R.id.resultYellow).text = "Unsicher (gelb): $y"
        findViewById<TextView>(R.id.resultRed).text = "Falsch (rot): $r"

        findViewById<Button>(R.id.resultClose).setOnClickListener { finish() }
    }
}
