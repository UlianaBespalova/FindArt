package com.skvoznyak.findart

import android.os.Bundle
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.ViewGroup
import com.skvoznyak.findart.databinding.UploadScreenBinding
import com.skvoznyak.findart.model.SimilarPicture
import com.skvoznyak.findart.utils.GetImage
import io.reactivex.disposables.CompositeDisposable

import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
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


                //---------------------------------------------
                val vector = getVectorFromImage()
                Log.d("ivan", "size = ${vector.size}")
                PRINT_RESUTL(vector)
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

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
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

    //---------------------------------------------------

    lateinit var tflite: Interpreter
    lateinit var tflitemodel: ByteBuffer

    private fun getVectorFromImage() : FloatArray {
        try {
            tflitemodel = loadModelFile(this.assets, "model.tflite")
            tflite = Interpreter(tflitemodel)
        } catch (e: Exception) {
            Log.d("ivan", "Error while loading interpreter")
            e.printStackTrace()
        }

        val res = doInference(bm!!)
        Log.d("ivan", "we got a res_vector")
//        PRINT_RESUTL(res)
        return res
    }



    private fun loadModelFile(assetManager: AssetManager, modelPath: String): ByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun doInference(bm: Bitmap) : FloatArray {

        var res = emptyArray<Float>().toFloatArray()
        try {
            val imageDataType = tflite.getInputTensor(0).dataType()
            val inputImageBuffer = TensorImage(imageDataType)

            val bm_8888: Bitmap = bm.copy(Bitmap.Config.ARGB_8888, true)
            inputImageBuffer.load(bm_8888)

            val imageProcessor = ImageProcessor.Builder()
                .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                .add(getPreprocessNormalizeOp())
                .build()
            val imBuffer = imageProcessor.process(inputImageBuffer)

            //----------------
            val probabilityTensorShape = tflite.getOutputTensor(0).shape()
            val probabilityDataType = tflite.getOutputTensor(0).dataType()
            val outputProbBuffer = TensorBuffer.createFixedSize(probabilityTensorShape, probabilityDataType)

            tflite.run(imBuffer.buffer, outputProbBuffer.buffer.rewind())

            val probabilityProcessor = TensorProcessor.Builder().build()
            res = probabilityProcessor.process(outputProbBuffer).floatArray
        } catch (e: Exception) {
            Log.d("ivan", "error ${e.message}")
        }
        return res
    }

//    private fun doInference(bm: Bitmap) : FloatArray {
//
//        var res = emptyArray<Float>().toFloatArray()
//        try {
//            val imageShape = tflite.getInputTensor(0).shape()
//
//            val imageSizeX = imageShape[1]
//            val imageSizeY = imageShape[2]
//            val imageDataType = tflite.getInputTensor(0).dataType()
//
//            val inputImageBuffer = TensorImage(imageDataType)
//            val bm_8888: Bitmap = bm.copy(Bitmap.Config.ARGB_8888, true)
//            inputImageBuffer.load(bm_8888)
//
//            val cropSize = min(bm_8888.width, bm_8888.height)
//            val imageProcessor = ImageProcessor.Builder()
//                .add(ResizeWithCropOrPadOp(cropSize, cropSize))
//                .add(ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
//                .add(getPreprocessNormalizeOp())
//                .build()
//            val imBuffer = imageProcessor.process(inputImageBuffer)
//
//            //----------------
//            val probabilityTensorShape = tflite.getOutputTensor(0).shape()
//            Log.d("ivan", "probabilityTensorShape = {$probabilityTensorShape[0]} {$probabilityTensorShape[1]}")
//            val probabilityDataType = tflite.getOutputTensor(0).dataType()
//            val outputProbBuffer = TensorBuffer.createFixedSize(probabilityTensorShape, probabilityDataType)
//
//            tflite.run(imBuffer.buffer, outputProbBuffer.buffer.rewind())
//
//            val probabilityProcessor = TensorProcessor.Builder()
//                .add(getPostprocessNormalizeOp())
//                .build()
//            res = probabilityProcessor.process(outputProbBuffer).floatArray
//        } catch (e: Exception) {
//            Log.d("ivan", "error ${e.message}")
//        }
//        return res
//    }

    private fun getPreprocessNormalizeOp() : NormalizeOp {
        val IMAGE_MEAN = 0.0f
        val IMAGE_STD = 1.0f
        return NormalizeOp(IMAGE_MEAN, IMAGE_STD)
    }

    private fun getPostprocessNormalizeOp() : NormalizeOp {
        val PROB_MEAN = 0.0f
        val PROB_STD = 255.0f
        return NormalizeOp(PROB_MEAN, PROB_STD)
    }


    private fun PRINT_RESUTL(res: FloatArray) {
        for (step in (0..3)) {
            Log.d("ivan", "---------------$step--------------")

            val vector = mutableListOf<Float>()
            for (i in (256*step..256*(step+1)-1)) {
                vector.add(res[i])
            }
            val str = vector.joinToString(separator = "|")
            Log.d("ivan", str)
        }
    }
}