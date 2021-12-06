package com.skvoznyak.findart

import android.os.Bundle
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.skvoznyak.findart.databinding.UploadScreenBinding
import com.skvoznyak.findart.utils.*
import java.util.*
import com.yalantis.ucrop.UCrop
import java.io.File


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

        uploadImageBinding.buttonEdit.setOnClickListener {
            startEditing()
        }

        uploadImageBinding.buttonFind.setOnClickListener {
            if (isOnline(this)) {
                findPicture()
            }
            else {
                noConnection(applicationContext)
            }
//            if (SharedPref.loadNightModeState()) {
//                SharedPref.setNightModeState(false)
//            } else {
//                SharedPref.setNightModeState(true)
//            }
        }
    }

    private fun startEditing() {
        if (bm == null) {
            return
        }
        val imageUri = getImageUri(this, bm!!)
        Log.d("ivan", "OK!")
        openCropActivity(imageUri, imageUri, bm!!.height, bm!!.width)

    }

    fun openCropActivity(sourceUri: Uri, destinationUri: Uri, maxHeight: Int, maxWidth: Int) {
        try {
            UCrop.of(sourceUri, destinationUri)
                .withMaxResultSize(maxWidth, maxHeight)
                .start(this)
            val file: File = File(sourceUri.path!!)
            val deleted = file.delete()
            Log.d("ivan", "Del: ${deleted}")
        } catch (e: Exception) {
            Log.d("ivan", "error in ucrop of")
            e.printStackTrace()
        }
    }

    private fun findPicture() {
        if (bm != null) {
            val vector = tfliteModel.imageToVector(bm!!, this)
            val intent =
                Intent(this@UploadImageActivity, SimilarPicturesListActivity::class.java)
            intent.putExtra("headerFlag", true)
            intent.putExtra("vector", vector)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Log.d("ivan", "OKKK we are in crop result")
            val imageUri = data?.let { UCrop.getOutput(it) }
            val imageView = uploadImageBinding.uploadedImage
            if (imageUri != null) {
                showImageFromUri(this, imageUri, imageView)
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
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
