package com.skvoznyak.findart

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.skvoznyak.findart.databinding.LoadingScreenBinding


class LoadingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        super.onCreateView(inflater, container, savedInstanceState)

        val loadingBinding = LoadingScreenBinding.inflate(layoutInflater)
        return loadingBinding.root
    }

}





//
//    private lateinit var loadingBinding: LoadingScreenBinding
//    private lateinit var progressBar: View
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        progressBar = loadingBinding.horizontalDottedProgress
//        showProgressBar(true)
//
//
////        //----------------заглушка для выхода-----------------
////        loadingBinding.root.setOnClickListener {
////            val intent = Intent(this@LoadingActivity, PicturesListActivity::class.java)
////            intent.putExtra("headerFlag", true)
////            startActivity(intent)
////        }
////        //---------------------------------
//    }
//
//    override fun addActivity() {
//        loadingBinding = LoadingScreenBinding.inflate(layoutInflater)
//        setContentView(loadingBinding.root)
//    }
//
//    @SuppressLint("UseCompatLoadingForDrawables")
//    override fun addToolbar() {
//        val toolbarBinding = LayoutToolbarBinding.inflate(layoutInflater)
//        addContentView(
//            toolbarBinding.root, ViewGroup.LayoutParams(
//                ViewGroup
//                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//        )
//        setSupportActionBar(toolbarBinding.toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeButtonEnabled(true)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//    }
//
//    private fun showProgressBar(visibility: Boolean) {
//        progressBar.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
//    }
//}
