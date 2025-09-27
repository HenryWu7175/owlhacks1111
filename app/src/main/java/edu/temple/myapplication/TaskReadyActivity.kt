package edu.temple.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TaskReadyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_ready)

        val zone = intent.getStringExtra("zone") ?: "Unknown"

        // Update message
        findViewById<TextView>(R.id.tvMsg).text =
            "You have 10 minutes to complete: $zone.\nPlease don’t leave this session."

        // When user clicks Begin → go to TaskInProgressActivity
        findViewById<Button>(R.id.btnBegin).setOnClickListener {
            startActivity(
                Intent(this, TaskInProgressActivity::class.java)
                    .putExtra("zone", zone)
            )
            finish()
        }
    }
}