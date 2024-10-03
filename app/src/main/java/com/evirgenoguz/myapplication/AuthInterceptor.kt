package com.evirgenoguz.myapplication

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // Mevcut Access Token'ı ekliyoruz
        val accessToken = tokenManager.getAccessToken()
        request = request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(request)

        // Eğer 401 Unauthorized dönerse, refresh token kullanarak yeni token al
        if (response.code == 401) {
            synchronized(this) {
                val newAccessToken = tokenManager.getAccessToken()

                // Eğer token hala aynıysa refresh token ile yeni token al
                if (accessToken == newAccessToken) {
                    val refreshToken = tokenManager.getRefreshToken()

                    val newTokens = runBlocking {
                        tokenManager.refreshAccessToken(refreshToken)
                    }

                    if (newTokens != null) {
                        // Yeni Access Token'ı header'a ekleyerek isteği yeniden gönderiyoruz
                        request = request.newBuilder()
                            .header("Authorization", "Bearer ${newTokens.accessToken}")
                            .build()

                        response.close()  // Eski yanıtı kapat
                        return chain.proceed(request)  // Yeni istekle devam et
                    } else {
                        // Refresh Token da geçersizse, kullanıcının çıkışını yap
                        tokenManager.clearTokens()
                    }
                }
            }
        }

        return response
    }
}