package com.example.mobilefinalproject.view.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun IconClick(imageVector: ImageVector, onClick: () -> Unit){
    androidx.compose.material3.Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = Modifier.clickable { onClick() }
    )
}