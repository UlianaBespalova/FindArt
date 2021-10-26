package com.skvoznyak.findart

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.skvoznyak.findart.databinding.ActivityMainBinding
import com.skvoznyak.findart.databinding.LayoutToolbarBinding


open class BaseActivity : AppCompatActivity() {

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
    }

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
            else -> super.onOptionsItemSelected(item)
        }
    }



//    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        super.onWindowFocusChanged(hasFocus)
//        if (hasFocus) hideSystemUI()
//    }


}