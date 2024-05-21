package com.example.mobilefinalproject

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.mobilefinalproject.config.AuthSharedPreferencesUtil
import com.example.mobilefinalproject.navigation.AppNavHost
import com.example.mobilefinalproject.navigation.NavigationItem
import com.example.mobilefinalproject.navigation.Screen
import com.example.mobilefinalproject.ui.theme.MobileFinalProjectTheme
import com.example.mobilefinalproject.view.components.BottomNavItem
import com.example.mobilefinalproject.view.components.BottomNavigationBar
import com.example.mobilefinalproject.view.screens.HomeScreen
import com.example.mobilefinalproject.view.screens.auth.LoginContent
import com.example.mobilefinalproject.viewmodel.AuthViewModel
import com.example.mobilefinalproject.viewmodel.ChatViewModel
import com.example.mobilefinalproject.viewmodel.CourseViewModel
import com.example.mobilefinalproject.viewmodel.HomeViewModel

class MainActivity : ComponentActivity(){


    private val homeViewModel: HomeViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private val courseViewModel: CourseViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()

        private val pickVisualMediaLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if(uri != null) {
                chatViewModel.handlePickedImage(uri)
            }
        }




    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            var startDestination by remember {
                mutableStateOf(NavigationItem.Login.route)
            }

            val context = LocalContext.current
            val email = AuthSharedPreferencesUtil.getEmail(context)
            val idString = AuthSharedPreferencesUtil.getID(context)
            val userID = idString?.toInt()
            val token = AuthSharedPreferencesUtil.getToken(context)
            if (email != null && token != null && userID != null) {
                startDestination = "${NavigationItem.Home.route}/${userID}"
            }

            val isDarkMode by authViewModel.isDarkMode.collectAsState()
            MobileFinalProjectTheme (
                darkTheme = isDarkMode
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val userCurrent by authViewModel.userCurrent.collectAsState(null)
                    Log.e("User NavHost", userCurrent.toString())
                    val navController = rememberNavController()
                    val screens = listOf(
                        "${NavigationItem.Home.route}/{userID}",
                        "${NavigationItem.MyCourses.route}/{userID}",
                        "${NavigationItem.Profile.route}/{userID}",
                    )

                    val screensHideChat = listOf(
                        NavigationItem.Chat.route,
                        "${NavigationItem.Profile.route}/{userID}",
                        NavigationItem.Login.route,
                        NavigationItem.Register.route
                    )
                    val currentRoute =
                        navController.currentBackStackEntryAsState().value?.destination?.route

                    val showBottomBar = currentRoute in screens
                    val hideChatIcon = currentRoute in screensHideChat

                    Log.d(
                        "RouteCheck",
                        "Current Route: $currentRoute, Show Bottom Bar: $showBottomBar"
                    )

                    val painter = rememberAsyncImagePainter(
                        model = "https://cdn-icons-png.flaticon.com/512/3621/3621443.png"
                    )


                    Scaffold(
                        bottomBar = {
                            AnimatedVisibility(
                                visible = showBottomBar,
                                enter = fadeIn() + scaleIn(),
                                exit = fadeOut() + scaleOut(),
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.background)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(24.dp))
                                        .shadow(
                                            elevation = 4.dp,
                                            shape = RoundedCornerShape(
                                                topStart = 24.dp,
                                                topEnd = 24.dp
                                            ),
                                            ambientColor = Color.Black
                                        )
                                ) {
                                    BottomNavigationBar(
                                        navController = navController,
                                        items = listOf(
                                            BottomNavItem(
                                                route = "${NavigationItem.Home.route}/${userCurrent?.id}",
                                                name = Screen.HOME.name,
                                                icon = rememberVectorPainter(image = Icons.Default.Home),
                                                label = "Home"
                                            ),

                                            BottomNavItem(
                                                route = "${NavigationItem.MyCourses.route}/${userCurrent?.id}",
                                                name = Screen.MY_COURSES.name,
                                                icon = rememberVectorPainter(image = Icons.Default.PlayArrow),
                                                label = "My Courses"
                                            ),
                                            BottomNavItem(
                                                route = "${NavigationItem.Profile.route}/${userCurrent?.id}",
                                                name = Screen.PROFILE.name,
                                                icon = rememberVectorPainter(
                                                    image = Icons.Default.AccountCircle
                                                ),
                                                label = "Profile"
                                            ),


                                        ),
                                    )
                                }
                            }
                        },

                        floatingActionButton = {
                            AnimatedVisibility(
                                visible = !hideChatIcon,
                                enter = fadeIn() + scaleIn(),
                                exit = fadeOut() + scaleOut(),
                            ){
                                FloatingActionButton(
                                    onClick = {
                                        navController.navigate(NavigationItem.Chat.route)
                                    },
                                    modifier = Modifier
                                        .clip(
                                            shape = CircleShape
                                        )
                                        .padding(12.dp)
                                ) {
                                    Image(
                                        painter = painter,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(50.dp)


                                    )

                                }
                            }

                        }

                    ) {
                        AppNavHost(
                            homeViewModel = homeViewModel,
                            authViewModel = authViewModel,
                            navController = navController,
                            courseViewModel = courseViewModel,
                            chatViewModel = chatViewModel,
                            pickVisualMediaLauncher = pickVisualMediaLauncher,
                            modifier = Modifier.padding(it),
                            startDestination
                        )
                    }

                }

            }
        }
    }
}

