package edu.temple.myapplication


import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


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
        val imageFile = FileUtils.getFileFromUri(this, uri)
        startComparison(imageFile, zone)

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
    private fun startComparison(imageFile: File, zone: String) {
        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
        val taskIdBody = zone.toRequestBody("text/plain".toMediaTypeOrNull())

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.compareClean(imagePart, taskIdBody)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            handleSuccess(result)
                        } else {
                            Toast.makeText(this@UploadNewClean, "Empty response body.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@UploadNewClean, "API Error: ${response.code()}", Toast.LENGTH_LONG).show()
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
        val feedbackView = findViewById<TextView>(R.id.tvInfo)
        val scoreText = String.format("%.2f", result.score)

        if (result.score >= 0.92) {
            PointsManager.add(this, 10)
            feedbackView.text = "✅ Looks good! Score: $scoreText\n${result.feedback}"
        } else {
            feedbackView.text = "❌ Not quite right. Score: $scoreText\n${result.feedback}"
        }
    }

}
object FileUtils {
    fun getFileFromUri(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Unable to open URI: $uri")

        val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
        tempFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        return tempFile
    }
}
