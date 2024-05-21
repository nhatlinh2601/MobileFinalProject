package com.example.mobilefinalproject.viewmodel

import android.provider.ContactsContract.StatusUpdates
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilefinalproject.model.CartResponse
import com.example.mobilefinalproject.model.Course
import com.example.mobilefinalproject.model.CourseResponse
import com.example.mobilefinalproject.model.Lesson
import com.example.mobilefinalproject.model.LessonResponse
import com.example.mobilefinalproject.model.Question
import com.example.mobilefinalproject.model.Teacher
import com.example.mobilefinalproject.model.Test
import com.example.mobilefinalproject.model.TestResponse
import com.example.mobilefinalproject.model.Video
import com.example.mobilefinalproject.service.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CourseViewModel : ViewModel() {

    private val _coursesRelatedByCate = MutableStateFlow<List<Course>>(emptyList())
    val coursesRelatedByCate = _coursesRelatedByCate.asStateFlow()

    private val _infoCourse = MutableStateFlow<Course?>(null)
    val infoCourse = _infoCourse.asStateFlow()

    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons = _lessons.asStateFlow()

    private val _teacher = MutableStateFlow<Teacher?>(null)
    val teacher = _teacher.asStateFlow()

    private val _infoLesson = MutableStateFlow<Lesson?>(null)
    val infoLesson = _infoLesson.asStateFlow()

    private val _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos = _videos.asStateFlow()

    private val _coursesOfCart = MutableStateFlow<List<Course>>(emptyList())
    val coursesOfCart = _coursesOfCart.asStateFlow()

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions = _questions.asStateFlow()

    private val _test = MutableStateFlow<Test?>(null)
    val test = _test.asStateFlow()

    private val _cartTotal = MutableStateFlow(0.00)
    val cartTotal = _cartTotal.asStateFlow()

    private val _messageDeleteState = MutableStateFlow<String?>(null)
    val messageDeleteState=_messageDeleteState.asStateFlow()

    private val _messageAddState = MutableStateFlow<String?>(null)
    val messageAddState=_messageAddState.asStateFlow()

    private val _isOpenDialog = MutableStateFlow<Boolean>(false)
    val isOpenDialog=_isOpenDialog.asStateFlow()

    private val _isMyCourse = MutableStateFlow(false)
    val isMyCourse=_isMyCourse.asStateFlow()

    private val _isCompleteLesson = MutableStateFlow<Int>(0)
    val isCompleteLesson = _isCompleteLesson.asStateFlow()

    private val _countOfCart = MutableStateFlow(0)
    val countOfCart = _countOfCart.asStateFlow()


    private val _isMyCart = MutableStateFlow(false)
    val isMyCart=_isMyCart.asStateFlow()

    fun fetchInfoCourseById(id: Int) {
        _infoCourse.value=null
        _lessons.value= emptyList()
        viewModelScope.launch {
            val call: Call<CourseResponse> = ApiService.apiService.getInfoCourse(id)
            call.enqueue(object : Callback<CourseResponse> {
                override fun onResponse(
                    call: Call<CourseResponse>,
                    response: Response<CourseResponse>
                ) {
                    if (response.isSuccessful) {
                        val courseResponse = response.body()
                        _coursesRelatedByCate.value =
                            courseResponse?.coursesRelatedByCategory ?: emptyList()
                        _lessons.value = courseResponse?.lessons ?: emptyList()
                        _infoCourse.value = courseResponse?.infoCourse!!
                        Log.e("info-course-response", courseResponse.toString())
                        Log.e("info-course-data-log", _infoCourse.value.toString())
                        Log.e(
                            "info-course-related-data-log",
                            _coursesRelatedByCate.value.toString()
                        )
                        Log.e("info-course-lesson-data-log", _lessons.value.toString())


                    } else {
                        Log.e("course-log-error", response.toString())
                    }
                }

                override fun onFailure(call: Call<CourseResponse>, t: Throwable) {
                    Log.e("API Error", "Error: ${t.message}")
                    t.printStackTrace()
                }
            })
        }
    }

    fun fetchLesson(lessonID: Int) {

        viewModelScope.launch {
            val call: Call<LessonResponse> = ApiService.apiService.getInfoLesson(lessonID)
            call.enqueue(object : Callback<LessonResponse> {
                override fun onResponse(
                    call: Call<LessonResponse>,
                    response: Response<LessonResponse>
                ) {
                    if (response.isSuccessful) {
                        val courseResponse = response.body()
                        _infoLesson.value=courseResponse?.lesson!!
                        _videos.value = courseResponse.lesson.videos
                        _teacher.value=courseResponse.teacher
                    }
                }

                override fun onFailure(call: Call<LessonResponse>, t: Throwable) {
                    Log.e("API Error", "Error: ${t.message}")
                    t.printStackTrace()
                }
            })
        }
    }

    fun updateStatus(lessonID: Int){
        _isOpenDialog.value=false
        viewModelScope.launch {
            val call: Call<Lesson> = ApiService.apiService.updateStatus(lessonID)
            call.enqueue(object : Callback<Lesson> {
                override fun onResponse(call: Call<Lesson>, response: Response<Lesson>) {
                   fetchLesson(lessonID)
                }

                override fun onFailure(call: Call<Lesson>, t: Throwable) {
                    Log.e("API Error", "Error: ${t.message}")
                    t.printStackTrace()
                }

            })
        }
    }

    fun fetchCart(userID:Int){
        viewModelScope.launch {
            val call: Call<CartResponse> = ApiService.apiService.getCart(userID)
            call.enqueue(object : Callback<CartResponse> {
                override fun onResponse(
                    call: Call<CartResponse>,
                    response: Response<CartResponse>
                ) {
                    if (response.isSuccessful) {
                        val cartResponse = response.body()
                        if (cartResponse != null) {
                            _coursesOfCart.value=cartResponse.courses
                            _cartTotal.value=cartResponse.total
                        }
                        Log.e("cart-response", cartResponse.toString())
                        Log.e(
                            "info-courseCart-data-log",
                            _infoLesson.value.toString()
                        )
                    } else {
                        Log.e("lesson-log-error", response.toString())
                    }
                }

                override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                    Log.e("API Error", "Error: ${t.message}")
                    t.printStackTrace()
                }
            })
        }
    }

    fun isMyCart(userID: Int,courseID: Int){
        viewModelScope.launch {
            try {
                ApiService.apiService.isMyCart(userID,courseID).enqueue(object :
                    Callback<CourseResponse> {
                    override fun onResponse(
                        call: Call<CourseResponse>,
                        response: Response<CourseResponse>
                    ) {
                        if (response.isSuccessful) {
                            val courseResponse = response.body()
                            if (courseResponse != null) {
                                _isMyCart.value=courseResponse.isMyCart
                            }
                            Log.e("Is My Cart", _isMyCourse.value.toString())
                            Log.e("Response Is My Cart", courseResponse.toString())
                        } else {
                            Log.e("API Error Is My Cart", "Error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<CourseResponse>, t: Throwable) {
                        Log.d("is-my-cart-error", "My Cart Error", t)

                    }
                })
            } catch (e: Exception) {
                Log.d("Logging", "Is My Cart Error", e)

            }
        }
    }

    fun getCountCart(userID:Int){
        viewModelScope.launch {
            val call: Call<CartResponse> = ApiService.apiService.getCountOfCart(userID)
            call.enqueue(object : Callback<CartResponse> {
                override fun onResponse(
                    call: Call<CartResponse>,
                    response: Response<CartResponse>
                ) {
                    if (response.isSuccessful) {
                        val cartResponse = response.body()
                        if (cartResponse != null) {
                            _countOfCart.value=cartResponse.count
                        }
                        Log.e("count-cart-response", cartResponse.toString())

                    } else {
                        Log.e("error-count-cart-response", response.toString())
                    }
                }

                override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                    Log.e("API Error", "Error: ${t.message}")
                    t.printStackTrace()
                }
            })
        }
    }

    fun deleteCart(userID:Int, courseID:Int){
        _isOpenDialog.value=false
        viewModelScope.launch {
            val call: Call<CartResponse> = ApiService.apiService.deleteCart(userID, courseID)
            call.enqueue(object : Callback<CartResponse> {
                override fun onResponse(
                    call: Call<CartResponse>,
                    response: Response<CartResponse>
                ) {
                    if (response.isSuccessful) {
                        val cartResponse = response.body()
                        Log.e("cart-response", cartResponse.toString())
                        if (cartResponse != null) {
                            _messageDeleteState.value=cartResponse.messageRemove
                            fetchCart(userID)
                            getCountCart(userID)
                            _isOpenDialog.value=true
                        }
                        Log.e("messageDeleteState", messageDeleteState.toString())
                    } else {
                        _isOpenDialog.value=true
                        Log.e("delete-cart-log-error", response.toString())
                    }
                }

                override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                    Log.e("API Error", "Error: ${t.message}")
                    t.printStackTrace()
                }
            })
        }
    }

    fun deleteDialog(){
        _isOpenDialog.value=false
    }

    fun addToCart(
        userID:Int,
        courseID:Int,

    ){
        _isOpenDialog.value=false
        viewModelScope.launch {

            val call: Call<CartResponse> = ApiService.apiService.addToCart(userID, courseID)
            call.enqueue(object : Callback<CartResponse> {
                override fun onResponse(
                    call: Call<CartResponse>,
                    response: Response<CartResponse>
                ) {
                    if (response.isSuccessful) {
                        val cartResponse = response.body()
                        Log.e("cart-response", cartResponse.toString())
                        if (cartResponse != null) {
                            _messageAddState.value=cartResponse.messageAddToCart
                            fetchCart(userID)
                            getCountCart(userID)
                            _isOpenDialog.value=true
                        }
                        Log.e("messageAddState", messageDeleteState.toString())
                    } else {
                        _isOpenDialog.value=true
                        Log.e("add-to-cart-log-error", response.toString())
                    }
                }

                override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                    Log.e("API Error", "Error: ${t.message}")
                    t.printStackTrace()
                }
            })
        }
    }

    fun isMyCourse(userID: Int,courseID: Int){
        viewModelScope.launch {
            try {
                ApiService.apiService.isMyCourses(userID,courseID).enqueue(object :
                    Callback<CourseResponse> {
                    override fun onResponse(
                        call: Call<CourseResponse>,
                        response: Response<CourseResponse>
                    ) {
                        if (response.isSuccessful) {
                            val courseResponse = response.body()
                            if (courseResponse != null) {
                                _isMyCourse.value=courseResponse.isMyCourse
                            }
                            Log.e("Is My Course", _isMyCourse.value.toString())
                            Log.e("Response Is My Course", courseResponse.toString())
                        } else {
                            Log.e("API Error Is My Course", "Error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<CourseResponse>, t: Throwable) {
                        Log.d("is-my-courses-error", "My Courses Error", t)

                    }
                })
            } catch (e: Exception) {
                Log.d("Logging", "Is My Courses Error", e)

            }
        }
    }

    fun fetchTest(lessonID: Int) {
        viewModelScope.launch {
            val call: Call<TestResponse> = ApiService.apiService.getTest(lessonID)
            call.enqueue(object : Callback<TestResponse> {
                override fun onResponse(
                    call: Call<TestResponse>,
                    response: Response<TestResponse>
                ) {
                    if (response.isSuccessful) {
                        val courseResponse = response.body()
                        if (courseResponse != null) {
                            _test.value=courseResponse.test
                            _questions.value=courseResponse.questions
                        }
                        Log.e("lesson-test-response", courseResponse.toString())
                        Log.e(
                            "info-test-data-log",
                            _test.value.toString()
                        )
                        Log.e("test-questions", _questions.value.toString())

                    } else {
                        Log.e("lesson-test-log-error", response.toString())
                    }
                }

                override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                    Log.e("API Error", "Error: ${t.message}")
                    t.printStackTrace()
                }
            })
        }

    }


}