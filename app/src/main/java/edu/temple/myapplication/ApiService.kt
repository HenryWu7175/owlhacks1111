package edu.temple.myapplication


// ApiService.kt
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    /**
     * POST /compare-clean/ with multipart data:
     * - 'image': The user's uploaded image file.
     * - 'task_id': The string identifier (e.g., "Bed", "Dishes").
     */
    @Multipart
    @POST("compare-clean/")
    suspend fun compareClean(
        @Part image: MultipartBody.Part,
        @Part("task_id") taskId: RequestBody
    ): Response<AnalysisResult>
}
