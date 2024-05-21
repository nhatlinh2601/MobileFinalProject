package com.example.mobilefinalproject.model

import android.graphics.Bitmap

data class Video(
    val id: Int,
    val lesson_id: Int,
    val name: String,
    val status: Int ,
    val url: String,
    var currentPosition : Float = 0f
){
    fun extractVideoId(url: String): String {
        return when {
            url.contains("youtube.com/watch") -> {
                val parts = url.split("?v=")
                if (parts.size > 1) parts[1].split("&")[0] else ""
            }
            url.contains("youtu.be") -> {
                val parts = url.split("/")
                if (parts.size > 1) parts.last() else ""
            }
            url.contains("youtube.com/embed") -> {
                val parts = url.split("/embed/")
                if (parts.size > 1) parts[1].split("?")[0] else ""
            }
            else -> ""
        }
    }
}

sealed class ChatUiEvent {
    data class UpdatePrompt(val newPrompt: String) : ChatUiEvent()
    data class SendPrompt(
        val prompt: String,
        val bitmap: Bitmap?
    ) : ChatUiEvent()
}
