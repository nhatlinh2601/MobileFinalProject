package com.example.mobilefinalproject.viewmodel

import android.annotation.SuppressLint
import android.util.Log

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.mobilefinalproject.model.Category
import com.example.mobilefinalproject.model.CategoryResponse
import com.example.mobilefinalproject.model.Course
import com.example.mobilefinalproject.model.CourseResponse
import com.example.mobilefinalproject.service.ApiService
import com.example.mobilefinalproject.service.ApiService.apiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltViewModel
class HomeViewModel : ViewModel() {

    private var _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories= _categories.asStateFlow()

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses= _courses.asStateFlow()

    private val _popularCourses = MutableStateFlow<List<Course>>(emptyList())
    val popularCourses= _popularCourses.asStateFlow()

    private val _recommendCourses = MutableStateFlow<List<Course>>(emptyList())
    val recommendCourses= _recommendCourses.asStateFlow()

    private var _coursesByCate = MutableLiveData<List<Course>>(emptyList())
    val coursesByCate= _coursesByCate.asFlow()


    private var _coursesBySearchKey = MutableStateFlow<List<Course>>(emptyList())
    val coursesBySearchKey= _coursesBySearchKey.asStateFlow()










    init {
        fetchCourses()
        fetchCategories()
    }

    fun fetchCategories() {
        viewModelScope.launch {
            val call: Call<CategoryResponse> = apiService.getCategories()
            call.enqueue(object : Callback<CategoryResponse> {
                override fun onResponse(
                    call: Call<CategoryResponse>,
                    response: Response<CategoryResponse>
                ) {
                    if (response.isSuccessful) {

                        val categoryResponse = response.body()
                        _categories.value = categoryResponse?.categories ?: emptyList()

                        Log.e("Response Category",categoryResponse.toString())


                    } else {
                        Log.e("API Error", "Error: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    Log.e("API Error", "Error: ${t.message}")
                    t.printStackTrace()
                }
            })
        }


    }



    fun fetchCourses() {
        viewModelScope.launch {
                val call: Call<CourseResponse> = ApiService.apiService.getCourses()
                call.enqueue(object : Callback<CourseResponse> {
                    override fun onResponse(
                        call: Call<CourseResponse>,
                        response: Response<CourseResponse>
                    ) {
                        if (response.isSuccessful) {
                            val courseResponse = response.body()
                            _courses.value = courseResponse?.courses ?: emptyList()

                            val responseData: List<Course>? = response.body()?.courses
                            if(responseData != null){
                                _courses.value = responseData
                            }
                            Log.e("Response Course", courseResponse.toString())
                        } else {
                            Log.e("API Error", "Error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<CourseResponse>, t: Throwable) {
                        Log.e("API Error", "Error: ${t.message}")
                        t.printStackTrace()
                    }
                })

        }

    }

    fun fetchPopularCourses() {
        viewModelScope.launch {
            val call: Call<CourseResponse> = ApiService.apiService.getPopularCourses()
            call.enqueue(object : Callback<CourseResponse> {
                override fun onResponse(
                    call: Call<CourseResponse>,
                    response: Response<CourseResponse>
                ) {
                    if (response.isSuccessful) {
                        val courseResponse = response.body()
                        _popularCourses.value = courseResponse?.popularCourses ?: emptyList()

                        val responseData: List<Course>? = response.body()?.popularCourses
                        if(responseData != null){
                            _popularCourses.value = responseData
                        }
                        Log.e("Response Popular Course", courseResponse.toString())
                    } else {
                        Log.e("API Error", "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<CourseResponse>, t: Throwable) {
                    Log.e("API Error", "Error: ${t.message}")
                    t.printStackTrace()
                }
            })

        }

    }
    fun fetchRecommendCourses() {
        viewModelScope.launch {
            val call: Call<CourseResponse> = ApiService.apiService.getRecommendCourses()
            call.enqueue(object : Callback<CourseResponse> {
                override fun onResponse(
                    call: Call<CourseResponse>,
                    response: Response<CourseResponse>
                ) {
                    if (response.isSuccessful) {
                        val courseResponse = response.body()
                        _recommendCourses.value = courseResponse?.popularCourses ?: emptyList()

                        val responseData: List<Course>? = response.body()?.recommendCourses
                        if(responseData != null){
                            _recommendCourses.value = responseData
                        }
                        Log.e("Response Recommend Course", courseResponse.toString())
                    } else {
                        Log.e("API Error", "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<CourseResponse>, t: Throwable) {
                    Log.e("API Error", "Error: ${t.message}")
                    t.printStackTrace()
                }
            })

        }

    }
    fun fetchCoursesByCate(id:Int){
        viewModelScope.launch {
            _coursesByCate.value = emptyList()
            val call: Call<CourseResponse> = apiService.getCoursesByCate(id)
            call.enqueue(object : Callback<CourseResponse> {
                override fun onResponse(
                    call: Call<CourseResponse>,
                    response: Response<CourseResponse>
                ) {
                    if (response.isSuccessful) {
                        val courseResponse = response.body()
                        _coursesByCate.value = courseResponse?.coursesByCate ?: emptyList()
                        Log.e("course-data-log",_coursesByCate.toString())
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

    fun fetchCoursesBySearchKey(searchKey:String) {
        viewModelScope.launch {
            _coursesBySearchKey.value= emptyList()
            val call: Call<CourseResponse> = apiService.getCoursesBySearchKey(searchKey)
            call.enqueue(object : Callback<CourseResponse> {
                override fun onResponse(
                    call: Call<CourseResponse>,
                    response: Response<CourseResponse>
                ) {
                    if (response.isSuccessful) {

                        val courseResponse = response.body()
                        _coursesBySearchKey.value =
                            courseResponse?.coursesBySearchKey ?: emptyList()
                        _searchText.value=""
                        Log.e("Response Course By Search Key", _coursesBySearchKey.toString())


                    } else {
                        _searchText.value=""
                        Log.e("API Error SearchKey", "Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<CourseResponse>, t: Throwable) {
                    Log.e("API Error onFailure", "Error: ${t.message}")
                    t.printStackTrace()
                }
            })
        }

    }




    // search bar

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()


    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    var courses1 = mutableStateOf<List<Course>>(emptyList())

    private val _coursesList = MutableStateFlow(courses1)




    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    @SuppressLint("SuspiciousIndentation")
    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
            onSearchTextChange("")

    }











    }

