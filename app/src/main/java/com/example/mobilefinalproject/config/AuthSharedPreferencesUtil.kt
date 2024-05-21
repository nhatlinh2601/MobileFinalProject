package com.example.mobilefinalproject.config

import android.content.Context

object AuthSharedPreferencesUtil {
    private const val PREF_NAME = "AuthPrefs"
    private const val KEY_EMAIL = "email"
    private const val KEY_ID = "id"
    private const val KEY_TOKEN = "token"

    fun saveUserInfo(context: Context,id:String, email: String, token: String) {
        val editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(KEY_ID, id)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_TOKEN, token)
        editor.apply()
    }

    fun getID(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_ID, null)
    }

    fun getEmail(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_EMAIL, null)
    }

    fun getToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_TOKEN, null)
    }
}

