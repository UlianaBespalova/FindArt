package com.skvoznyak.findart

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException

open class GetImage : BaseActivity() {

    private val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 10001
    val ACTIVITY_RESULT_CODE_REQUEST_CAMERA = 10002
    val ACTIVITY_RESULT_CODE_SELECT_FILE = 10003
    private val TITLE_ADD_PHOTO = "Добавить фото"
    private val TITLE_SELECT_FILE = "Выбрать файл"
    private val TAKE_PHOTO = "Сделать фото"
    private val CHOOSE_FROM_GALLERY = "Выбрать из Галереи"
    private val CANCEL = "Отмена"
    private val ACTIONS = arrayOf<CharSequence>(CHOOSE_FROM_GALLERY, TAKE_PHOTO, CANCEL)
    private var userChosenTask = ""

    private val fileName = "photo"
    lateinit var storageDirectory: File
    lateinit var imageFile: File
    lateinit var currentPhotoPath: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        imageFile = File.createTempFile(fileName, ".jpg", storageDirectory)
        currentPhotoPath = imageFile.absolutePath
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ACTIVITY_RESULT_CODE_SELECT_FILE -> {
                    onSelectFromGalleryResult(data)
                }
                ACTIVITY_RESULT_CODE_REQUEST_CAMERA -> {
                    onCaptureImageResult()
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
                    if (userChosenTask == TAKE_PHOTO) {
                        cameraIntent()
                    } else if (userChosenTask == CHOOSE_FROM_GALLERY) {
                        galleryIntent()
                    }
                }
            }
        }
    }

    open fun onSelectFromGalleryResult(data: Intent?) : Bitmap? {
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
        return bm
    }

    open fun onCaptureImageResult() : Bitmap {
        val bm: Bitmap = BitmapFactory.decodeFile(currentPhotoPath)
        return bm
    }

    fun selectImage() {
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
        val imageUri = FileProvider.getUriForFile(this@GetImage, "com.skvoznyak.findart.fileprovider", imageFile)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
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
                    alertBuilder.setTitle("Требуется разрешение")
                    alertBuilder.setMessage("Для выполнения действия приложению необходим доступ к фото")
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