package com.example.mobilefinalproject.model

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("email")
    var email: String,

    @SerializedName("password")
    var password: String
)


data class RegisterRequest (
    @SerializedName("name")
    var name: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("password")
    var password: String
)

data class UpdateRequest (
    @SerializedName("name")
    var name: String?=null,

    @SerializedName("email")
    var email: String?=null,

    @SerializedName("password")
    var password: String?=null
)

data class AuthResponse(
    val user: User,
    val token: String
)


