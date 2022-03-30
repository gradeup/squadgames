package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Collections.shuffle

class QuizActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val resultsIntent = Intent(this, ResultScreen::class.java)
        var questionsList = quizQuestions.toMutableList()
        shuffle(quizQuestions)
        var questionsToShow = ArrayList<Question>()
        for (i in quizQuestions.take(5)) {
            questionsToShow.add(i);
        }
        setContent {


            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(Color(0xFF353a65))
                .fillMaxSize(1f)) {
                Text(modifier = Modifier.padding(10.dp), color=Color.White, fontSize = 22.sp,fontWeight = FontWeight.W900, text = "Guess the squad")
                QuestionRender(questionsToShow[showQuestion.value], questionsToShow.size, resultsIntent) }
                Text(modifier = Modifier.padding(10.dp), color=Color.White, fontSize = 22.sp,fontWeight = FontWeight.W900, text = "${totalScore.value}")
                resultsIntent.putExtra("score", totalScore.value)
        }
    }
}

@Composable
fun QuestionRender(ques: Question, questionsLength: Int, resultsIntent: Intent) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
        val painter = painterResource(id = ques.questionImageUrl)
        val description = "Android logo"
        val title = "Android"
        val correctOption = ques.answer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(start = 125.dp)
        ) {
            ImageCard(painter = painter, contentDescription = description, title = title)
        }
        HintBox(
            ques.questionHint,
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
        )
        //var x = 0;
        for(i in ques.options) {
            val optionPainter = painterResource(id = i.optionImageUrl)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                SingleChoiceIconQuestion(painter = optionPainter, option = i.optionText, correctOption = correctOption, optionId=i.id, resultsIntent = resultsIntent, questionsLength = questionsLength)
            }
        }
    }
}

@Composable
fun Submit(i: Intent) {
    val context = LocalContext.current

    Button(onClick = { context.startActivity(i)
    }) {
        Text(text = "View Score")
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun HintBox(hint: String, modifier: Modifier) {
    Column() {
        Box(modifier = Modifier
            .clickable { showHint.value = !showHint.value; totalScore.value = totalScore.value - 5;  }){
            if(showHint.value) {
                Text(text = hint,color=Color.White, textAlign = TextAlign.Center, fontSize=17.sp, fontWeight = FontWeight.W500, modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .width(190.dp))
            } else {
                Text(text = "Hint",color=Color.White,textAlign = TextAlign.Center, fontSize = 17.sp, fontWeight = FontWeight.W500, modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .width(190.dp))
//                    .padding(bottom = 20.dp)
//                    .padding(5.dp)
//                    .padding(start = 5.dp, end = 5.dp))
            }

        }
    }

}


@Composable
fun ImageCard(
    painter: Painter,
    contentDescription: String,
    title: String,
    modifier: Modifier = Modifier
) {
    Card (
        modifier = Modifier
            .width(150.dp)
            .height(150.dp),
        shape = RoundedCornerShape(100.dp),
        elevation =  5.dp
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(painter = painter, contentDescription = contentDescription,
                 contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .background(color = Color.White, shape = CircleShape))
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                        startY = 300f
                    )
                ))

        }
    }
}
@Composable
fun SingleChoiceIconQuestion(
    painter: Painter,
    option: String,
    optionId: Int,
    correctOption: Int,
    resultsIntent: Intent,
    questionsLength: Int,
) {
    val context = LocalContext.current
    var selectOption = 0
    var disableoption = true
    val activity = (LocalContext.current as? Activity)

    Row(
        modifier = Modifier
            .fillMaxWidth()
//            .height(70.dp)
            .clickable {

                selectOption = optionId
                if (selectOption == correctOption) {
                    totalScore.value = totalScore.value + 10
                    Toast
                        .makeText(
                            context,
                            "Correct Answer",
                            Toast.LENGTH_SHORT
                        )
                        .show();
                } else {
                    Toast
                        .makeText(
                            context,
                            "Wrong Answer",
                            Toast.LENGTH_SHORT
                        )
                        .show();
                }
                if (showQuestion.value >= questionsLength - 1) {


                    context.startActivity(resultsIntent)
                    activity?.finish()
                    showQuestion.value = 0

                }
//                } else {
//                    showQuestion.value = showQuestion.value+1; showHint.value = false }
                else {
                    showQuestion.value = showQuestion.value + 1;
                    showHint.value = false
                }

            }


            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFFa54776),
                        Color(0xFFd45dad),
                        Color(0xFFc468da)
                    )
                ),

                RoundedCornerShape(50.dp)
            )
            .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .width(56.dp)
                .height(56.dp)
                //.fillMaxHeight(1f)
                .background(color = Color.Transparent, shape = CircleShape)
                .clip(CircleShape),
        )
        Text(text = option, textAlign = TextAlign.Center, modifier = Modifier.padding(end = 150.dp), style=TextStyle(color= Color.White, fontSize = 16.sp))

    }


}

private val showQuestion = mutableStateOf(0)
private val showHint = mutableStateOf(false)
private val totalScore = mutableStateOf(0)

data class Option(
    val id: Int,
    val optionText: String,
    val optionImageUrl: Int,
)

data class Question(
    val id: Int,
    val questionHint: String,
    val questionImageUrl: Int,
    val answer: Int,
    val options: List<Option> = emptyList(),
)

private val quizQuestions = listOf(
    Question(
        id = 1,
        questionHint = "Prashant",
        //questionImageUrl =  "https://ca.slack-edge.com/T029Z234S-U0100FH3KA4-31f759f82b38-192",
        questionImageUrl = R.drawable.pc,
        answer = 2,
        options = listOf(
            Option(
                id = 1,
                optionText = "Ozone",
                optionImageUrl = R.drawable.ozone,
            ),
            Option(
                id = 2,
                optionText = "Photon",
                optionImageUrl = R.drawable.photonl,
            ),
            Option(
                id = 3,
                optionText = "Nucleus",
                optionImageUrl = R.drawable.nucleus,
            ),
            Option(
                id = 4,
                optionText = "Quantum",
                optionImageUrl = R.drawable.quantum,
            ),
        )
    ),
    Question(
        id = 2,
        questionHint = "Piyush",
        //questionImageUrl = "https://ca.slack-edge.com/T029Z234S-U010026KRK6-8e13318c2565-192",
        questionImageUrl = R.drawable.piyush,
        answer = 2,
        options = listOf(
            Option(
                id = 1,
                optionText = "Ozone",
                optionImageUrl = R.drawable.ozone,
            ),
            Option(
                id = 2,
                optionText = "Photon",
                optionImageUrl = R.drawable.photonl,
            ),
            Option(
                id = 3,
                optionText = "Nucleus",
                optionImageUrl = R.drawable.nucleus,
            ),
            Option(
                id = 4,
                optionText = "Quantum",
                optionImageUrl = R.drawable.quantum,
            ),
        )
    ),
    Question(
        id = 3,
        questionHint = "Faheem",
        //questionImageUrl = "https://ca.slack-edge.com/T029Z234S-U3F1490EP-0df8620cc5d9-192",
        questionImageUrl = R.drawable.faheem,
        answer = 2,
        options = listOf(
            Option(
                id = 1,
                optionText = "Ozone",
                optionImageUrl = R.drawable.ozone,
            ),
            Option(
                id = 2,
                optionText = "Photon",
                optionImageUrl = R.drawable.photonl,
            ),
            Option(
                id = 3,
                optionText = "Nucleus",
                optionImageUrl = R.drawable.nucleus,
            ),
            Option(
                id = 4,
                optionText = "Quantum",
                optionImageUrl = R.drawable.quantum,
            ),
        )
    ),
    Question(
        id = 4,
        questionHint = "Gunjit",
        //questionImageUrl = "https://ca.slack-edge.com/T029Z234S-U0102MQ3X9C-3c019c024a3f-192",
        questionImageUrl = R.drawable.gunjit,
        answer = 2,
        options = listOf(
            Option(
                id = 1,
                optionText = "Ozone",
                optionImageUrl = R.drawable.ozone,
            ),
            Option(
                id = 2,
                optionText = "Photon",
                optionImageUrl = R.drawable.photonl,
            ),
            Option(
                id = 3,
                optionText = "Nucleus",
                optionImageUrl = R.drawable.nucleus,
            ),
            Option(
                id = 4,
                optionText = "Quantum",
                optionImageUrl = R.drawable.quantum,
            ),
        )
    ),
    Question(
        id = 5,
        questionHint = "Unnati",
        //questionImageUrl = "https://ca.slack-edge.com/T029Z234S-UVDV3AYJY-f78c2d308997-192",
        questionImageUrl = R.drawable.unnati,
        answer = 2,
        options = listOf(
            Option(
                id = 1,
                optionText = "Ozone",
                optionImageUrl = R.drawable.ozone,
            ),
            Option(
                id = 2,
                optionText = "Photon",
                optionImageUrl = R.drawable.photonl,
            ),
            Option(
                id = 3,
                optionText = "Nucleus",
                optionImageUrl = R.drawable.nucleus,
            ),
            Option(
                id = 4,
                optionText = "Quantum",
                optionImageUrl = R.drawable.quantum,
            ),
        )
    ),
    Question(
        id = 6,
        questionHint = "Abhilash",
        //questionImageUrl = "https://ca.slack-edge.com/T029Z234S-U02L9T65A8N-a1f617e965be-192",
        questionImageUrl = R.drawable.abhilash,
        answer = 2,
        options = listOf(
            Option(
                id = 1,
                optionText = "Ozone",
                optionImageUrl = R.drawable.ozone,
            ),
            Option(
                id = 2,
                optionText = "Photon",
                optionImageUrl = R.drawable.photonl,
            ),
            Option(
                id = 3,
                optionText = "Nucleus",
                optionImageUrl = R.drawable.nucleus,
            ),
            Option(
                id = 4,
                optionText = "Quantum",
                optionImageUrl = R.drawable.quantum,
            ),
        )
    ),
)