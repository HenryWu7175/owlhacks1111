package edu.temple.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class UploadActivity : AppCompatActivity() {

    private lateinit var tvZone: TextView
    private lateinit var tvInfo: TextView
    private lateinit var imgPreview: ImageView
    private var referenceUri: android.net.Uri? = null
    private var afterUri: android.net.Uri? = null

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            imgPreview.setImageURI(uri)
            tvInfo.text = "Image selected!"

            // 🧠 If no reference exists yet, save it
            if (referenceUri == null) {
                referenceUri = uri
                tvInfo.text = "Reference image saved! Upload the 'after' image next."
            }
            // ✅ If reference already exists, treat this as after image
            else {
                afterUri = uri
                compareImages()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        tvZone = findViewById(R.id.tvZone)
        tvInfo = findViewById(R.id.tvInfo)
        imgPreview = findViewById(R.id.imgPreview)

        val zone = intent.getStringExtra("zone") ?: "Unknown"
        tvZone.text = "Upload for: $zone"

        findViewById<Button>(R.id.btnPick).setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun compareImages() {
        // 🧠 Replace this with real AI or vision API comparison later
        // For now, just simulate a random "score"
        val looksRight = (0..1).random() == 1

        if (looksRight) {
            tvInfo.text = "✅ Looks right! You earned 10 points 🎉"
            // 🏆 Add points
            PointsManager.add(this, 10)
        } else {
            tvInfo.text = "❌ That doesn't look right. Try again!"
        }
    }
}