package com.evirgenoguz.myapplication

class TokenManager {
    private var accessToken: String? = null
    private var refreshToken: String? = null

    fun getAccessToken(): String? {
        return accessToken
    }

    fun getRefreshToken(): String? {
        return refreshToken
    }

    suspend fun refreshAccessToken(refreshToken: String?): TokenResponse? {
        return try {
            val response = ApiClient.apiService.refreshToken(RefreshTokenRequest(refreshToken))
            if (response.isSuccessful) {
                val newTokens = response.body()
                accessToken = newTokens?.accessToken
                this.refreshToken = newTokens?.refreshToken
                newTokens
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun clearTokens() {
        accessToken = null
        refreshToken = null
    }
}

data class TokenResponse(val accessToken: String, val refreshToken: String)
data class RefreshTokenRequest(val refreshToken: String?)
