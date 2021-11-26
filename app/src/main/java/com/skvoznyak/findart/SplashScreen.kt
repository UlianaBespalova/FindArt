package com.skvoznyak.findart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.skvoznyak.findart.databinding.ActivityMainBinding
import com.skvoznyak.findart.databinding.LayoutToolbarBinding

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this@SplashScreen, MainScreenActivity::class.java)
        startActivity(intent)
        finish()
    }
}