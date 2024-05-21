package com.example.mobilefinalproject.model

data class SearchResponse(
    val coursesBySearchKey:List<Course>
)
data class SearchRequest(
    val searchKey:String
)

