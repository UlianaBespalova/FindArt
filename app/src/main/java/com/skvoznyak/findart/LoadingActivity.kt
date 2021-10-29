package com.skvoznyak.findart

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.*
//import com.google.android.gms.tasks.OnFailureListener
//import com.google.android.gms.tasks.OnSuccessListener
//import com.google.firebase.ml.vision.FirebaseVision
//import com.google.firebase.ml.vision.common.FirebaseVisionImage
//import com.google.firebase.ml.vision.label.FirebaseVisionLabel
//import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions
import com.skvoznyak.findart.databinding.LayoutToolbarBinding
import com.skvoznyak.findart.databinding.LoadingScreenBinding


class LoadingActivity : BaseActivity() {

    private lateinit var loadingBinding: LoadingScreenBinding
    private lateinit var progressBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressBar = loadingBinding.horizontalDottedProgress
        showProgressBar(true)

        Log.d("ivan", "create loading")
//        val imageBm = intent.extras?.get("imageBm") as Bitmap
//        Log.d("ivan", "get bitmap $imageBm")
//        firebaseMagic(imageBm)


        //----------------заглушка для выхода-----------------
        loadingBinding.root.setOnClickListener {
            val intent = Intent(this@LoadingActivity, PicturesListActivity::class.java)
            intent.putExtra("headerFlag", true)
            startActivity(intent)
        }
        //---------------------------------
    }

    override fun addActivity() {
        loadingBinding = LoadingScreenBinding.inflate(layoutInflater)
        setContentView(loadingBinding.root)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun addToolbar() {
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

    private fun showProgressBar(visibility: Boolean) {
        progressBar.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
    }



//    private fun firebaseMagic(imageBm : Bitmap) {
//
//        Log.d("ivan", "Start")
//        val options = FirebaseVisionLabelDetectorOptions.Builder()
//            .setConfidenceThreshold(0.7f)
//            .build()
//
//        val image = FirebaseVisionImage.fromBitmap(imageBm)
//        val detector = FirebaseVision.getInstance().getVisionLabelDetector(options)
//
//        detector.detectInImage(image).addOnSuccessListener(OnSuccessListener<List<FirebaseVisionLabel>>() {
//            fun onSuccess(labels : List<FirebaseVisionLabel>) {
//                Log.d("ivan", "Success!!! $labels")
//                //Do something if a label is detected//
//            }
//        }).addOnFailureListener(OnFailureListener() {
//
//            fun onFailure(e: Exception) {
//                Log.d("ivan", "errrror $e")
//                //Task failed with an exception//
//            }
//        })
//    }


}
