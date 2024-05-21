package com.example.mobilefinalproject.model

import com.google.gson.annotations.SerializedName

data class Course(
    val id: Int,
    val name: String,
    val image_path: String,
    val detail: String,
    val price: Double,
    val sale_price: Double,
    val duration: Int,
    val is_documen: Boolean,
    val teacher_id: Int,
    val category_id: Int,
    val status: Int,
    val lessons: List<Lesson>?= emptyList()
){
    fun convertMinutesToHoursAndMinutes(minutes: Int): String {
        val hours = minutes / 60
        val leftoverMinutes = minutes % 60
        return "${hours}h ${leftoverMinutes} mins"
    }
}

data class CourseResponse(
    val courses:List<Course>,

    val coursesByCate:List<Course>,

    val coursesBySearchKey:List<Course>,

    @SerializedName("Popular Courses")
    val popularCourses:List<Course>,

    @SerializedName("Recommend Courses")
    val recommendCourses:List<Course>,

    @SerializedName("coursesByCategory")
    val coursesRelatedByCategory:List<Course>,

    @SerializedName("lessons")
    val lessons:List<Lesson>,

    @SerializedName("infoCourse")
    val infoCourse:Course,

    @SerializedName("My Courses")
    val myCourses:List<Course>,

    @SerializedName("IsMyCourse")
    val isMyCourse:Boolean,

    @SerializedName("IsMyCart")
    val isMyCart:Boolean,

)


