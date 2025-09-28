package edu.temple.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ChatbotActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure this matches your XML file name (activity_result.xml or activity_chatbot.xml)
        setContentView(R.layout.activity_result)

        val textView = findViewById<TextView>(R.id.textView)
        val input = findViewById<EditText>(R.id.textView2)
        val button = findViewById<Button>(R.id.button2)

        textView.text = "What productivity task would you like help with today?"

        button.setOnClickListener {
            val userText = input.text?.toString()?.trim().orEmpty()

            if (userText.isEmpty()) {
                Toast.makeText(this, "Please type something like 'make bed' or 'clean dishes'.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Detect which task the user means
            val zone = detectZone(userText)

            if (zone == null) {
                Toast.makeText(
                    this,
                    "Hmm, I didn’t catch that. Try mentioning 'bed', 'desk', or 'dishes'.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // ✅ Launch UploadActivity with the right zone
                val intent = Intent(this, UploadActivity::class.java)
                intent.putExtra("zone", zone)
                startActivity(intent)
            }
        }
    }

    // 🔍 Helper function to detect which zone the user is talking about
    private fun detectZone(text: String): String? {
        val t = text.lowercase()

        if (listOf("bed", "bedding", "make bed", "blanket", "pillow").any { t.contains(it) }) {
            return "Bed"
        }

        if (listOf("desk", "table", "workspace", "clean desk", "organize table").any { t.contains(it) }) {
            return "Desk"
        }

        if (listOf("dishes", "dish", "sink", "plate", "wash", "cups").any { t.contains(it) }) {
            return "Dishes"
        }

        return null
    }
}