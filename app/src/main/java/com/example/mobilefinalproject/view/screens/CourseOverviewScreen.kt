package com.example.mobilefinalproject.view.screens


import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


import coil.compose.rememberAsyncImagePainter
import com.example.mobilefinalproject.config.CONSTANT
import com.example.mobilefinalproject.model.Lesson
import com.example.mobilefinalproject.navigation.NavigationItem
import com.example.mobilefinalproject.view.components.CircularProgressIndicatorEx
import com.example.mobilefinalproject.view.components.InfoDialog
import com.example.mobilefinalproject.view.components.MyButton


import com.example.mobilefinalproject.view.components.TopBar
import com.example.mobilefinalproject.viewmodel.CourseViewModel

@Composable
fun CourseOverviewScreen(
    courseViewModel: CourseViewModel,
    courseId: Int,
    userID:Int,
    navController: NavController
) {
    val isOpenDialog by courseViewModel.isOpenDialog.collectAsState()
    val lessons by courseViewModel.lessons.collectAsState(initial = emptyList())
    val infoCourse by courseViewModel.infoCourse.collectAsState(initial = null)
    val isMyCourse by courseViewModel.isMyCourse.collectAsState()
    val isMyCart by courseViewModel.isMyCart.collectAsState()

    var openExistedCart by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        courseViewModel.fetchInfoCourseById(courseId)
        courseViewModel.isMyCourse(userID,courseId)
        courseViewModel.isMyCart(userID,courseId)
    }
    if (infoCourse==null){
        CircularProgressIndicatorEx()
    } else{
        infoCourse?.let { TopBar(navController = navController, title = it.name) }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 70.dp, end = 16.dp, bottom = 16.dp)
        ) {
            item {
                Image(
                    painter = rememberAsyncImagePainter(model = infoCourse?.image_path),
                    contentDescription = null,
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "About the course",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight(700),
                    letterSpacing = 0.3.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                infoCourse?.let {
                    Text(
                        text = it.detail,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Start,
                        letterSpacing = 0.3.sp
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))




                Text(
                    text = "Lessons",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight(700),
                    letterSpacing = 0.3.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                infoCourse?.let { LessonExpandableList(
                    lessons,
                    navController = navController,
                    isMyCourse
                ) }
                Spacer(modifier = Modifier.height(30.dp))
                if (isMyCourse){
                    MyButton(
                        text = "LEARN NOW!" ,
                        modifier = Modifier.fillMaxWidth() ,
                        onClick = {
                            navController.navigate("${NavigationItem.CourseDetails.route}/${infoCourse?.id}")
                        },
                        roundedCornerShape = 4 ,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(50.dp))
                }else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,

                        ) {
                        MyButton(
                            text = "BUY NOW!" ,
                            modifier = Modifier ,
                            onClick = {
                                navController.navigate("${NavigationItem.CourseDetails.route}/${infoCourse?.id}")
                            },

                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        MyButton(
                            text = "ADD TO CART!" ,
                            modifier = Modifier,
                            onClick = {
                                if (isMyCart){
                                    openExistedCart=true
                                } else{
                                    courseViewModel.addToCart(userID, courseId)
                                }

                            },

                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                }

            }
        }
        if (isOpenDialog) {
            InfoDialog(
                title = "Successfully" ,
                desc =  "Add to cart successfully" ,
                onDismiss = {
                    courseViewModel.deleteDialog()
                },
                urlIcon = CONSTANT.APP.SUCCESS_ICON_URL
            )

        }
        if (openExistedCart) {
            InfoDialog(
                title = "Existed" ,
                desc =  "This course has been already in your cart" ,
                onDismiss = {
                    openExistedCart=false
                },

                )

        }
    }


}

@Composable
fun LessonExpandableList(
    lessons: List<Lesson>,
    navController: NavController,
    isMyCourse: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()

            .border(0.5.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(10.dp))
    ) {
        lessons.forEach { lesson ->
            LessonCardExpandable(
                lesson = lesson,
                navController = navController,
                isMyCourse = isMyCourse
            )
        }
    }
}

@Composable
fun LessonCardExpandable(
    lesson: Lesson,
    navController: NavController,
    descriptionMaxLines: Int = 2,
    isMyCourse:Boolean
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = Shapes().medium,
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.background
        ),
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(6f),
                    text = lesson.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground
                )
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            if (expandedState) {
                Text(
                    text = lesson.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .clickable {
                            if (isMyCourse){
                                navController.navigate("${NavigationItem.Lesson.route}/${lesson.id}") }
                            }


                        .fillMaxWidth(),
                    maxLines = descriptionMaxLines,
                    overflow = TextOverflow.Ellipsis,

                )
            }
        }
    }
}



