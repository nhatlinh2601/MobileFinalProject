package com.example.mobilefinalproject.view.screens


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobilefinalproject.model.Course
import com.example.mobilefinalproject.navigation.NavigationItem
import com.example.mobilefinalproject.view.components.CircularProgressIndicatorEx
import com.example.mobilefinalproject.view.components.SmallCourseCard
import com.example.mobilefinalproject.view.components.TopBar
import com.example.mobilefinalproject.viewmodel.HomeViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CoursesResultScreen(
    homeViewModel: HomeViewModel,
    navController: NavController,
    searchKey: String?
) {
    val coursesBySearchKey by homeViewModel.coursesBySearchKey.collectAsState()
    LaunchedEffect(key1 = Unit) {
        if (searchKey != null) {
            homeViewModel.fetchCoursesBySearchKey(searchKey)
        }
    }
    Log.e("Courses result:", coursesBySearchKey.toString())

    CourseResult(
        title = "Results for '${searchKey}'",
        listCourses = coursesBySearchKey,
        navController = navController
    )


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseResult(
    title: String,
    listCourses: List<Course>,
    navController: NavController,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(navController = navController, title = title)
        },
    ) {
        LazyColumn(

            contentPadding = PaddingValues(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
            if (listCourses.isEmpty()){
                item {
                    Spacer(modifier = Modifier.height(300.dp))
                }
                item {
                    CircularProgressIndicatorEx()
                }
            } else{
                items(listCourses) { course ->
                    SmallCourseCard(
                        course = course,
                        onItemClick = { navController.navigate("${NavigationItem.CourseOverview.route}/${course.id}") }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
