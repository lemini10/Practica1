package com.example.practica1lrbl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.concurrent.thread

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        thread {
            Thread.sleep(3000)
            val intentNextScreen = Intent(this, MainActivity::class.java)
            startActivity(intentNextScreen)
            finish()
        }

    }
}