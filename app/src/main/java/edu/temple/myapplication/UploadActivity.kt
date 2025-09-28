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

    // Pick an image from gallery
    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri == null) {
            toast("No image selected.")
            return@registerForActivityResult
        }

        // Preview
        try {
            imgPreview.setImageURI(uri)
        } catch (e: Exception) {
            Log.e("UploadActivity", "Failed to preview image", e)
            toast("Failed to preview image.")
            return@registerForActivityResult
        }

        if (!expectAfter) {
            // First run: save as CLEAN reference and move to timer screen
            try {
                ZoneStorage.saveReference(this, zone, uri)
                referenceUri = uri
                Log.d("UploadActivity", "Reference saved for $zone: $uri")
            } catch (e: Exception) {
                Log.e("UploadActivity", "Saving reference failed", e)
                toast("Could not save reference image.")
                return@registerForActivityResult
            }

            tvInfo.text = "Reference saved! Starting timer…"

            // Go to the 10-minute task screen
            startActivity(
                Intent(this, TaskReadyActivity::class.java)
                    .putExtra("zone", zone)
            )
            finish()
        } else {
            // After timer: this is the AFTER photo. You can call API or mock here.
            tvInfo.text = "After photo selected! Analyzing…"
            // TODO: call your compare flow here (or mock)
            // e.g., startActivity(Intent(this, UploadNewClean::class.java).putExtra("zone", zone))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        // Read intent extras SAFELY before using them
        zone = intent.getStringExtra("zone") ?: "Unknown"
        expectAfter = intent.getBooleanExtra("expectAfter", false)

        // Bind views (will crash only if the layout doesn't have these IDs)
        tvZone = findViewById(R.id.tvZone)
        tvInfo = findViewById(R.id.tvInfo)
        imgPreview = findViewById(R.id.imgPreview)

        // Set title text based on stage
        tvZone.text = if (expectAfter) {
            "Upload AFTER photo for $zone"
        } else {
            "Upload CLEAN reference for $zone"
        }

        // Button
        findViewById<Button>(R.id.btnPick).setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun toast(s: String) =
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}