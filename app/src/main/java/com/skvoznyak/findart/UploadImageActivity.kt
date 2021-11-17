package com.skvoznyak.findart

import android.os.Bundle
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.ViewGroup
import com.skvoznyak.findart.databinding.UploadScreenBinding
import com.skvoznyak.findart.utils.GetImage
import com.skvoznyak.findart.utils.TfliteModel
import java.util.*
import com.skvoznyak.findart.utils.isOnline
import com.skvoznyak.findart.utils.noConnection


class UploadImageActivity : GetImage() {

    private lateinit var uploadImageBinding: UploadScreenBinding

    private var bm : Bitmap? = null
    private val tfliteModel = TfliteModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addContent()

        val requestCode = intent.extras?.get("requestCode") as Int?
        val data = intent.extras?.get("data") as Intent?
        val currentPhotoPath = intent.extras?.get("currentPhotoPath") as String?
        if (requestCode != null) {
            processImage(requestCode, currentPhotoPath, data)
            if (bm != null) uploadImageBinding.uploadedImage.setImageBitmap(bm)
        }

        uploadImageBinding.buttonChooseAnother.setOnClickListener {
            selectImage()
        }

        uploadImageBinding.buttonFind.setOnClickListener {

            if (isOnline(this)) {
                if (bm != null) {
                    val vector = tfliteModel.doMagic(bm!!, this)
                    val intent =
                        Intent(this@UploadImageActivity, SimilarPicturesListActivity::class.java)
                    intent.putExtra("headerFlag", true)
                    intent.putExtra("vector", vector)
                    startActivity(intent)
                }
            }
            else {
                noConnection(applicationContext)
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


    private fun processImage(requestCode : Int, currentPhotoPath : String?, data : Intent?) {
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
}