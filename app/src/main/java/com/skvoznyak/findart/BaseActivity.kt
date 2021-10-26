package com.skvoznyak.findart

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import com.skvoznyak.findart.databinding.ActivityMainBinding
import com.skvoznyak.findart.databinding.LayoutToolbarBinding
import android.R.attr.bottom

import android.R.attr.right

import android.R.attr.top

import android.R.attr.left
import android.app.ActionBar
import android.util.Log
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout


open class BaseActivity : AppCompatActivity() {

    private lateinit var mainBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addActivity()
        addToolbar()
    }


    protected open fun addActivity() {
        val mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
    }

    protected open fun addToolbar() {
        val toolbarBinding = LayoutToolbarBinding.inflate(layoutInflater)
        addContentView(toolbarBinding.root, ViewGroup.LayoutParams(
            ViewGroup
            .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        setSupportActionBar(toolbarBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)


//        val params = toolbarBinding.toolbar.layoutParams as ViewGroup.MarginLayoutParams
//        params.setMargins(0, getStatusBarHeight(), 0, 0)
//        toolbarBinding.toolbar.layoutParams = params
    }

//    private fun getStatusBarHeight() : Int {
//        var statusBarHeight = 0
//        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
//        if (resourceId > 0) {
//            statusBarHeight = resources.getDimensionPixelSize(resourceId)
//        }
//        return statusBarHeight
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                this.finish()
                true
            }
            R.id.action_bookmarks -> {
                val intent = Intent(this@BaseActivity, PicturesListActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



//    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        super.onWindowFocusChanged(hasFocus)
//        if (hasFocus) hideSystemUI()
//    }


}