package com.example.mobilefinalproject.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobilefinalproject.model.Question
import com.example.mobilefinalproject.view.components.MyButton
import com.example.mobilefinalproject.view.components.TopBar
import com.example.mobilefinalproject.viewmodel.CourseViewModel

@Composable
fun MultipleChoiceTestScreen(
    navController: NavController,
    courseViewModel: CourseViewModel,
    lessonID: Int,
) {
    val test by courseViewModel.test.collectAsState()
    val questions by courseViewModel.questions.collectAsState()
    LaunchedEffect(key1 = Unit) {
        courseViewModel.fetchTest(lessonID)
    }

    val isSubmitted = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        test?.let { TopBar(navController = navController, title = it.title) }
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {

            LazyColumn (
                contentPadding = PaddingValues(8.dp)
            ) {

                itemsIndexed(questions) { index, question ->
                    Column {
                        QuestionItem(
                            index = index + 1,
                            question = question,
                            isSubmitted = isSubmitted.value
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }
                item {

                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MyButton(
                            text = "SUBMIT" ,
                            modifier = Modifier ,
                            onClick = { isSubmitted.value = true })
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                }

            }

        }




    }
}


@Composable
fun QuestionItem(
    index:Int,
    question: Question,
    isSubmitted: Boolean
){
    val answerList = listOf(
        question.optionA,
        question.optionB,
        question.optionC,
        question.optionD
    )

    var selectedAnswer by remember { mutableStateOf<String?>(null) }

    Column {
        Text(
            text = "Question $index.",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,

            )
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = question.text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            answerList.forEach{answer ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .selectable(
                            selected = answer == selectedAnswer,
                            onClick = { selectedAnswer = answer },
                            role = Role.RadioButton
                        )

                ){
                    RadioButton(
                        selected = answer == selectedAnswer,
                        onClick = { selectedAnswer = answer },
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text(text = answer)
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))
        if (isSubmitted) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Correct Answer: ${question.correctAnswer}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color(16, 158, 47)
            )
        }

    }
}