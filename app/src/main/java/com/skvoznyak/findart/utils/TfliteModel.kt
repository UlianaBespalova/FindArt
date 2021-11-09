package com.skvoznyak.findart.utils

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.min

class TfliteModel {

    private lateinit var tflite: Interpreter
    private lateinit var tflitemodel: ByteBuffer

    private val modelPath = "model.tflite"


    fun doMagic(bm: Bitmap): FloatArray {

    }


    fun loadModel(assets : AssetManager) {
        try {
            tflitemodel = loadModelFile(assets, modelPath)
            tflite = Interpreter(tflitemodel)
        } catch (e: Exception) {
            Log.d("ivan", "Error while loading interpreter")
            e.printStackTrace()
        }
    }

    fun getVectorFromImage(bm: Bitmap): FloatArray {
        return doInference(bm)
    }



    private fun doInference(bm: Bitmap) : FloatArray {

        var res = emptyArray<Float>().toFloatArray()
        try {

            //----------------------------------------------------------------------
            val imageShape = tflite.getInputTensor(0).shape()
            val imageDataType = tflite.getInputTensor(0).dataType()

            val inputImageBuffer = TensorImage(imageDataType)
            val bm_8888: Bitmap = bm.copy(Bitmap.Config.ARGB_8888, true)
            inputImageBuffer.load(bm_8888)

            val cropSize = min(bm_8888.width, bm_8888.height)
            val imageProcessor = createImageProcessor(imageShape[1], imageShape[2], cropSize)
            val imBuffer = imageProcessor.process(inputImageBuffer)

            //----------------
            val probabilityTensorShape = tflite.getOutputTensor(0).shape()
            val probabilityDataType = tflite.getOutputTensor(0).dataType()
            val outputProbBuffer = TensorBuffer.createFixedSize(probabilityTensorShape, probabilityDataType)

            tflite.run(imBuffer.buffer, outputProbBuffer.buffer.rewind())

            val probabilityProcessor = createTensorProcessor()
            res = probabilityProcessor.process(outputProbBuffer).floatArray
        } catch (e: Exception) {
            Log.d("ivan", "error ${e.message}")
        }
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

    private fun createImageProcessor(imageSizeX : Int, imageSizeY : Int, cropSize : Int)
            : ImageProcessor {
        return ImageProcessor.Builder()
            .add(ResizeWithCropOrPadOp(cropSize, cropSize))
            .add(ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(getPreprocessNormalizeOp())
            .build()
    }

    private fun createTensorProcessor () : TensorProcessor {
        return TensorProcessor.Builder()
            .add(getPostprocessNormalizeOp())
            .build()
    }

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


    fun PRINT_RESUTL(res: FloatArray) {

//        for (step in (0..3)) {
        for (step in (0..0)) {
            Log.d("ivan", "---------------$step--------------")

            val vector = mutableListOf<Float>()
            for (i in (256*step..256*(step+1)-1)) {
                vector.add(res[i])
            }
            val str = vector.joinToString(separator = "|")
            Log.d("ivan", str)
        }
//    }
    }

}