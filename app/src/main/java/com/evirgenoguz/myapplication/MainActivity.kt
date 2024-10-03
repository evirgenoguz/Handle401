package com.evirgenoguz.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            fetchUserProfile()
        }
    }



    private suspend fun fetchUserProfile() {
        val response = ApiClient.apiService.getUserProfile()
        if (response.isSuccessful) {
            val userProfile = response.body()
            // Kullanıcı profili işlenir
            //back-end servisi bulamadigim icin burada bir istek vs atamayacagiz
        } else {
            // Hata yönetimi yapılır
        }
    }
}