package com.skvoznyak.findart.model

import com.google.gson.annotations.SerializedName

data class Picture(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("painter")
    val painter: String,
    @SerializedName("image")
    val image: String,
)



