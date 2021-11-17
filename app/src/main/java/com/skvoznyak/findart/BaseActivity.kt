package com.skvoznyak.findart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.skvoznyak.findart.databinding.ActivityMainBinding
import com.skvoznyak.findart.databinding.LayoutToolbarBinding


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
        addContentView(
            toolbarBinding.root, ViewGroup.LayoutParams(
                ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
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
            R.id.action_bookmarks -> {
                val intent = Intent(this@BaseActivity, PicturesListActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    protected fun showNotFound() {
        val imageView = findViewById<View>(R.id.not_found_picture)
        imageView?.visibility = View.VISIBLE
    }
}