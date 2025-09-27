package edu.temple.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private fun openUpload(zone: String) {
        val i = Intent(this, UploadActivity::class.java)
        i.putExtra("zone", zone)
        startActivity(i)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.make_bed).setOnClickListener { openUpload("Bed") }
        findViewById<Button>(R.id.table_desk).setOnClickListener { openUpload("Desk") }
        findViewById<Button>(R.id.dishes).setOnClickListener { openUpload("Dishes") }
    }

    override fun onResume() {
        super.onResume()
        // Show latest points (uses PointsManager from earlier)
        findViewById<TextView>(R.id.tvPoints)?.text = "Points: ${PointsManager.get(this)}"
    }
}