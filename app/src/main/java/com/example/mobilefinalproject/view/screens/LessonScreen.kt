package com.example.mobilefinalproject.view.screens


import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build

import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.bonus.data.AndroidDownloader
import com.example.mobilefinalproject.model.Document
import com.example.mobilefinalproject.model.Teacher
import com.example.mobilefinalproject.model.Video
import com.example.mobilefinalproject.navigation.NavigationItem
import com.example.mobilefinalproject.view.components.CircularProgressIndicatorEx
import com.example.mobilefinalproject.view.components.MyButton
import com.example.mobilefinalproject.view.components.OutlineMyButton
import com.example.mobilefinalproject.view.components.TopBar
import com.example.mobilefinalproject.viewmodel.CourseViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.delay


import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LessonDetailScreen(
    lessonID: Int,
    courseViewModel: CourseViewModel,
    navController: NavController
) {
    val lesson by courseViewModel.infoLesson.collectAsState()
    val videos by courseViewModel.videos.collectAsState()
    val teacher by courseViewModel.teacher.collectAsState()
    val currentVideoIndexState = remember { mutableStateOf(0) }


    LaunchedEffect(key1 = lessonID) {
        courseViewModel.fetchLesson(lessonID)
    }

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { HomeTabs.entries.size })
    val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }

    lesson?.let { TopBar(navController = navController, title = it.name) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        TabRow(
            selectedTabIndex = selectedTabIndex.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
        ) {
            HomeTabs.entries.forEachIndexed { index, currentTab ->
                Tab(
                    selected = selectedTabIndex.value == index,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.outline,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(currentTab.ordinal)
                        }
                    },
                    text = { Text(
                        text = currentTab.text,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1) },
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (page) {
                HomeTabs.Lesson.ordinal -> lesson?.let {
                    LessonScreen(
                        it.id,
                        LocalLifecycleOwner.current,
                        videos = videos,
                        currentVideoIndexState = currentVideoIndexState,
                        courseViewModel = courseViewModel,
                        onClick = {
                            courseViewModel.updateStatus(lessonID)
                            navController.navigate("${NavigationItem.CourseDetails.route}/${lesson?.course_id}")
                        }

                    )
                }
                HomeTabs.Test.ordinal -> TestScreen(
                    lessonID,
                    navController
                )
                HomeTabs.Resources.ordinal -> ResourcesScreen()
                HomeTabs.Discuss.ordinal -> teacher?.let { DiscussScreen(teacher = it, navController = navController) }
                else -> throw IllegalArgumentException("Unexpected page index: $page")
            }
        }
    }
}

@Composable
fun TestScreen(
    lessonID: Int,
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        MyButton(
            text = "PLAY NOW",
            modifier = Modifier,
            onClick = {
                navController.navigate("${NavigationItem.Test.route}/$lessonID")
            })
    }
}

@Composable
fun LessonScreen(
    lessonID: Int,
    lifecycleOwner: LifecycleOwner,
    videos: List<Video>,
    currentVideoIndexState: MutableState<Int>,
    courseViewModel: CourseViewModel,
    onClick: () -> Unit,
) {


    val currentVideoIndex = currentVideoIndexState.value
    val lesson by courseViewModel.infoLesson.collectAsState()

    LaunchedEffect(key1 = lessonID) {
        courseViewModel.fetchLesson(lessonID)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 30.dp)
    )
    {
        item(key = currentVideoIndex) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp)),
                factory = { context ->
                    YouTubePlayerView(context = context).apply {
                        lifecycleOwner.lifecycle.addObserver(this)

                        addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                val currentVideo = videos.getOrNull(currentVideoIndex)
                                Log.e("currentVideo", currentVideo.toString())
                                val videoId = currentVideo?.let { currentVideo.extractVideoId(it.url) }
                                if (videoId != null) {
                                    youTubePlayer.loadVideo(videoId, 0f)
                                }
                            }
                        })
                    }
                })

            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = videos.getOrNull(currentVideoIndex)?.name ?: "",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight(700),
                letterSpacing = 0.3.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            lesson?.let {
                Text(
                    text = it.description,
                    style = MaterialTheme.typography.bodyMedium,
                    letterSpacing = 0.3.sp
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentVideoIndexState.value > 0) {
                    MyButton(
                        text = "Previous",
                        modifier = Modifier,
                        onClick = {
                            currentVideoIndexState.value = maxOf(currentVideoIndex - 1, 0)
                        }
                    )
                } else {
                    OutlineMyButton(
                        text = "Previous",
                        modifier = Modifier,
                        onClick = {}
                    )
                }
                if (currentVideoIndexState.value < videos.lastIndex) {
                    MyButton(
                        text = "Next",
                        modifier = Modifier,
                        onClick = {
                            currentVideoIndexState.value =
                                minOf(currentVideoIndex + 1, videos.lastIndex)
                        }
                    )
                } else {
                    MyButton(
                        text = "Next Lesson",
                        modifier = Modifier,
                        onClick = onClick,
                    )
                    LaunchedEffect(key1 = Unit) {
                        delay(5000)
                    }
                }
            }
        }
    }
}


@Composable
fun ResourcesScreen(
) {
    val docs = Document.getDoc()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 30.dp)
    ) {
        items(docs) { doc ->
            DocumentDownloadCard(doc)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun DocumentDownloadCard(
    document: Document
) {

    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                val downloader = AndroidDownloader(context)
                downloader.DownloadFile(document.doc_url)
            },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Filled.Build,
                contentDescription = "Download icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = document.doc_name,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${document.memory} MB",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun DiscussScreen(
    teacher: Teacher,
    navController: NavController
) {
    val painter = rememberAsyncImagePainter(
        model = teacher.image_path
    )
    val painterError = rememberAsyncImagePainter(
        model = "https://cdn.sforum.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 26.dp),
        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (painter.state is AsyncImagePainter.State.Error) {
                Image(
                    painter = painterError,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)

                )
            } else {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            teacher.let { it1 ->
                Text(
                    text = it1.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            MyButton(text = "Chat with AI", modifier = Modifier.width(200.dp), onClick = { navController.navigate(NavigationItem.Chat.route)})
        }


    }
}

enum class HomeTabs(
    val text: String
) {
    Lesson(
        text = "Lesson"
    ),
    Test(
        text = "Test"
    ),
    Resources(
        text = "Doc"
    ),
    Discuss(
        text = "Chat"
    )
}






