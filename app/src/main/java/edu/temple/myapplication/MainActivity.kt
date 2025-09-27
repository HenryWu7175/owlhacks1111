package edu.temple.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

        // Use IDs that actually exist in activity_main.xml.
        // If your XML uses "make_bed", "table_desk", "dishes", use those:
        findViewById<Button>(R.id.make_bed).setOnClickListener { openUpload("Bed") }
        findViewById<Button>(R.id.table_desk).setOnClickListener { openUpload("Desk") }
        findViewById<Button>(R.id.dishes).setOnClickListener { openUpload("Dishes") }
    }
}