package com.example.mobilefinalproject.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobilefinalproject.view.components.InfoDialog
import com.example.mobilefinalproject.view.components.MyButton
import com.example.mobilefinalproject.view.components.SmallCourseCard
import com.example.mobilefinalproject.view.components.TopBar
import com.example.mobilefinalproject.viewmodel.CourseViewModel

@Composable
fun CartScreen(
    userID: Int,
    navController: NavController,
    courseViewModel: CourseViewModel
) {
    val courses by courseViewModel.coursesOfCart.collectAsState()
    val messageDelete by courseViewModel.messageDeleteState.collectAsState()
    val isOpenDialog by courseViewModel.isOpenDialog.collectAsState()
    val checkedMap = remember { mutableStateMapOf<Int, Boolean>() }
    var total by remember {
        mutableDoubleStateOf(0.00)
    }


    LaunchedEffect(Unit) {
        courseViewModel.fetchCart(userID)
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar(navController = navController, title = "My Cart")
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),

            ) {
            items(courses) { course ->
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween


                ) {
                    Checkbox(
                        checked = checkedMap[course.id] ?: false,
                        onCheckedChange = { isChecked ->
                            checkedMap[course.id] = isChecked
                        }
                    )
                    Column(
                        modifier = Modifier.width(300.dp)
                    ) {
                        SmallCourseCard(
                            course = course,
                        ) {
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier
                            .size(22.dp)
                            .clickable {
                                courseViewModel.deleteCart(userID, course.id)

                            },
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))


                }
                Spacer(modifier = Modifier.height(12.dp))

            }

        }


        val checkedItems = courses.filter { checkedMap[it.id] == true }
        Log.e("checkedItems", checkedItems.toString())
        total = checkedItems.sumByDouble { it.price }
        Spacer(modifier = Modifier.height(30.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Wrap the Row with a container that has a background and rounded corners
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)) // Rounded corners applied here
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(vertical = 20.dp, horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "$$total",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            MyButton(
                text = "CHECKOUT",
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                },
                roundedCornerShape = 30,
                height = 60,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                containerColor = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onSecondary
            )
        }
        if (isOpenDialog){
            InfoDialog(
                title = "Successfully",
                desc = messageDelete.toString(),
                urlIcon = "https://icons.veryicon.com/png/o/miscellaneous/8atour/submit-successfully.png",
                onDismiss = {
                    courseViewModel.deleteDialog()
                }
            )
        }
    }
}




