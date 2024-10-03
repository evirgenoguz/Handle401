package com.evirgenoguz.myapplication

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {

    @GET("user/profile")
    suspend fun getUserProfile(): Response<UserProfile>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body refreshToken: RefreshTokenRequest): Response<TokenResponse>
}