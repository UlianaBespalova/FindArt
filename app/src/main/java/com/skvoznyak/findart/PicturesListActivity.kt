package com.skvoznyak.findart

import android.os.Bundle

import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skvoznyak.findart.adapters.Picture
import com.skvoznyak.findart.databinding.ListScreenBinding


class PicturesListActivity :BaseActivity() {

    private lateinit var listScreenBinding: ListScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addContent()

        val resultList:RecyclerView = findViewById(R.id.resultList)
        resultList.isNestedScrollingEnabled = true
        val pictureAdapter = PictureAdapter(resultsMock())

        if (intent.extras?.get("headerFlag") as? Boolean == true) {
            val headerAdapter = HeaderAdapter()
            resultList.adapter = ConcatAdapter(headerAdapter, pictureAdapter)
        } else {
            resultList.adapter = pictureAdapter
        }
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


    private fun resultsMock():List<Picture> {
        return listOf(
            Picture("Пикник", "Томас Коул", R.drawable.picture_mock),
            Picture("Пруд с кувшинками", "Клод Моне", R.drawable.picture_mock1),
            Picture("Женщина с зонтиком", "Клод Моне", R.drawable.picture_mock2),
        )
    }
}