package edu.temple.myapplication

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Toggle later if you hook the real API back up
private const val USE_FAKE = true

class UploadNewClean : AppCompatActivity() {

    private lateinit var tvZone: TextView
    private lateinit var tvInfo: TextView
    private lateinit var imgPreview: ImageView
    private lateinit var zone: String

    // Pick AFTER photo
    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) {
            Toast.makeText(this, "No image selected.", Toast.LENGTH_SHORT).show()
            return@registerForActivityResult
        }

        // Show preview
        imgPreview.setImageURI(uri)

        // ---- MOCK or REAL ----
        if (USE_FAKE) {
            // Fake “analysis” – no network
            fakeComparison()
        } else {
            // If you later want the real API, convert uri -> File and call startComparison(...)
            // val imageFile = FileUtils.getFileFromUri(this, uri)
            // startComparison(imageFile, zone)
            fakeComparison() // keep fake as fallback
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_new_clean)

        zone = intent.getStringExtra("zone") ?: "Unknown"

        tvZone = findViewById(R.id.tvZone)
        tvInfo = findViewById(R.id.tvInfo)
        imgPreview = findViewById(R.id.imgPreview)

        tvZone.text = "Upload AFTER photo for $zone"
        tvInfo.text = "Pick an AFTER photo to compare with your clean reference."

        findViewById<Button>(R.id.btnPick).setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    // -------------------------
    // FAKE ANALYSIS (no network)
    // -------------------------
    private fun fakeComparison() {
        tvInfo.text = "Analyzing…"

        lifecycleScope.launch {
            delay(1200) // pretend to upload/analyze

            // Random “score” 0.70–0.98 for demo
            val score = (70..98).random() / 100.0
            val msg = if (score >= 0.92) {
                PointsManager.add(this@UploadNewClean, 10)
                "✅ Looks good! Score: $score\n+10 points!"
            } else {
                "❌ Not quite right. Score: $score\nTry tidying a bit more."
            }

            tvInfo.text = msg
            Toast.makeText(this@UploadNewClean, msg, Toast.LENGTH_LONG).show()
        }
    }
}
// -------------------------
// REAL ANALYSIS (optional)
// Keep for later if you wire the API back.
// -------------------------
/*
private fun startComparison(imageFile: File, zone: String) {
    val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
    val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
    val taskIdBody = zone.toRequestBody("text/plain".toMediaTypeOrNull())

    lifecycleScope.launch(Dispatchers.IO) {
        try {
            val response = RetrofitClient.instance.compareClean(imagePart, taskIdBody)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let { handleSuccess(it) }
                        ?: run { Toast.makeText(this@UploadNewClean, "Empty response.", Toast.LENGTH_SHORT).show() }
                } else {
                    Toast.makeText(this@UploadNewClean, "API error ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@UploadNewClean, "Network failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}

private fun handleSuccess(result: AnalysisResult) {
    val scoreText = String.format("%.2f", result.score)
    if (result.score >= 0.92) {
        PointsManager.add(this, 10)
        tvInfo.text = "✅ Looks good! Score: $scoreText\n${result.feedback}"
    } else {
        tvInfo.text = "❌ Not quite right. Score: $scoreText\n${result.feedback}"
    }
}

}
 */