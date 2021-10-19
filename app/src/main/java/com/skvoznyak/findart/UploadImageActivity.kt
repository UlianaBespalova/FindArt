package com.skvoznyak.findart

import android.Manifest
import android.annotation.TargetApi
import android.os.Bundle
import android.content.DialogInterface
import android.provider.MediaStore
import android.content.Intent
import android.content.pm.PackageManager
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Environment.getExternalStorageDirectory
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.skvoznyak.findart.databinding.UploadScreenBinding
import java.io.*


class UploadImageActivity : MainActivity() {

    private lateinit var uploadImageBinding: UploadScreenBinding
    private val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 10001
    private val ACTIVITY_RESULT_CODE_REQUEST_CAMERA = 10002
    private val ACTIVITY_RESULT_CODE_SELECT_FILE = 10003
    private val TITLE_ADD_PHOTO = "Добавить фото"
    private val TITLE_SELECT_FILE = "Выбрать файл"
    private val TAKE_PHOTO = "Сделать фото"
    private val CHOOSE_FROM_GALLERY = "Выбрать из Галереи"
    private val CANCEL = "Отмена"
    private val ACTIONS = arrayOf<CharSequence>(TAKE_PHOTO, CHOOSE_FROM_GALLERY, CANCEL)
    private var userChosenTask = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uploadImageBinding = UploadScreenBinding.inflate(layoutInflater)
        setContentView(uploadImageBinding.root)

        //Вынести весь этот функицонал в отдельный файл
        uploadImageBinding.buttonChooseAnother.setOnClickListener {
            selectImage()
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ACTIVITY_RESULT_CODE_SELECT_FILE -> {
                    onSelectFromGalleryResult(data)
                }
                ACTIVITY_RESULT_CODE_REQUEST_CAMERA -> {
                    onCaptureImageResult(data)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChosenTask.equals(TAKE_PHOTO)) {
                        cameraIntent()
                    } else if (userChosenTask.equals(CHOOSE_FROM_GALLERY)) {
                        galleryIntent()
                    }
                }
            }
        }
    }


    private fun onSelectFromGalleryResult(data: Intent?) {
        var bm: Bitmap? = null
        if (data != null) {
            try {
                bm = if (Build.VERSION.SDK_INT < 29) {
                    MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, data.data)
                } else {
                    val source = ImageDecoder.createSource(applicationContext.contentResolver, data.data!!)
                    ImageDecoder.decodeBitmap(source)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        uploadImageBinding.uploadedImage.setImageBitmap(bm)
//        if (bm != null) saveImage(bm)
    }

    private fun onCaptureImageResult(data: Intent?) {
        if (data == null) return
        val thumbnail = data.extras!!.get("data") as Bitmap
        uploadImageBinding.uploadedImage.setImageBitmap(thumbnail)
//        saveImage(thumbnail)
    }

//    private fun saveImage(bitmap: Bitmap) {
//
//        val bytes = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
//
//        // create a directory if it doesn't already exist
//        val photoDirectory = File(getExternalStorageDirectory().absolutePath + "/cameraphoto/")
//        if (!photoDirectory.exists()) {
//            photoDirectory.mkdirs()
//        }
//        val destination = File(photoDirectory,   "captured_photo.jpg")
//        Log.d("photo path:", destination.absolutePath)
//        val fo: FileOutputStream
//        try {
//            destination.createNewFile()
//            fo = FileOutputStream(destination)
//            fo.write(bytes.toByteArray())
//            fo.close()
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//

    private fun selectImage() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(TITLE_ADD_PHOTO)
        builder.setItems(ACTIONS, DialogInterface.OnClickListener { dialog, item ->
            val result = checkPermission(this)
            if (ACTIONS[item] == TAKE_PHOTO) {
                userChosenTask = TAKE_PHOTO
                if (result) {
                    cameraIntent()
                }
            } else if (ACTIONS[item] == CHOOSE_FROM_GALLERY) {
                userChosenTask = CHOOSE_FROM_GALLERY
                if (result) {
                    galleryIntent()
                }
            } else if (ACTIONS[item] == CANCEL) {
                dialog.dismiss()
            }
        })
        builder.show()
    }



    private fun cameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, ACTIVITY_RESULT_CODE_REQUEST_CAMERA)
    }

    private fun galleryIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, TITLE_SELECT_FILE), ACTIVITY_RESULT_CODE_SELECT_FILE)
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun checkPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    val alertBuilder = AlertDialog.Builder(context)
                    alertBuilder.setCancelable(true)
                    alertBuilder.setTitle("Permission Needed")
                    alertBuilder.setMessage("External storage permission is needed to proceed.")
                    alertBuilder.setPositiveButton(android.R.string.yes) { _, _ ->
                        ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                    }
                    val alert = alertBuilder.create()
                    alert.show()
                } else {
                    ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }
}