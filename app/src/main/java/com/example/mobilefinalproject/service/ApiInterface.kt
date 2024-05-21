package com.example.mobilefinalproject.service

import com.example.mobilefinalproject.model.AuthResponse
import com.example.mobilefinalproject.model.CartResponse
import com.example.mobilefinalproject.model.Category
import com.example.mobilefinalproject.model.CategoryResponse
import com.example.mobilefinalproject.model.Course
import com.example.mobilefinalproject.model.CourseResponse
import com.example.mobilefinalproject.model.Lesson
import com.example.mobilefinalproject.model.LessonResponse
import com.example.mobilefinalproject.model.LoginRequest

import com.example.mobilefinalproject.model.RegisterRequest
import com.example.mobilefinalproject.model.TestResponse

import com.example.mobilefinalproject.model.UpdateRequest
import com.example.mobilefinalproject.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {
    @GET("categories")
    fun getCategories():  Call<CategoryResponse>

    @GET("categories/{id}")
    fun getCoursesByCate(@Path("id") categoryId: Int): Call<CourseResponse>

    @GET("courses")
    fun getCourses():  Call<CourseResponse>

    @GET("courses/popular")
    fun getPopularCourses():  Call<CourseResponse>

    @GET("courses/recommend")
    fun getRecommendCourses():  Call<CourseResponse>

    @GET("courses/{id}")
    fun getInfoCourse(@Path("id") courseID:Int):  Call<CourseResponse>

    @GET("courses/lesson/{id}")
    fun getInfoLesson(@Path("id") lessonID:Int):  Call<LessonResponse>

    @GET("courses/lesson/status/{id}")
    fun updateStatus(@Path("id") lessonID :Int):  Call<Lesson>

    @GET("search/{search}")
    fun getCoursesBySearchKey(@Path("search") request: String): Call<CourseResponse>

    @POST("login")
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<AuthResponse>

    @POST("register")
    fun register(
        @Body registerRequest: RegisterRequest
    ): Call<AuthResponse>

    @POST("user/update/{id}")
    fun update(
        @Path("id") userID:Int,
        @Body updateRequest: UpdateRequest
    ): Call<AuthResponse>


    @GET("user/{id}")
    fun getUsers(@Path("id") userID: Int): Call<User>
    @GET("user/courses/{id}")
    fun getMyCourses(@Path("id") userID: Int): Call<CourseResponse>

    @GET("user/cart/{id}")
    fun getCart(@Path("id") userID: Int): Call<CartResponse>

    @GET("myCart/{id}")
    fun getCountOfCart(@Path("id") userID: Int): Call<CartResponse>

    @GET("user/cart/delete/{userID}/{courseID}")
    fun deleteCart(
        @Path("userID") userID: Int,
        @Path("courseID") courseID: Int
    ): Call<CartResponse>

    @GET("user/cart/add/{userID}/{courseID}")
    fun addToCart(
        @Path("userID") userID: Int,
        @Path("courseID") courseID: Int
    ): Call<CartResponse>

    @GET("isMyCourses/{userID}/{courseID}")
    fun isMyCourses(
        @Path("userID") userID: Int,
        @Path("courseID") courseID: Int
    ): Call<CourseResponse>

    @GET("isMyCart/{userID}/{courseID}")
    fun isMyCart(
        @Path("userID") userID: Int,
        @Path("courseID") courseID: Int
    ): Call<CourseResponse>

    @GET("lesson/test/{lessonID}")
    fun getTest(
        @Path("lessonID") lessonID: Int,
    ): Call<TestResponse>


}