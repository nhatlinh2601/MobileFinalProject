package com.example.mobilefinalproject.model

import com.google.gson.annotations.SerializedName
import kotlin.time.Duration

class Lesson(
    val id: Int,
    val course_id: Int,
    val name: String,
    val description: String,
    val duration: Int,
    val status: Int,
    var videos: List<Video>,
    val document: List<Document> = Document.getDoc(),
){

}


data class LessonResponse(
    val course: Course,
    val lesson: Lesson,
    val teacher: Teacher
)
