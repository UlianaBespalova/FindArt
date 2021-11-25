package com.skvoznyak.findart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.FragmentTransaction
import com.skvoznyak.findart.model.PictureRepository
import com.skvoznyak.findart.model.Picture
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import com.skvoznyak.findart.utils.isOnline
import com.skvoznyak.findart.utils.noConnection
import com.google.gson.Gson

import com.google.gson.GsonBuilder





class SimilarPicturesListActivity : PicturesListActivity() {

    private val disposables = CompositeDisposable()
    private var pictures : List<Picture>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vector = intent.extras?.get("vector") as FloatArray
        if (vector.isNotEmpty()) {
            if (isOnline(this)) {
                if (savedInstanceState == null) {
                    showLoader()
                }
                val similarRequestDisposable = PictureRepository.getSimilarPictures(vector)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ images ->
                        if (images.isNotEmpty()) {
                            makePictureList(images)
                        }
                        else { noResults() }
                    }, { err -> Log.d("ivan", "request error: $err")
                        noResults()
                    })
                disposables.add(similarRequestDisposable)
            }
            else {
                noConnection(applicationContext)
                finish()
            }
        }
        else {
            noResults()
        }
    }

    private fun noResults() {
        hideLoader()

        val resultList:RecyclerView = findViewById(R.id.resultList)
        val headerAdapter = HeaderAdapter(resources.getString(R.string.no_results))
        resultList.adapter = headerAdapter
        resultList.layoutManager = LinearLayoutManager(this)
        showNotFound()
    }

    private fun makePictureList(images : List<Picture>) {
//        hideLoader()
        Log.d("ivan", "Server: Success!")
        pictures = images
        setResultList(images)
    }

    private fun setResultList(images : List<Picture>) {
        val resultList:RecyclerView = findViewById(R.id.resultList)
        resultList.isNestedScrollingEnabled = true

        val headerAdapter = HeaderAdapter(resources.getString(R.string.best_results))
        val pictureAdapter = PictureAdapter(this, images, ::hideLoader, ::openPicture)
        resultList.adapter = ConcatAdapter(headerAdapter, pictureAdapter)
        resultList.layoutManager = LinearLayoutManager(this)
    }

    private fun showLoader() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.listScreenContainer, LoadingFragment(), "LoadingFragment")
            .commit()
    }

    private fun hideLoader() {
        val fragment = supportFragmentManager.findFragmentByTag("LoadingFragment")
        if (fragment != null && fragment.isVisible) {
            supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .hide(fragment)
                .commit()
        }
        supportActionBar?.setBackgroundDrawable(ContextCompat
            .getDrawable(this, R.color.beige_normal) )
    }

    override fun createResultList() { }


    fun openPicture(title: String) {
        val picture = pictures?.firstOrNull{ it.title == title } ?: return

        Storage.write(title, picture)
        Storage.getAll()

//        val builder = GsonBuilder()
//        val gson = builder.create()
//        val intent =
//            Intent(this@SimilarPicturesListActivity, PictureActivity::class.java)
//        intent.putExtra("picture", gson.toJson(picture))
//        startActivity(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}