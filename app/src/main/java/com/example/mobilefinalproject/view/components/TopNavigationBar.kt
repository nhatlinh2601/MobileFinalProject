package com.example.mobilefinalproject.view.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobilefinalproject.navigation.NavigationItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(
    text: String,
    navController: NavController,
    icon: @Composable () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = Color(34,34,34),
        ),
        title = {
            Text(
                text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,


            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            icon()
        },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
@Preview
fun TopAppBarPre() {
    val navController = rememberNavController()
    TopNavigationBar(
        "Quay lai",
        navController = navController,
        icon = {
            Icon(imageVector = Icons.Filled.Menu, contentDescription = "OK")
        }
    )
}