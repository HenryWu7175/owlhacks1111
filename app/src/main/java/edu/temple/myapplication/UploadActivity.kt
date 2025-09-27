package edu.temple.myapplication  // match your actual package name

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

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            imgPreview.setImageURI(uri)
            tvInfo.text = "Selected image loaded."
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        val zone = intent.getStringExtra("zone") ?: "Unknown"

        tvZone = findViewById(R.id.tvZone)
        tvInfo = findViewById(R.id.tvInfo)
        imgPreview = findViewById(R.id.imgPreview)

        tvZone.text = "Upload for: $zone"

        findViewById<Button>(R.id.btnPick).setOnClickListener {
            pickImage.launch("image/*")
        }
    }
}