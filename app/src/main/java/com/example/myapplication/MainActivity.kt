package com.example.myapplication
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val scaffoldState= rememberScaffoldState()
            var textFieldState by remember {
                mutableStateOf(value="")
            }
            val scope= rememberCoroutineScope()

                // A surface container using the 'background' color from the theme

            Snackbar {

                Scaffold(modifier = Modifier.fillMaxSize(),
                    scaffoldState= scaffoldState
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center, modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF353a65)),
                    ) {
                        Loader()
                        Spacer(modifier = Modifier.height(50.dp))
                        TextField(value = textFieldState ,
                            label = {
                                Text("Enter your Name")
                            },
                            onValueChange = {
                                textFieldState= it
                            },
                            singleLine = true,
                            modifier = Modifier
                                .height(50.dp)
                                .background(Color.White, shape = RoundedCornerShape(50))

                        )
                        Spacer(modifier = Modifier.height(25.dp))
                        Box(modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFFa54776),
                                        Color(0xFFd45dad),
                                        Color(0xFFc468da)
                                    )
                                ), shape = RoundedCornerShape(50)
                            )
                            //.background(Color(0xFFea4c89), shape = RoundedCornerShape(50))
                            .height(50.dp)
                            .width(270.dp)
                            .clickable {
                                if(textFieldState=="")
                                {
                                    Toast
                                        .makeText(
                                            context,
                                            "Please enter your name",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show();
                                }
                                else{
                                    context.startActivity(
                                        Intent(context, QuizActivity::class.java)
                                    )
                                }
                            }
                        ){
                            Text("Start My Quiz", Modifier
                                .align(Alignment.Center),
                                color = Color.White,
                                fontSize = 20.sp,
                            )
                        }
                    }
                }

            }
        }

    }
}


@Composable
fun Welcome(name: String) {
    val context = LocalContext.current

    Text(text = "Hello $name!", Modifier.clickable {
        context.startActivity(Intent(context, QuizActivity::class.java))
    })
}

@Composable
private fun Loader(){
    val compositionResult: LottieCompositionResult =
        rememberLottieComposition(LottieCompositionSpec.Asset("lottie/start.json"))
    val progress by animateLottieCompositionAsState(
        compositionResult.value,
        isPlaying = true,
        iterations = LottieConstants.IterateForever,
        speed = 1.0f
    )
    LottieAnimation(compositionResult.value,progress,
        modifier = Modifier
            .height(300.dp)
            .width(270.dp)
    )
}

