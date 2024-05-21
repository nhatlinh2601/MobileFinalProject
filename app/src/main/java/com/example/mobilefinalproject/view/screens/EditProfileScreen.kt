package com.example.mobilefinalproject.view.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.mobilefinalproject.R
import com.example.mobilefinalproject.config.ValidRegex
import com.example.mobilefinalproject.model.RegisterRequest
import com.example.mobilefinalproject.model.UpdateRequest
import com.example.mobilefinalproject.navigation.NavigationItem
import com.example.mobilefinalproject.view.components.InfoDialog
import com.example.mobilefinalproject.view.components.TopBar
import com.example.mobilefinalproject.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditProfileScreen(
    userID: Int,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val infoUser by authViewModel.infoUser.collectAsState()
    val isUpdateSuccessfully by authViewModel.isUpdateSuccessfully.collectAsState()
    LaunchedEffect(Unit) {
        authViewModel.getUser(userID)
    }
    val painter = rememberAsyncImagePainter(
        model = infoUser?.img
    )
    val painterError = rememberAsyncImagePainter(
        model = "https://cdn.sforum.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg"
    )
    var name by rememberSaveable {
        mutableStateOf(infoUser!!.name)
    }
    var isNameError by rememberSaveable {
        mutableStateOf(false)
    }
    var nameTextError by rememberSaveable {
        mutableStateOf("")
    }
    var email by rememberSaveable {
        mutableStateOf(infoUser!!.email)
    }
    var isEmailError by rememberSaveable {
        mutableStateOf(false)
    }
    var emailTextError by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var isPasswordError by rememberSaveable {
        mutableStateOf(false)
    }
    var passwordTextError by rememberSaveable {
        mutableStateOf("")
    }
    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    fun validatePassword(text: String) {
        if (text == "") {
            isPasswordError = false;
        } else {
            if (text.length < 6) {
                isPasswordError = true;
                passwordTextError = "Password must have at least 6 characters"
            } else {
                isPasswordError = false;
            }
        }

    }

    fun validateEmail(text: String) {
        if (text == "") {
            isEmailError = true;
            emailTextError = "Email must be required"
        } else {
            if (!ValidRegex.isEmail(text)) {
                isEmailError = true;
                emailTextError = "Incorrect email format."
            } else {
                isEmailError = false;
            }
        }

    }

    fun validateName(text: String) {
        if (text == "") {
            isNameError = true;
            nameTextError = "Name must be required"
        } else {
            isNameError = false;
        }

    }



    Scaffold(
        topBar = {
            TopBar(navController = navController, title = "Edit Profile")
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
                            .size(200.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Column {
                    Text(
                        text = "Name",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it;
                            validateName(name)
                        },
                        textStyle = MaterialTheme.typography.titleMedium,
                        isError = isNameError,
                        singleLine = true,
                        supportingText = {
                            if (isNameError) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = nameTextError,
                                    color = Color.Black
                                )
                            }
                        },
                        label = { Text(text = "") },
                        shape = MaterialTheme.shapes.medium,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() }),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)


                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    Text(
                        text = "Name",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it;
                            validateEmail(email)
                        },
                        textStyle = MaterialTheme.typography.titleMedium,
                        isError = isEmailError,
                        singleLine = true,
                        supportingText = {
                            if (isEmailError) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = emailTextError,
                                    color = Color.Black
                                )
                            }
                        },
                        label = { Text(text = "") },
                        shape = MaterialTheme.shapes.medium,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() }),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)


                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    Text(
                        text = "Password - skip if not change",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it;
                            validatePassword(password)
                        },
                        textStyle = MaterialTheme.typography.titleMedium,
                        isError = isPasswordError,
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisible = !passwordVisible
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.hide_password),
                                    contentDescription = null
                                )
                            }
                        },
                        supportingText = {
                            if (isPasswordError) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = passwordTextError,
                                    color = Color.Black
                                )
                            }
                        },
                        label = { Text(text = "") },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)


                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                Column {
                    Button(
                        onClick = {

                            if (isEmailError || isNameError) {

                            } else {
                                authViewModel.update(
                                    UpdateRequest(name, email, password),
                                    authViewModel,
                                    navController
                                )

                                navController.navigate("${NavigationItem.EditProfile.route}/${userID}")
                            }


                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .background(MaterialTheme.colorScheme.onTertiary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "SAVE",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp),
                        )
                    }
                }
            }
        }
    }

    if (isUpdateSuccessfully) {
        InfoDialog(
            title = "Update Successfully",
            desc = "Update Successfully",
            onDismiss = {
                authViewModel.freshDialogUpdateSucces()
            },
            urlIcon = "https://icons.veryicon.com/png/o/miscellaneous/8atour/submit-successfully.png"
        )
    }


}
