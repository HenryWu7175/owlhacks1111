package edu.temple.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class UploadActivity : AppCompatActivity() {

    private lateinit var tvZone: TextView
    private lateinit var tvInfo: TextView
    private lateinit var imgPreview: ImageView
    private var referenceUri: Uri? = null
    private var expectAfter: Boolean = false
    private lateinit var zone: String

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri ?: run {
            Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show()
            return@registerForActivityResult
        }
        imgPreview.setImageURI(uri)
            // ✅ This is the clean reference upload
            ZoneStorage.saveReference(this, zone, uri)
        Log.d("UploadActivity", "Reference saved for $zone: $uri")

        tvInfo.text = "Reference saved! Starting timer..."

            // 🚀 Go to the 10-minute task screen
            startActivity(
                Intent(this, TaskReadyActivity::class.java)
                    .putExtra("zone", zone)
            )
            finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        zone = intent.getStringExtra("zone") ?: "Unknown"

        tvZone = findViewById(R.id.tvZone)
        tvInfo = findViewById(R.id.tvInfo)
        imgPreview = findViewById(R.id.imgPreview)

        tvZone.text = if (expectAfter) "Upload AFTER photo for $zone"
        else "Upload CLEAN reference for $zone"

        findViewById<Button>(R.id.btnPick).setOnClickListener {
            pickImage.launch("image/*")
        }
    }
}