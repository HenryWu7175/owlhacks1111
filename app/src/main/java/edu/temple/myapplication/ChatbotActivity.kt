package edu.temple.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Button
import edu.temple.myapplication.R

class ChatbotActivity: AppCompatActivity() {
    private fun openmain() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        var textView = findViewById<TextView>(R.id.textView)
        var textView2 = findViewById<TextView>(R.id.textView2)
        var button2 = findViewById<Button>(R.id.button2)
        textView.text = "What productivity task would you like help with today?"
        button2.setOnClickListener {
            openmain()
        }

    }
}