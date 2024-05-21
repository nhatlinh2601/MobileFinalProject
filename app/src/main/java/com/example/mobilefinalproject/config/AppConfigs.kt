package com.example.mobilefinalproject.config

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class CONSTANT {
    object UI {
        val ROUNDED_INPUT_BUTTON: Dp = 10.dp

        val HEIGHT_INPUT: Dp = 70.dp
        val HEIGHT_BUTTON: Dp = 60.dp

        object TEXT_SIZE {
            val SM__: TextUnit = 7.sp
            val SM_: TextUnit = 10.sp
            val SM: TextUnit = 15.sp
            val MD: TextUnit = 20.sp
            val LG: TextUnit = 25.sp
            val XL: TextUnit = 35.sp
        }
    }
    object APP{
        const val SUCCESS_ICON_URL ="https://www.facebook.com/ron292004?locale=vi_VN"
        const val FAIL_ICON_URL ="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRj-kPuIad0diuFlNw2Pc6pzwILfY9RFBpAbR4AuWfY3Q&s"
        const val DEFAULT_IMAGE_ACCOUNT_URL ="https://www.facebook.com/ron292004?locale=vi_VN"

    }
}