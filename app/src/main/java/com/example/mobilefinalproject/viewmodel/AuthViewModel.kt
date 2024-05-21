package com.example.mobilefinalproject.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobilefinalproject.config.AuthSharedPreferencesUtil
import com.example.mobilefinalproject.model.Course
import com.example.mobilefinalproject.model.CourseResponse
import com.example.mobilefinalproject.model.LoginRequest
import com.example.mobilefinalproject.model.AuthResponse
import com.example.mobilefinalproject.model.RegisterRequest
import com.example.mobilefinalproject.model.UpdateRequest
import com.example.mobilefinalproject.model.User
import com.example.mobilefinalproject.navigation.NavigationItem
import com.example.mobilefinalproject.service.ApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {

    private val _userCurrent = MutableLiveData<User>(null)
    private val _infoUser = MutableStateFlow<User?>(null)
    private val _token = MutableStateFlow<String?>(null)
    private val _myCourses = MutableStateFlow<List<Course>>(emptyList())
    private val _infoDialog = MutableLiveData(false)
    private val _isUpdateSuccessfully = MutableStateFlow(false)
    private val _isLogin = MutableLiveData(false)
    private val _isLoading = MutableStateFlow<Boolean>(false)
    private val _isErrorRegister = MutableLiveData(false)
    val isErrorRegister = _isErrorRegister.asFlow()
    val infoUser = _infoUser.asStateFlow()
    val token=_token.asStateFlow()
    val userCurrent = _userCurrent.asFlow()
    val myCourse = _myCourses.asStateFlow()
    val infoDialog = _infoDialog.asFlow()
    val isLoading = _isLoading.asStateFlow()
    val isLogin = _isLogin.asFlow()
    val isUpdateSuccessfully = _isUpdateSuccessfully.asStateFlow()
    val isSuccessLoading = mutableStateOf(value = false)
    val imageErrorAuth = mutableStateOf(value = false)
    val progressBar = mutableStateOf(value = false)
    private val loginRequestLiveData = MutableLiveData<Boolean>()


    private var _isDarkMode = MutableStateFlow(false)
    val isDarkMode= _isDarkMode.asStateFlow()


    fun login(email: String, password: String, navController: NavController, context: Context) {
        viewModelScope.launch {
            try {
                progressBar.value = true
                ApiService.apiService.login(LoginRequest(email, password)).enqueue(object :
                    Callback<AuthResponse> {
                    override fun onResponse(
                        call: Call<AuthResponse>,
                        response: Response<AuthResponse>
                    ) {
                        if (response.isSuccessful) {
                            isSuccessLoading.value = true
                            response.body()?.let { data ->
                                _userCurrent.value = data.user
                                _infoDialog.value = false
                                _isLogin.value = true
                                _token.value=data.token
                                val user = User(
                                    _userCurrent.value!!.id,
                                    _userCurrent.value!!.name,
                                    _userCurrent.value!!.email, _userCurrent.value!!.password
                                )
                                AuthSharedPreferencesUtil.saveUserInfo(
                                    context,
                                    _userCurrent.value!!.id.toString(),
                                    _userCurrent.value!!.email,
                                    _token.value!!
                                )
                                Log.e("user-login", user.toString())
//                                navController.currentBackStackEntry?.savedStateHandle?.set("user", user)

                                navController.navigate("${NavigationItem.Home.route}/${_userCurrent.value!!.id}")
                                Log.d("Logging", "Response TokenDto Login: $data")
                            }
                            loginRequestLiveData.postValue(response.isSuccessful)
                            progressBar.value = false
                        } else {
                            response.errorBody()?.let { error ->
                                imageErrorAuth.value = true
                                imageErrorAuth.value = false
                                error.close()
                            }
                            Log.d("Login-error",  response.toString())
                            _infoDialog.value = true
                            _isLogin.value = false
                        }
                    }

                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                        Log.d("Login-error", "Error Authentication", t)
                        _infoDialog.value = true
                    }
                })
            } catch (e: Exception) {
                Log.d("Logging", "Error Authentication", e)
                progressBar.value = false
                _infoDialog.value = true
            }
        }
    }

    fun deleteDialog() {
        _infoDialog.value = false
    }

    fun register(registerRequest: RegisterRequest, navController: NavController) {
        viewModelScope.launch {
            _isErrorRegister.value = false
            try {
                progressBar.value = true
                ApiService.apiService.register(
                    RegisterRequest(
                        registerRequest.name,
                        registerRequest.email,
                        registerRequest.password
                    )
                ).enqueue(object : Callback<AuthResponse> {
                    override fun onResponse(
                        call: Call<AuthResponse>,
                        response: Response<AuthResponse>
                    ) {

                        if (response.isSuccessful) {
                            isSuccessLoading.value = true
                            response.body()?.let { data ->
                                _userCurrent.value = data.user
                                _isLoading.value=true
                                navController.navigate("${NavigationItem.Home.route}/${_userCurrent.value!!.id}")
                                _isLoading.value=false
                                Log.e("register-response", response.toString())
                                Log.d("Logging", "Response TokenDto Register: $data")
                            }
                            loginRequestLiveData.postValue(response.isSuccessful)
                            progressBar.value = false
                        } else {
                            response.errorBody()?.let { error ->
                                imageErrorAuth.value = true
                                imageErrorAuth.value = false
                                error.close()
                                _isErrorRegister.value = true
                            }
                            _isErrorRegister.value = true
                            Log.d("Register-error", response.toString())
                        }
                    }

                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                        Log.d("Register-error", "Error Authentication", t)

                    }
                })
            } catch (e: Exception) {
                Log.d("Logging", "Error Authentication", e)
                progressBar.value = false
                _infoDialog.value = true
            }
        }
    }

    fun update(updateRequest: UpdateRequest, authViewModel: AuthViewModel, navController: NavController) {
        viewModelScope.launch {
            _isErrorRegister.value = false
            try {
                _userCurrent.value?.let {
                    ApiService.apiService.update(
                        it.id,
                        UpdateRequest(
                            updateRequest.name,
                            updateRequest.email,
                            updateRequest.password
                        )
                    ).enqueue(object : Callback<AuthResponse> {
                        override fun onResponse(
                            call: Call<AuthResponse>,
                            response: Response<AuthResponse>
                        ) {
                            Log.e("Request-user",updateRequest.toString())
                            if (response.isSuccessful) {
                                isSuccessLoading.value = true
                                response.body()?.let { data ->
                                    _userCurrent.value = data.user
                                    authViewModel.getUser(_userCurrent.value!!.id)
                                    _isUpdateSuccessfully.value=true

                                    navController.navigate("${NavigationItem.EditProfile.route}/${_userCurrent.value!!.id}")
                                    Log.e("user-update", response.body().toString())
                                    Log.e("user-update", _userCurrent.value.toString())
                                }
                            } else {
                                response.errorBody()?.let { error ->

                                }
                                _isUpdateSuccessfully.value=false
                                Log.d("update-error", response.toString())
                            }
                        }

                        override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                            Log.d("update-error", "Error Authentication", t)

                        }
                    })
                }
            } catch (e: Exception) {
                Log.d("Logging", "Error Authentication", e)
            }
        }
    }

    fun freshDialogUpdateSucces(){
        _isUpdateSuccessfully.value=false
    }

    fun getUser(userID: Int) {

        viewModelScope.launch {
            try {

                ApiService.apiService.getUsers(userID).enqueue(object :
                    Callback<User> {
                    override fun onResponse(
                        call: Call<User>,
                        response: Response<User>
                    ) {
                        if (response.isSuccessful) {
                            val userResponse = response.body()
                            _infoUser.value = userResponse
                        } else {
                            Log.e("API Error", "Error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.d("userInfo-error", "Error User Info", t)
                        _infoDialog.value = true
                    }
                })
            } catch (e: Exception) {
                Log.d("Logging", "Error User Info", e)
                progressBar.value = false
                _infoDialog.value = true
            }
        }
    }

    fun getMyCourse(userID: Int) {

        viewModelScope.launch {
            try {
                ApiService.apiService.getMyCourses(userID).enqueue(object :
                    Callback<CourseResponse> {
                    override fun onResponse(
                        call: Call<CourseResponse>,
                        response: Response<CourseResponse>
                    ) {
                        if (response.isSuccessful) {
                            val userResponse = response.body()
                            if (userResponse != null) {
                                _myCourses.value = userResponse.myCourses
                            }
                        } else {
                            Log.e("API Error My Course", "Error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<CourseResponse>, t: Throwable) {
                        Log.d("my-courses-error", "My Courses Error", t)
                        _infoDialog.value = true
                    }
                })
            } catch (e: Exception) {
                Log.d("Logging", "My Courses Error", e)
                progressBar.value = false
                _infoDialog.value = true
            }
        }
    }

    fun toggleDarkMode(){
        _isDarkMode.value=!_isDarkMode.value
    }



}