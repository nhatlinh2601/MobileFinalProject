package com.example.mobilefinalproject.model


data class Category(
    val id:Int,
    val name:String,
    val image_path:String
)
data class CategoryResponse(
    val categories: List<Category>
)
