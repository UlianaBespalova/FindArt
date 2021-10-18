package com.skvoznyak.findart

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skvoznyak.findart.databinding.ActivityMainBinding

class ContentActivity:AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_screen)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_left_black_24dp)


        val resultList:RecyclerView = findViewById(R.id.resultList)

        resultList.isNestedScrollingEnabled = true;
        val pictureAdapter = PictureAdapter(resultsMock())
        val headerAdapter = HeaderAdapter()
        //        resultList.adapter = pictureAdapter
        resultList.adapter = ConcatAdapter(headerAdapter, pictureAdapter)
        resultList.layoutManager = LinearLayoutManager(this)
    }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu, menu)
            return true
        }



    private fun resultsMock():List<Picture> {
        return listOf(
            Picture("Пикник", "Томас Коул", R.drawable.picture_mock),
            Picture("Пруд с кувшинками", "Клод Моне", R.drawable.picture_mock1),
            Picture("Женщина с зонтиком", "Клод Моне", R.drawable.picture_mock2),
        )
    }
}