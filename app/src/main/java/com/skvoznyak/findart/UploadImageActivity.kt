package com.skvoznyak.findart

import android.os.Bundle
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.ViewGroup
import com.skvoznyak.findart.databinding.UploadScreenBinding


class UploadImageActivity : GetImage() {

    private lateinit var uploadImageBinding: UploadScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addContent()

        val requestCode = intent.extras?.get("requestCode") as Int?
        val data = intent.extras?.get("data") as Intent?
        val currentPhotoPath = intent.extras?.get("currentPhotoPath") as String?
        if (requestCode != null) {
            when (requestCode) {
                ACTIVITY_RESULT_CODE_SELECT_FILE -> {
                    if (data != null) {
                        val bm = onSelectFromGalleryResult(data)
                        uploadImageBinding.uploadedImage.setImageBitmap(bm)
                    }
                }
                ACTIVITY_RESULT_CODE_REQUEST_CAMERA -> {
                    if (currentPhotoPath != null) {
                        val bm: Bitmap = BitmapFactory.decodeFile(currentPhotoPath)
                        uploadImageBinding.uploadedImage.setImageBitmap(bm)
                    }
                }
            }
        }

        uploadImageBinding.buttonChooseAnother.setOnClickListener {
            selectImage()
        }

        uploadImageBinding.buttonFind.setOnClickListener {
            val intent = Intent(this@UploadImageActivity, LoadingActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
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
        val bm = super.onCaptureImageResult()
        uploadImageBinding.uploadedImage.setImageBitmap(bm)
        return bm
    }

    override fun onSelectFromGalleryResult(data: Intent?) : Bitmap? {
        val bm = super.onSelectFromGalleryResult(data)
        if (bm != null) {
            uploadImageBinding.uploadedImage.setImageBitmap(bm)
        }
        return bm
    }
}