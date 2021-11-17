package com.skvoznyak.findart

import android.content.ContentResolver
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skvoznyak.findart.databinding.ListScreenBinding
import com.skvoznyak.findart.model.SimilarPicture


open class PicturesListActivity :BaseActivity() {

    protected lateinit var listScreenBinding: ListScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addContent()

        createResultList()
    }


    open fun createResultList(){
        val resultList:RecyclerView = findViewById(R.id.resultList)
        resultList.isNestedScrollingEnabled = true

        val pictureAdapter = PictureAdapter(this, resultsMock(), null)
        resultList.adapter = pictureAdapter

        resultList.layoutManager = LinearLayoutManager(this)
    }


    private fun addContent() { //TODO: превратить в фрагмент
        listScreenBinding = ListScreenBinding.inflate(layoutInflater)
        addContentView(
            listScreenBinding.root, ViewGroup.LayoutParams(
                ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    protected fun resultsMock():List<SimilarPicture> {

        val uri1 = "${ContentResolver.SCHEME_ANDROID_RESOURCE}:/" +
                "/${resources.getResourcePackageName(R.drawable.picture_mock)}/" +
                "/${resources.getResourceTypeName(R.drawable.picture_mock)}/" +
                "/${resources.getResourceEntryName(R.drawable.picture_mock)}"
        val uri2 = "${ContentResolver.SCHEME_ANDROID_RESOURCE}:/" +
                "/${resources.getResourcePackageName(R.drawable.picture_mock1)}/" +
                "/${resources.getResourceTypeName(R.drawable.picture_mock1)}/" +
                "/${resources.getResourceEntryName(R.drawable.picture_mock1)}"
        val uri3 = "${ContentResolver.SCHEME_ANDROID_RESOURCE}:/" +
                "/${resources.getResourcePackageName(R.drawable.picture_mock2)}/" +
                "/${resources.getResourceTypeName(R.drawable.picture_mock2)}/" +
                "/${resources.getResourceEntryName(R.drawable.picture_mock2)}"

        return listOf(
            SimilarPicture(0, "Пикник", "Томас Коул", uri1),
            SimilarPicture(0, "Пруд с кувшинками", "Клод Моне", uri2),
            SimilarPicture(0, "Женщина с зонтиком", "Клод Моне", uri3),
        )
    }
}