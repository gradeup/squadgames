package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme

class ResultScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var score:Int;
        setContent {

            Column() {
                score = intent.getIntExtra("score", 0)
                Greeting("Wohooo, your score is $score!")
                Reset("Retake Quiz")
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun Reset(name: String) {
//    val context = LocalContext.current
    val activity = (LocalContext.current as? Activity)
    Button(onClick = { activity?.finish() }) {
        Text(text = "$name!")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    MyApplicationTheme {
        Greeting("Android")
    }
}