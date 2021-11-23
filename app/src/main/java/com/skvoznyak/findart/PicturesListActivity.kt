package com.skvoznyak.findart

import android.content.ContentResolver
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skvoznyak.findart.databinding.ListScreenBinding
import com.skvoznyak.findart.model.Picture


open class PicturesListActivity :BaseActivity() {

    private lateinit var listScreenBinding: ListScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addContent()

        createResultList()
    }

    open fun createResultList(){
        val resultList:RecyclerView = findViewById(R.id.resultList)
        resultList.isNestedScrollingEnabled = true

        val pictureAdapter = PictureAdapter(this, resultsMock(), null, null)
        resultList.adapter = pictureAdapter

        resultList.layoutManager = LinearLayoutManager(this)
    }

    private fun addContent() {
        listScreenBinding = ListScreenBinding.inflate(layoutInflater)
        addContentView(
            listScreenBinding.root, ViewGroup.LayoutParams(
                ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    private fun resultsMock():List<Picture> {
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
            Picture(0, "Пикник", "Томас Коул", "1000", uri1, "kek"),
            Picture(0, "Пруд с кувшинками", "Клод Моне", "2000", uri2, "kek"),
            Picture(0, "Женщина с зонтиком", "Клод Моне", "3000", uri3, "kek"),
        )
    }
}