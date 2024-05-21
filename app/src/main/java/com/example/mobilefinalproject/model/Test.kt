package com.example.mobilefinalproject.model

data class Question(
    val id:Int,
    val text:String,
    val optionA:String,
    val optionB:String,
    val optionC:String,
    val optionD:String,
    val correctAnswer:String
)

data class Test(
    val id: Int,
    val questions: List<Question>?= emptyList(),
    val title: String,
)

data class TestResponse(
    val test: Test,
    val questions: List<Question>,
)