package com.example.mobilefinalproject.view.screens.auth

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobilefinalproject.R
import com.example.mobilefinalproject.config.ValidRegex
import com.example.mobilefinalproject.model.RegisterRequest
import com.example.mobilefinalproject.navigation.NavigationItem
import com.example.mobilefinalproject.view.components.InfoDialog
import com.example.mobilefinalproject.view.screens.HomeScreen
import com.example.mobilefinalproject.viewmodel.AuthViewModel
import com.example.mobilefinalproject.viewmodel.HomeViewModel

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    navController: NavController
) {
    var isEmty by remember {
        mutableStateOf(false)
    }
    var isErrorEmailUnique by remember {
        mutableStateOf(false)
    }
    val isErrorRegister by authViewModel.isErrorRegister.collectAsState(false)
    val isLoading by authViewModel.isLoading.collectAsState()


    val keyboardController = LocalSoftwareKeyboardController.current

    var name by rememberSaveable {
        mutableStateOf("")
    }
    var isNameError by rememberSaveable {
        mutableStateOf(false)
    }
    var nameTextError by rememberSaveable {
        mutableStateOf("")
    }

    var email by rememberSaveable {
        mutableStateOf("")
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
        mutableStateOf(false) }

    fun validateEmail(text: String) {
        if (text == ""){
            isEmailError=true;
            emailTextError="Email must be required"
        }else{
            if (!ValidRegex.isEmail(text)){
                isEmailError=true;
                emailTextError="Incorrect email format."
            } else{
                isEmailError=false;
            }
        }

    }
    fun validateName(text: String) {
        if (text == ""){
            isNameError=true;
            nameTextError="Name must be required"
        }else{
            isNameError=false;
        }

    }
    fun validatePassword(text: String) {
        if (text == ""){
            isPasswordError=true;
            passwordTextError="Password must be required"
        }else{
            if (text.length<6){
                isPasswordError=true;
                passwordTextError="Password must have at least 6 characters"
            } else{
                isPasswordError=false;
            }
        }

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 80.dp)
    ) {


        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.login),
                contentDescription = "Login"
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Let's get started!",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Create an account to LiviCode to get all features",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it;
                    validateName(name)
                },

                isError = isNameError,
                singleLine = true,
                supportingText = {
                    if (isNameError) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = nameTextError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                label = { Text(text = "Name") },
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() }),
                modifier = Modifier
                    .fillMaxWidth(0.8f)


            )
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it;
                    validateEmail(email)
                },

                isError = isEmailError,
                singleLine = true,
                supportingText = {
                    if (isEmailError) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = emailTextError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                label = { Text(text = "Email") },
                shape = MaterialTheme.shapes.medium,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() }),
                modifier = Modifier
                    .fillMaxWidth(0.8f)


            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it;
                    validatePassword(password)
                },

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
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                label = { Text(text = "Password") },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth(0.8f)


            )
        }


        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = {
                    if (email == "" || password == "" || name == "") {
                        isEmty = true
                    } else {
                        if (isEmailError || isPasswordError || isNameError) {

                        } else {
                            authViewModel.register(RegisterRequest(name, email, password), navController)

                            Log.e("isErrorRegister", isErrorRegister.toString())
                            if (isErrorRegister) {
                                isErrorEmailUnique = true
                            }
                            Log.e("isErrorEmailUnique", isErrorEmailUnique.toString())
                        }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .background(MaterialTheme.colorScheme.onTertiary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "CREATE",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Login here",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(NavigationItem.Login.route)
                    },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
        if (isEmty) {
            InfoDialog(
                title = "Register validated",
                desc = "Information to be entered cannot be left blank.",
                urlIcon = "",
                onDismiss = {
                    isEmty = false
                }
            )
        }

        if (isErrorEmailUnique) {
            InfoDialog(
                title = "Email error",
                desc = "Email already exists.",
                urlIcon = "",
                onDismiss = {
                    isErrorEmailUnique = false
                }
            )
        }

        if (isLoading){
            val strokeWidth = 5.dp

            CircularProgressIndicator(
                modifier = Modifier
                    .drawBehind {
                        drawCircle(
                            Color.Red,
                            radius = size.width / 2 - strokeWidth.toPx() / 2,
                            style = Stroke(strokeWidth.toPx())
                        )
                    },
                color = Color.LightGray,
                strokeWidth = strokeWidth
            )
        }
    }
}


