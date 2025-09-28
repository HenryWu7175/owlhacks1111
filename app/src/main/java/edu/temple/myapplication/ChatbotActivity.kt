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
        // Must match the XML file name below
        setContentView(R.layout.activity_result)

        val title = findViewById<TextView>(R.id.textView)
        val input = findViewById<EditText>(R.id.textView2)
        val btnSubmit = findViewById<Button>(R.id.buttonSubmit)     // "Go"
        val btnExamples = findViewById<Button>(R.id.buttonExamples) // "See Examples"

        title.text = "What productivity task would you like help with today?"

        // "See Examples" → go to main picker screen (Bed / Desk / Dishes)
        btnExamples.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // "Go" → route by keywords; if empty or no match, open main picker
        btnSubmit.setOnClickListener {
            val text = input.text?.toString()?.trim().orEmpty()

            if (text.isEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
                return@setOnClickListener
            }

            when (val zone = detectZone(text)) {
                null -> startActivity(Intent(this, MainActivity::class.java)) // no match → manual pick
                else -> startActivity(
                    Intent(this, UploadActivity::class.java).putExtra("zone", zone)
                )
            }
        }
    }

    /**
     * Very forgiving keyword detector:
     * - contains "dish" → Dishes
     * - contains "desk" or "tabl" → Desk
     * - contains "bed" or "bedd" → Bed
     */
    private fun detectZone(raw: String): String? {
        val t = raw.lowercase()

        if ("dish" in t) return "Dishes"
        if ("desk" in t || "tabl" in t) return "Desk"
        if ("bed" in t || "bedd" in t) return "Bed"

        return null
    }
}