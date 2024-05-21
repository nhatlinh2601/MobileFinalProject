package com.example.mobilefinalproject.view.screens

import androidx.annotation.Dimension.Companion.DP
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mobilefinalproject.R
import com.example.mobilefinalproject.model.Course
import com.example.mobilefinalproject.model.Lesson
import com.example.mobilefinalproject.navigation.NavigationItem
import com.example.mobilefinalproject.view.components.CircularProgressIndicatorEx
import com.example.mobilefinalproject.view.components.CourseCard
import com.example.mobilefinalproject.view.components.TopBar
import com.example.mobilefinalproject.viewmodel.CourseViewModel

@Composable
fun CourseDetailScreen (
    courseViewModel: CourseViewModel,
    courseId:Int,
    navController: NavController
)
{

    val lessons by courseViewModel.lessons.collectAsState(initial = emptyList())
    val course by courseViewModel.infoCourse.collectAsState(initial = null)

    LaunchedEffect(Unit) {
        courseViewModel.fetchInfoCourseById(courseId)
    }
    if (lessons.isEmpty() || course==null){
        CircularProgressIndicatorEx()
    }else{
        course?.let { TopBar(navController= navController , title = it.name) }
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 70.dp, end = 16.dp)
        ){
            item{

                Image(
                    painter = rememberAsyncImagePainter(model = course?.image_path),
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(16f / 9f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(20.dp))
                course?.let {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.3.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                course?.let {
                    Text(
                        text = it.detail,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Start,
                        letterSpacing = 0.3.sp
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Lessons",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight(700),
                    letterSpacing = 0.3.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                LessonListCard(lessons = lessons, navController = navController)
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }


}

@Composable
fun LessonListCard(
    lessons: List<Lesson>,
    navController: NavController,
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ){
        lessons.forEach { lesson ->
            LessonCard(lesson = lesson, onClick = { navController.navigate("${NavigationItem.Lesson.route}/${lesson.id}") })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}

@Composable
fun LessonCard(
    lesson: Lesson,
    onClick: () -> Unit
) {

    val progress = if (lesson.status == 1) 1.0f else 0.0f

    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .align(Alignment.CenterHorizontally)){
            Image(
                painter = painterResource(id = randomImageResource()),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .size(80.dp)
                    .aspectRatio(1f / 1f)
                    .align(Alignment.CenterVertically),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier
                .padding(16.dp)
                .weight(1f)) {
                lesson.name.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = lesson.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    progress = progress,
                )
            }
        }

    }
}

fun randomImageResource(): Int {
    val randomNumber = (1..9).random()
    return when (randomNumber) {
        1 -> R.drawable.l1
        2 -> R.drawable.l2
        3 -> R.drawable.l3
        4 -> R.drawable.l4
        5 -> R.drawable.l5
        6 -> R.drawable.l6
        7 -> R.drawable.l7
        8 -> R.drawable.l8
        else -> R.drawable.l9
    }
}

