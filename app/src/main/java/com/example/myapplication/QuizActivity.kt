package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
        shuffle(quizQuestions)
        val questionsToShow = ArrayList<Question>()
        for (i in quizQuestions.take(5)) {
            questionsToShow.add(i)
        }

        setContent {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(Color(0xFF353a65))
                    .fillMaxSize(1f)) {
                        Text(modifier = Modifier.padding(10.dp), color=Color.White, fontSize = 22.sp,fontWeight = FontWeight.W900, text = "Guess the squad")
                        QuestionRender(questionsToShow[showQuestion.value], questionsToShow.size, resultsIntent) }
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
                val correctOption = ques.answer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(start = 125.dp)
                ) {
                    ImageCard(painter = painter, contentDescription = description)
                }
                HintBox(
                    ques.questionHint,
                )
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
            ScoreCard()
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun HintBox(hint: String,) {
    Column() {
        Box(modifier = Modifier
            .height(30.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFFa54776),
                        Color(0xFFd45dad),
                        Color(0xFFc468da)
                    )
                ),
             shape = CircleShape,
            )
                .clickable { showHint.value = !showHint.value
                    if(showHint.value) {
                        totalScore.value = totalScore.value - 5
                    }
            }){
                if(showHint.value) {
                    Text(text = hint,color=Color.White,
                        textAlign = TextAlign.Center,
                        fontSize=17.sp,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .width(190.dp))
                } else {
                    Text(text = "Hint",
                        color=Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .width(190.dp))
                }
            }
        }

    }

@Composable
fun ScoreCard() {
    Text(text = "Current Score    ",
        color= Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.W700)
    Text(text = totalScore.value.toString(),
        color= Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.W700)
}



@Composable
fun ImageCard(
    painter: Painter,
    contentDescription: String,
) {
    Card (
        modifier = Modifier
            .width(150.dp)
            .height(150.dp),
        shape = RoundedCornerShape(100.dp),
        elevation =  5.dp
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(painter = painter,
                contentDescription = contentDescription,
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
                )
            )

        }
    }
}

@SuppressLint("UnrememberedMutableState")
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
    var selectOption: Int
    val activity = (LocalContext.current as? Activity)
    val correctAnswerPlayer = MediaPlayer.create(context, R.raw.correctanswer)
    val wrongAnswerPlayer = MediaPlayer.create(context, R.raw.wronganswer)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                selectOption = optionId
                selectedAnswer.value = optionId
                if (selectOption == correctOption) {
                    correctAnswerPlayer.start()
                    totalScore.value = totalScore.value + 10
                    resultsIntent.putExtra("score", totalScore.value)
                } else {
                    wrongAnswerPlayer.start()
                }
                if (showQuestion.value >= questionsLength - 1) {
                    context.startActivity(resultsIntent)
                    activity?.finish()
                    showQuestion.value = 0
                    selectedAnswer.value = 0
                    totalScore.value = 0

                }
                else {
                    Handler().postDelayed({
                        showQuestion.value = showQuestion.value + 1
                        showHint.value = false
                        selectedAnswer.value = 0
                    }, 2000)
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
                .background(color = Color.Transparent, shape = CircleShape)
                .clip(CircleShape)
        )
        Text(text = option,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(end = 150.dp),
            style=TextStyle(color= Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.W700)
        )
        if (selectedAnswer.value == correctOption && selectedAnswer.value == optionId) {
            Text(text = "Wohoo",
                color=Color.White,
                fontWeight = FontWeight.W500,
                modifier = Modifier
                    .padding(end=15.dp)
            )
        }
        if (selectedAnswer.value != correctOption && selectedAnswer.value == optionId) {
            Text(text = "Mehh",
                color=Color.White,
                fontWeight = FontWeight.W500,
                modifier = Modifier
                    .padding(end=15.dp)
            )
        }
    }
}

private val showQuestion = mutableStateOf(0)
private val showHint = mutableStateOf(false)
private val totalScore = mutableStateOf(0)
private val selectedAnswer = mutableStateOf(0)


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
        questionImageUrl = R.drawable.piyush,
        answer = 1,
        options = listOf(
            Option(
                id = 1,
                optionText = "Electron",
                optionImageUrl = R.drawable.electron,
            ),
            Option(
                id = 2,
                optionText = "Photon",
                optionImageUrl = R.drawable.photonl,
            ),
            Option(
                id = 3,
                optionText = "Ozone",
                optionImageUrl = R.drawable.ozone,
            ),
            Option(
                id = 4,
                optionText = "Nucleus",
                optionImageUrl = R.drawable.nucleus,
            ),
        )
    ),
    Question(
        id = 3,
        questionHint = "Faheem",
        questionImageUrl = R.drawable.faheem,
        answer = 2,
        options = listOf(
            Option(
                id = 1,
                optionText = "Quantum",
                optionImageUrl = R.drawable.quantum,
            ),
            Option(
                id = 2,
                optionText = "Photon",
                optionImageUrl = R.drawable.photonl,
            ),
            Option(
                id = 3,
                optionText = "Electron",
                optionImageUrl = R.drawable.electron,
            ),
            Option(
                id = 4,
                optionText = "Ozone",
                optionImageUrl = R.drawable.ozone,
            ),
        )
    ),
    Question(
        id = 3,
        questionHint = "Faheem",
        questionImageUrl = R.drawable.faheem,
        answer = 2,
        options = listOf(
            Option(
                id = 1,
                optionText = "Quantum",
                optionImageUrl = R.drawable.quantum,
            ),
            Option(
                id = 2,
                optionText = "Photon",
                optionImageUrl = R.drawable.photonl,
            ),
            Option(
                id = 3,
                optionText = "Electron",
                optionImageUrl = R.drawable.electron,
            ),
            Option(
                id = 4,
                optionText = "Ozone",
                optionImageUrl = R.drawable.ozone,
            ),
        )
    ),
    Question(
        id = 4,
        questionHint = "Gunjit",
        questionImageUrl = R.drawable.gunjit,
        answer = 4,
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
                optionText = "Sigma",
                optionImageUrl = R.drawable.sigma,
            ),
        )
    ),
    Question(
        id = 5,
        questionHint = "Unnati",
        questionImageUrl = R.drawable.unnati,
        answer = 1,
        options = listOf(
            Option(
                id = 1,
                optionText = "Qubit",
                optionImageUrl = R.drawable.qubit,
            ),
            Option(
                id = 2,
                optionText = "Nucleus",
                optionImageUrl = R.drawable.nucleus,
            ),
            Option(
                id = 3,
                optionText = "Quantum",
                optionImageUrl = R.drawable.quantum,
            ),
            Option(
                id = 4,
                optionText = "Electron",
                optionImageUrl = R.drawable.electron,
            ),
        )
    ),
    Question(
        id = 6,
        questionHint = "Abhilash",
        questionImageUrl = R.drawable.abhilash,
        answer = 3,
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
                optionText = "Delta",
                optionImageUrl = R.drawable.delta,
            ),
            Option(
                id = 4,
                optionText = "Quantum",
                optionImageUrl = R.drawable.quantum,
            ),
        )
    ),
    Question(
        id = 7,
        questionHint = "Radhika",
        questionImageUrl = R.drawable.radhika,
        answer = 3,
        options = listOf(
            Option(
                id = 1,
                optionText = "Delta",
                optionImageUrl = R.drawable.delta,
            ),
            Option(
                id = 2,
                optionText = "Qubit",
                optionImageUrl = R.drawable.qubit,
            ),
            Option(
                id = 3,
                optionText = "Momentum",
                optionImageUrl = R.drawable.momentum,
            ),
            Option(
                id = 4,
                optionText = "Ozone",
                optionImageUrl = R.drawable.ozone,
            ),
        )
    ),
    Question(
        id = 8,
        questionHint = "Aditee",
        questionImageUrl = R.drawable.aditee,
        answer = 1,
        options = listOf(
            Option(
                id = 1,
                optionText = "Quantum",
                optionImageUrl = R.drawable.quantum,
            ),
            Option(
                id = 2,
                optionText = "Photon",
                optionImageUrl = R.drawable.photonl,
            ),
            Option(
                id = 3,
                optionText = "Sigma",
                optionImageUrl = R.drawable.sigma,
            ),
            Option(
                id = 4,
                optionText = "Electron",
                optionImageUrl = R.drawable.electron,
            ),
        )
    ),
    Question(
        id = 9,
        questionHint = "Tripati",
        questionImageUrl = R.drawable.tripati,
        answer = 4,
        options = listOf(
            Option(
                id = 1,
                optionText = "Quantum",
                optionImageUrl = R.drawable.quantum,
            ),
            Option(
                id = 2,
                optionText = "Photon",
                optionImageUrl = R.drawable.photonl,
            ),
            Option(
                id = 3,
                optionText = "Electron",
                optionImageUrl = R.drawable.electron,
            ),
            Option(
                id = 4,
                optionText = "Ozone",
                optionImageUrl = R.drawable.ozone,
            ),
        )
    ),
    Question(
        id = 10,
        questionHint = "Kunal",
        questionImageUrl = R.drawable.kunal,
        answer = 2,
        options = listOf(
            Option(
                id = 1,
                optionText = "Qubit",
                optionImageUrl = R.drawable.qubit,
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
                optionText = "Ozone",
                optionImageUrl = R.drawable.ozone,
            ),
        )
    ),
    Question(
        id = 11,
        questionHint = "Anjana",
        questionImageUrl = R.drawable.anjana,
        answer = 1,
        options = listOf(
            Option(
                id = 1,
                optionText = "Qubit",
                optionImageUrl = R.drawable.qubit,
            ),
            Option(
                id = 2,
                optionText = "Nucleus",
                optionImageUrl = R.drawable.nucleus,
            ),
            Option(
                id = 3,
                optionText = "Electron",
                optionImageUrl = R.drawable.electron,
            ),
            Option(
                id = 4,
                optionText = "Momentum",
                optionImageUrl = R.drawable.momentum,
            ),
        )
    ),
    Question(
        id = 12,
        questionHint = "Shivani",
        questionImageUrl = R.drawable.shivani,
        answer = 4,
        options = listOf(
            Option(
                id = 1,
                optionText = "Quantum",
                optionImageUrl = R.drawable.quantum,
            ),
            Option(
                id = 2,
                optionText = "Momentum",
                optionImageUrl = R.drawable.momentum,
            ),
            Option(
                id = 3,
                optionText = "Electron",
                optionImageUrl = R.drawable.electron,
            ),
            Option(
                id = 4,
                optionText = "Sigma",
                optionImageUrl = R.drawable.sigma,
            ),
        )
    ),
    Question(
        id = 13,
        questionHint = "Tushar",
        questionImageUrl = R.drawable.tushar,
        answer = 2,
        options = listOf(
            Option(
                id = 1,
                optionText = "Momentum",
                optionImageUrl = R.drawable.momentum,
            ),
            Option(
                id = 2,
                optionText = "Sigma",
                optionImageUrl = R.drawable.sigma,
            ),
            Option(
                id = 3,
                optionText = "Quantum",
                optionImageUrl = R.drawable.quantum,
            ),
            Option(
                id = 4,
                optionText = "Ozone",
                optionImageUrl = R.drawable.ozone,
            ),
        )
    ),
    Question(
        id = 14,
        questionHint = "Sanchit",
        questionImageUrl = R.drawable.sanchit,
        answer = 3,
        options = listOf(
            Option(
                id = 1,
                optionText = "Quantum",
                optionImageUrl = R.drawable.quantum,
            ),
            Option(
                id = 2,
                optionText = "Photon",
                optionImageUrl = R.drawable.photonl,
            ),
            Option(
                id = 3,
                optionText = "Qubit",
                optionImageUrl = R.drawable.qubit,
            ),
            Option(
                id = 4,
                optionText = "Ozone",
                optionImageUrl = R.drawable.ozone,
            ),
        )
    ),
    Question(
        id = 15,
        questionHint = "Nishant Yadav",
        questionImageUrl = R.drawable.nishantyadav,
        answer = 2,
        options = listOf(
            Option(
                id = 1,
                optionText = "Momentum",
                optionImageUrl = R.drawable.momentum,
            ),
            Option(
                id = 2,
                optionText = "Quantum",
                optionImageUrl = R.drawable.quantum,
            ),
            Option(
                id = 3,
                optionText = "Electron",
                optionImageUrl = R.drawable.electron,
            ),
            Option(
                id = 4,
                optionText = "Ozone",
                optionImageUrl = R.drawable.ozone,
            ),
        )
    ),
    Question(
        id = 16,
        questionHint = "Sharad",
        questionImageUrl = R.drawable.sharad,
        answer = 2,
        options = listOf(
            Option(
                id = 1,
                optionText = "Quantum",
                optionImageUrl = R.drawable.quantum,
            ),
            Option(
                id = 2,
                optionText = "Qubit",
                optionImageUrl = R.drawable.qubit,
            ),
            Option(
                id = 3,
                optionText = "Electron",
                optionImageUrl = R.drawable.electron,
            ),
            Option(
                id = 4,
                optionText = "Ozone",
                optionImageUrl = R.drawable.ozone,
            ),
        )
    ),
)