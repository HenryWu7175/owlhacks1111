package edu.temple.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
class UploadNewClean : AppCompatActivity() {
    private lateinit var tvZone: TextView
    private lateinit var tvInfo: TextView
    private lateinit var imgPreview: ImageView
    private var referenceUri: Uri? = null
    private var expectAfter: Boolean = false
    private lateinit var zone: String
    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri ?: return@registerForActivityResult
        imgPreview.setImageURI(uri)
            val refUri = ZoneStorage.getReference(this, zone)
            if (refUri != null) {
                val score = Scoring.score(
                    Scoring.loadBitmap(this, refUri),
                    Scoring.loadBitmap(this, uri)
                )
                if (score >= .92) {
                    tvInfo.text = "✅ Looks good! +10 points!"
                    PointsManager.add(this, 10)
                } else {
                    tvInfo.text = "❌ Not quite right, try again!"
                }
            } else {
                tvInfo.text = "⚠️ No reference found!"
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_new_clean)
        zone = intent.getStringExtra("zone") ?: "Unknown"
        expectAfter = intent.getBooleanExtra("expectAfter", false)

        tvZone = findViewById(R.id.tvZone)
        tvInfo = findViewById(R.id.tvInfo)
        imgPreview = findViewById(R.id.imgPreview)

        tvZone.text = "Upload AFTER photo for $zone"

        findViewById<Button>(R.id.btnPick).setOnClickListener {
            pickImage.launch("image/*")

        }
    }
}