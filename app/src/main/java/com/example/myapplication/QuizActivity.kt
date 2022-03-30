package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.util.Collections.shuffle
import android.annotation.SuppressLint
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.foundation.Image
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import android.widget.RadioButton
import android.widget.Toast
import androidx.compose.material.RadioButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlin.random.Random

class QuizActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val resultsIntent = Intent(this, ResultScreen::class.java)
        var questionsList = quizQuestions.toMutableList()
        shuffle(quizQuestions)
        var questionsToShow = ArrayList<Question>()
        for (i in quizQuestions.take(4)) {
            questionsToShow.add(i);
        }
        setContent {
            resultsIntent.putExtra("score", 4)

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(modifier = Modifier.padding(10.dp), text = "Guess the squad")
                QuestionRender(questionsToShow[showQuestion.value], questionsToShow.size, resultsIntent)
            }
        }
    }
}

@Composable
fun QuestionRender(ques: Question, questionsLength: Int, resultsIntent: Intent) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
        val context = LocalContext.current
        val activity = (LocalContext.current as? Activity)
        //Text(text="Hint ${ques.questionHint}!")
        val painter = painterResource(id = ques.questionImageUrl)
        val description = "Android logo"
        val title = "Android"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(start = 135.dp)
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
                SingleChoiceIconQuestion(painter = optionPainter, option = i.optionText)
            }
        }
        if (showQuestion.value >= questionsLength-1) {
//        options.forEach {
//            val optionText = options[x++];
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                SingleChoiceIconQuestion(painter = optionPainter, option = optionText)
//            }
//        }

            Button(onClick = {
                context.startActivity(resultsIntent)
                activity?.finish()
                showQuestion.value = 0
            }) {
                Text(text = "Submit")
            }
        } else {
            Button(onClick = { showQuestion.value = showQuestion.value+1;  }) {
                Text(text = "Next")
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

    val showHint = remember {
        mutableStateOf(false)
    }
    Column() {
        Box(modifier = Modifier
            .clickable { showHint.value = !showHint.value }){
            Text(text = "Click me", modifier = Modifier
                .align(alignment = Alignment.Center)
                .padding(bottom = 20.dp))
            RoundedCornerShape(20.dp)
            if(showHint.value){
                Text(text=hint, modifier = Modifier.padding(top = 20.dp))
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
            .width(100.dp)
            .height(100.dp),
        shape = RoundedCornerShape(100.dp),
        elevation =  5.dp
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(painter = painter, contentDescription = contentDescription, contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(1f))
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
    option: String
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Red)
            .clickable {
                Toast
                    .makeText(
                        context,
                        "Showing Toast",
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
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

        )
        Text(text = option, style=TextStyle(color= Color.White, fontSize = 16.sp))

    }


}

private val showQuestion = mutableStateOf(0)

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
        questionImageUrl = R.drawable.ozone,
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
        questionImageUrl = R.drawable.photonl,
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
        questionImageUrl = R.drawable.quantum,
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
        questionImageUrl = R.drawable.electron,
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
        questionImageUrl = R.drawable.qubit,
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
        questionHint = "Manish",
        //questionImageUrl = "https://ca.slack-edge.com/T029Z234S-U02L9T65A8N-a1f617e965be-192",
        questionImageUrl = R.drawable.sigma,
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