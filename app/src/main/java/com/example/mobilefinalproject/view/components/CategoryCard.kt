package com.example.mobilefinalproject.view.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.mobilefinalproject.R
import com.example.mobilefinalproject.model.Category
import com.example.mobilefinalproject.model.Course

@SuppressLint("SuspiciousIndentation")
@Composable
fun CategoryCard(
    category: Category,
    onItemClick: () -> Unit,
    width: Int
) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()


    val painter = rememberImagePainter(
        data = category.image_path,
        )
    Column(
        modifier = Modifier
            .wrapContentSize()
            .width(width.dp)
            .clip(shape = RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable(onClick = onItemClick),
    ) {
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f / 1f)
                .clip(shape = RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
fun preveCa(){
    val cate = Category(1,"Front End", "https://www.goskills.com/blobs/bags/176/structured-data-1x1.jpg")
}