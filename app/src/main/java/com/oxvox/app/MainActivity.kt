package com.oxvox.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.oxvox.app.ui.theme.OXVOXTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OXVOXTheme {
                val navController = rememberNavController()
                OXVOXNavGraph(navController = navController)
            }
        }
    }
}
