package com.skvoznyak.findart.model

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class SimilarPicture(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("painter")
    val painter: String,
    @SerializedName("image")
    val image: String,
)



