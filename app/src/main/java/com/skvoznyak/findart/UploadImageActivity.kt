package com.skvoznyak.findart

import android.os.Bundle
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.ViewGroup
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.skvoznyak.findart.databinding.UploadScreenBinding


class UploadImageActivity : GetImage() {

    private lateinit var uploadImageBinding: UploadScreenBinding
    private var bm : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addContent()

        val requestCode = intent.extras?.get("requestCode") as Int?
        val data = intent.extras?.get("data") as Intent?
        val currentPhotoPath = intent.extras?.get("currentPhotoPath") as String?
        if (requestCode != null) {
            when (requestCode) {
                activityResCodeSelectFile -> {
                    if (data != null) {
                        bm = onSelectFromGalleryResult(data)
                    }
                }
                activityResCodeRequestCamera -> {
                    if (currentPhotoPath != null) {
                        bm = BitmapFactory.decodeFile(currentPhotoPath)
                    }
                }
            }
            if (bm != null) uploadImageBinding.uploadedImage.setImageBitmap(bm)
        }

        uploadImageBinding.buttonChooseAnother.setOnClickListener {
            selectImage()
        }

        uploadImageBinding.buttonFind.setOnClickListener {
            if (bm != null) {
                firebaseMagic()
//                val intent = Intent(this@UploadImageActivity, LoadingActivity::class.java)
//                Log.d("ivan", bm.toString())
////                intent.putExtra("imageBm", bm)
//                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
//                startActivity(intent)
            }
        }
    }

    private fun addContent() {
        uploadImageBinding = UploadScreenBinding.inflate(layoutInflater)
        addContentView(
            uploadImageBinding.root, ViewGroup.LayoutParams(
                ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    override fun onCaptureImageResult() : Bitmap {
        bm = super.onCaptureImageResult()
        uploadImageBinding.uploadedImage.setImageBitmap(bm)
        return bm as Bitmap
    }

    override fun onSelectFromGalleryResult(data: Intent?) : Bitmap? {
        bm = super.onSelectFromGalleryResult(data)
        if (bm != null) {
            uploadImageBinding.uploadedImage.setImageBitmap(bm)
        }
        return bm
    }


    private fun firebaseMagic() {

        Log.d("ivan", "---------------Start------------ $bm")

        val image = InputImage.fromBitmap(bm!!, 0)
        val options = ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.7f)
            .build()
        val labeler = ImageLabeling.getClient(options)

        labeler.process(image).addOnSuccessListener { labels ->
            Log.d("ivan", "-------> ${labels.size}")
            for (label in labels) {
                Log.d("ivan", "-> $label")
            }
        }.addOnFailureListener { e -> Log.d("ivan", "errrrrrrror I FOUND IT $e") }


    }

}