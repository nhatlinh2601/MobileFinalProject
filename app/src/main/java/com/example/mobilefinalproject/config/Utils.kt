package com.example.mobilefinalproject.config


object ValidRegex {
    private val emailPattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    fun isEmail(value: String): Boolean {
        return emailPattern.matches(value)
    }
}