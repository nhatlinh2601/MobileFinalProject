package com.example.mobilefinalproject.view.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.mobilefinalproject.navigation.NavigationItem
import com.example.mobilefinalproject.view.components.TopBar
import com.example.mobilefinalproject.viewmodel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    userID: Int,
    navController:NavController,
    authViewModel: AuthViewModel
) {
    val infoUser by authViewModel.infoUser.collectAsState()
    LaunchedEffect(Unit) {
        authViewModel.getUser(userID)
    }
    var switchState by remember { mutableStateOf(true) }
    val painter = rememberAsyncImagePainter(
        model = infoUser?.img
    )
    val painterError = rememberAsyncImagePainter(
        model = "https://cdn.sforum.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg"
    )
    Scaffold(
        topBar = {
           TopBar(navController = navController, title = "Profile")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 26.dp),


        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
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
                infoUser?.let { it1 ->
                    Text(
                        text = it1.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

            }

            Spacer(modifier = Modifier.height(20.dp))
            TitleProfile("ACCOUNT")
            Spacer(modifier = Modifier.height(10.dp))
            Divider(
                color = MaterialTheme.colorScheme.onBackground
            )
            ProfileItem(
                icon = Icons.Outlined.AccountCircle,
                title = "Edit Profile",
                trailingIcon = { Icons.Outlined.ArrowForward },
                onClick = {
                    navController.navigate("${NavigationItem.EditProfile.route}/${userID}")
                }
            )
            Divider(
                color = MaterialTheme.colorScheme.onBackground
            )
            ProfileItem(
                icon = Icons.Outlined.Notifications,
                title = "Notifications",
                trailingIcon = { Icons.Outlined.ArrowForward },
                onClick = { /* handle onClick here */ }
            )
            Divider(
                color = MaterialTheme.colorScheme.onBackground
            )
            ProfileItem(
                icon = Icons.Outlined.Build,
                title = "My Courses",
                trailingIcon = { Icons.Outlined.ArrowForward },
                onClick = {
//                    navController.navigate(NavigationItem.Cart.route)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))
            TitleProfile("ACTIONS")
            Spacer(modifier = Modifier.height(10.dp))
            Divider(
                color = MaterialTheme.colorScheme.onBackground
            )
            ProfileItem(
                icon = Icons.Outlined.Lock,
                title = "Logout",
                trailingIcon = { Icons.Outlined.ArrowForward },
                onClick = { /* handle onClick here */ }
            )
            Divider(
                color = MaterialTheme.colorScheme.onBackground
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {

                        })
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Build,
                            contentDescription = "Leading Icon",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )

                        Text(
                            text = "Dark Mode",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))

                        val checked by authViewModel.isDarkMode.collectAsState()

                        Switch(
                            checked = checked,
                            onCheckedChange = {
                                authViewModel.toggleDarkMode()
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                                uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                            )
                        )

                    }

                }
            }
            Divider(
                color = MaterialTheme.colorScheme.onBackground
            )



        }

    }
}


@Composable
fun TitleProfile(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(start = 16.dp, top = 10.dp, bottom = 5.dp),
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun ProfileItem(
    icon: ImageVector,
    color: Color = MaterialTheme.colorScheme.onBackground,
    title: String,
    trailingIcon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Leading Icon",
                    modifier = Modifier.size(24.dp),
                    tint = color
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = color,
                    modifier = Modifier.padding(start = 16.dp)
                )

            }



        }
    }
}




