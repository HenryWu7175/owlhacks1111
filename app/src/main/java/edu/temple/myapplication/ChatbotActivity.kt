package edu.temple.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ChatbotActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result) // ✅ Make sure XML name matches

        val title = findViewById<TextView>(R.id.textView)
        val input = findViewById<EditText>(R.id.textView2)
        val btnSubmit = findViewById<Button>(R.id.buttonSubmit)   // Go button
        val btnExamples = findViewById<Button>(R.id.buttonExamples) // See examples

        title.text = "What productivity task would you like help with today?"

        // “See Examples” → goes to main picker
        btnExamples.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // “Go” → keyword detection or fallback
        btnSubmit.setOnClickListener {
            val text = input.text?.toString()?.trim().orEmpty()

            // If empty, send to main menu
            if (text.isEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
                return@setOnClickListener
            }

            // Detect zone
            when (val zone = detectZone(text)) {
                null -> startActivity(Intent(this, MainActivity::class.java))
                else -> startActivity(Intent(this, UploadActivity::class.java).putExtra("zone", zone))
            }
        }
    }

    // ✅ Updated detector with “kitchen” → Dishes
    private fun detectZone(raw: String): String? {
        val t = raw.lowercase()

        // Dishes zone
        if ("dish" in t || "kitchen" in t || "sink" in t) return "Dishes"

        // Desk / Table zone
        if ("desk" in t || "tabl" in t) return "Desk"

        // Bed zone
        if ("bed" in t || "bedd" in t) return "Bed"

        return null
    }
}