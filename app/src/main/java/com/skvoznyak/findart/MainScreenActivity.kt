package com.skvoznyak.findart

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import com.skvoznyak.findart.databinding.ActivityMainBinding
import com.skvoznyak.findart.databinding.LoadingScreenBinding
import com.skvoznyak.findart.databinding.MainScreenBinding


class MainScreenActivity : GetImage() {

    lateinit var mainScreenBinding : MainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainScreenBinding.buttonBookmarks.setOnClickListener {
            val intent = Intent(this@MainScreenActivity, PicturesListActivity::class.java)
            startActivity(intent)
        }
        mainScreenBinding.buttonChooseImage.setOnClickListener {
//            selectImage()
            val intent = Intent(this@MainScreenActivity, UploadImageActivity::class.java)
            startActivity(intent)
        }
    }

    override fun addActivity() {
        mainScreenBinding = MainScreenBinding.inflate(layoutInflater)
        setContentView(mainScreenBinding.root)
    }

    override fun addToolbar() { }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val intent = Intent(this@MainScreenActivity, UploadImageActivity::class.java)
            intent.putExtra("requestCode", requestCode)
            intent.putExtra("data", data)
            intent.putExtra("currentPhotoPath", currentPhotoPath)
            startActivity(intent)
        }
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                window.navigationBarColor = getColor(R.color.beige_normal)
                it.hide(WindowInsets.Type.systemBars())
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
    }
}