package com.skvoznyak.findart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import com.skvoznyak.findart.databinding.LayoutToolbarBinding
import com.skvoznyak.findart.databinding.LoadingScreenBinding


class LoadingActivity : BaseActivity() {

    private lateinit var loadingBinding: LoadingScreenBinding
    private lateinit var progressBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressBar = loadingBinding.horizontalDottedProgress
        showProgressBar(true)
    }

    override fun addActivity() {
        loadingBinding = LoadingScreenBinding.inflate(layoutInflater)
        setContentView(loadingBinding.root)
    }

    @SuppressLint("ResourceAsColor")
    override fun addToolbar() { //TODO: передача стиля как параметра?
        val toolbarBinding = LayoutToolbarBinding.inflate(layoutInflater)
        addContentView(toolbarBinding.root, ViewGroup.LayoutParams(
            ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        setSupportActionBar(toolbarBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbarBinding.toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_left_grey600_24dp)
    }

    fun showProgressBar(visibility: Boolean) {
        progressBar.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
    }
}