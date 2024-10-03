package com.evirgenoguz.myapplication

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://api.yourservice.com/"

    // Token Manager (Bu örnekte basit bir token yöneticisi, ihtiyaca göre geliştirilebilir)
    private val tokenManager = TokenManager()

    // OkHttpClient ile interceptor ekliyoruz
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(tokenManager))  // Token yenileme ve yetkilendirme
        .addInterceptor(HttpLoggingInterceptor().apply { // İstek ve yanıt loglama
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    // Retrofit nesnesi oluşturma
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)  // OkHttpClient'ı ekliyoruz
        .addConverterFactory(GsonConverterFactory.create())  // JSON dönüşümü
        .build()

    // ApiService oluşturma (tüm API çağrıları buradan yapılacak)
    val apiService: AuthService = retrofit.create(AuthService::class.java)
}