package edu.temple.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// RetrofitClient.kt

object RetrofitClient {
    // IMPORTANT: Use 10.0.2.2 to access your local machine's localhost from the Android Emulator.
    // If you use a physical device, use your local machine's IP address (e.g., http://192.168.1.5:8000/)
    private const val BASE_URL = "http://10.0.2.2:8000/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}