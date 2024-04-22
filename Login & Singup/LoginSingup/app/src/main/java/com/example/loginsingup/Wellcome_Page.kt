package com.example.loginsingup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class Wellcome_Page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wellcome_page)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@Wellcome_Page,LoginPage::class.java))
            finish()
        },2000)
    }
}