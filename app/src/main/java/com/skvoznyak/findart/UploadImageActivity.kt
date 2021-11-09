package com.skvoznyak.findart

import android.os.Bundle
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.ViewGroup
import com.skvoznyak.findart.databinding.UploadScreenBinding
import com.skvoznyak.findart.model.SimilarPicture
import com.skvoznyak.findart.utils.GetImage
import com.skvoznyak.findart.utils.TfliteModel
import io.reactivex.disposables.CompositeDisposable
import java.util.*


class UploadImageActivity : GetImage() {

    private lateinit var uploadImageBinding: UploadScreenBinding
    private val disposables = CompositeDisposable()
    private var bm : Bitmap? = null


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


        val tfliteModel = TfliteModel()
        tfliteModel.loadModel(this.assets)

        uploadImageBinding.buttonChooseAnother.setOnClickListener {
            selectImage()
        }

        uploadImageBinding.buttonFind.setOnClickListener {
            if (bm != null) {


                val vector = tfliteModel.doMagic(bm!!)
                Log.d("ivan", "size = ${vector.size}")
                tfliteModel.PRINT_RESUTL(vector)

//                val vector = tfliteModel.getVectorFromImage(bm!!)
//                Log.d("ivan", "size = ${vector.size}")
//                tfliteModel.PRINT_RESUTL(vector)

                //---------------------------------------------

//                val similarRequestDisposable = PictureRepository.getSimilarPictures(vector)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({
//                        images -> onSuccess(images)
//                    }, {err -> Log.d("ivan", "subscribe eror: $err")})
//                disposables.add(similarRequestDisposable)
//                //---------------------------------------------
//                Log.d("ivan", "Request made, go to loading")
//                val intent = Intent(this@UploadImageActivity, LoadingActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
//                startActivity(intent)
            }
        }
    }

    private fun onSuccess(images : List<SimilarPicture>) {
        Log.d("ivan", "Sucess!")
        for (picture in images){
            Log.d("ivan", "->> $picture")
        }
        val intent = Intent(this@UploadImageActivity, PicturesListActivity::class.java)
        intent.putExtra("headerFlag", true)
        intent.putExtra("data", images.toTypedArray())
//        startActivity(intent)
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

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}