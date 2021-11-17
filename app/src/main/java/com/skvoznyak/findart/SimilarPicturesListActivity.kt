package com.skvoznyak.findart

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.FragmentTransaction
import com.skvoznyak.findart.model.PictureRepository
import com.skvoznyak.findart.model.SimilarPicture
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import com.skvoznyak.findart.utils.isOnline
import com.skvoznyak.findart.utils.noConnection


class SimilarPicturesListActivity : PicturesListActivity() {

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vector = intent.extras?.get("vector") as FloatArray
        if (vector.isNotEmpty()) {
            if (isOnline(this)) {

                if (savedInstanceState == null) { showLoader() }

                val similarRequestDisposable = PictureRepository.getSimilarPictures(vector)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ images ->

                        if (images.isNotEmpty()) {
                            makePictureList(images)
                        }
                        else { noResults() }

                    }, { err -> Log.d("ivan", "request error")
                        noResults()
                    })
                disposables.add(similarRequestDisposable)
            }
            else {
                noConnection(applicationContext)
                finish()
            }
        }
        else { noResults() }
    }

//    -------------------------------------------------------

    private fun noResults() {
        hideLoader()

        val resultList:RecyclerView = findViewById(R.id.resultList)
        val headerAdapter = HeaderAdapter(resources.getString(R.string.no_results))
        resultList.adapter = headerAdapter
        resultList.layoutManager = LinearLayoutManager(this)


        showNotFound()

        Log.d("ivan", "No results")
    }
    //    -------------------------------------------------------

    private fun makePictureList(images : List<SimilarPicture>) {
        Log.d("ivan", "Success!")
        for (picture in images){
            Log.d("ivan", "->> $picture")
        }
//        hideLoader()
        setResultList(images)
    }

    private fun setResultList(images : List<SimilarPicture>) {
        val resultList:RecyclerView = findViewById(R.id.resultList)
        resultList.isNestedScrollingEnabled = true

        val headerAdapter = HeaderAdapter(resources.getString(R.string.best_results))
        val pictureAdapter = PictureAdapter(this, images) { hideLoader() }
        resultList.adapter = ConcatAdapter(headerAdapter, pictureAdapter)

        resultList.layoutManager = LinearLayoutManager(this)
    }






    private fun showLoader() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.listScreenContainer, LoadingFragment(), "LoadingFragment")
            .commit()



//        supportActionBar?.setBackgroundDrawable(ContextCompat
//            .getDrawable(this, R.color.toolbar_gray) )

//        window.statusBarColor = resources.getColor(R.color.black)



    }

    private fun hideLoader() {
        Log.d("ivan", "hide")

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


    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }


//    private fun createResultsMock(title: String) : Picture {
//        return Picture(title, "----", R.drawable.picture_mock1)
//    }
}