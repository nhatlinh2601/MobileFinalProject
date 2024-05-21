package com.example.mobilefinalproject.service

import kotlinx.coroutines.delay
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private const val BASE_URL = "https://40a5-2001-ee0-4b4b-ad90-904c-bf20-aa82-b643.ngrok-free.app/api/"
    private val retrofit: Retrofit by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val apiService: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}