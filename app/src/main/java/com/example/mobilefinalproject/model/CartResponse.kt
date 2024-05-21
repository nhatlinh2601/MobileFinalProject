package com.example.mobilefinalproject.model

import com.google.gson.annotations.SerializedName

data class CartResponse(
    val courses: List<Course>,
    val total: Double,
    @SerializedName("message-remove")
    val messageRemove: String,
    @SerializedName("message-addToCart")
    val messageAddToCart: String,
    @SerializedName("itemCount")
    val count:Int
)