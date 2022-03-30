package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class ResultScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var score:Int;
        setContent {
            Column() {
                score = intent.getIntExtra("score", 0)
                val context = LocalContext.current
                val activity = (LocalContext.current as? Activity)
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF353a65)),
                ) {
                    Box(modifier = Modifier
                        .background(Color.White, shape = CircleShape)
                        .height(200.dp)
                        .width(200.dp)
                    )
                    Spacer(modifier = Modifier.height(40.dp))
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
                        .height(60.dp)
                        .width(300.dp)
                        .clickable {
                            context.startActivity(
                                Intent(context, MainActivity::class.java)
                            )
                            activity?.finish()
                        }
                    ){
                        Text("Play Again", Modifier
                            .align(Alignment.Center),
                            color = Color.White,
                            fontSize = 25.sp,
                        )
                    }
                }
            }
        }
    }
}
