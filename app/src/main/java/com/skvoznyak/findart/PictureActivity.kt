package com.skvoznyak.findart

import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AlertDialog
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import android.view.View
import android.view.ViewGroup
import com.skvoznyak.findart.databinding.PictureScreenFullscreenImageBinding
import com.skvoznyak.findart.databinding.PictureScreenBinding
import com.google.gson.Gson

import com.google.gson.GsonBuilder
import com.skvoznyak.findart.model.Picture
import com.squareup.picasso.Picasso


class PictureActivity : BaseActivity() {

    private lateinit var pictureBinding: PictureScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addContent()

        val pictureJson = intent.extras?.get("picture") as String
        val builder = GsonBuilder()
        val gson = builder.create()
        val picture: Picture? = gson.fromJson(pictureJson, Picture::class.java)

        if (picture != null) {
            setData(picture)
            pictureBinding.pictureImage.setOnClickListener {
                openImage(picture.image)
            }
        }
    }

    private fun setData(picture: Picture) {
        pictureBinding.pictureTitle.text = picture.title
        pictureBinding.picturePainter.text = picture.painter
        pictureBinding.pictureYear.text = picture.year
        val text = "\t\t\t\t${picture.text.replace("*", "\n\n\t\t\t\t")}"
        pictureBinding.pictureText.text = text

        Picasso.with(this@PictureActivity).load(picture.image)
            .into(pictureBinding.pictureImage)
    }

    private fun openImage(image: String) {
        var bundle : Bundle? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val v = pictureBinding.pictureImage
            val options = android.app.ActivityOptions.makeSceneTransitionAnimation(
                this@PictureActivity, v, "open_image")
            bundle = options.toBundle()
        }
        val intent = Intent(this@PictureActivity, FullScreenActivity::class.java)
        intent.putExtra("image", image)
        if (bundle == null) {
            startActivity(intent)
        } else {
            startActivity(intent, bundle)
        }
//            val bitmap = ContextCompat.getDrawable(this, R.drawable.picture_mock);
//                mPhotoView.setImageDrawable(bitmap)
    }

    private fun addContent() {
        pictureBinding = PictureScreenBinding.inflate(layoutInflater)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            pictureBinding.pictureText.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }
        addContentView(
            pictureBinding.root, ViewGroup.LayoutParams(
                ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }
}
