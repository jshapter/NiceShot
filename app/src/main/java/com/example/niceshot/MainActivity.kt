package com.example.niceshot

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import com.example.niceshot.ui.Navigation
import com.example.niceshot.ui.theme.NiceShotTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NiceShotTheme {
                Surface{
                    Navigation()
                }
            }
        }
    }
}