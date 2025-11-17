package com.inacapsos.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.inacapsos.app.navigation.AppNavHost
import com.inacapsos.app.ui.theme.InacapSOSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            InacapSOSTheme {
                AppNavHost()
            }
        }
    }
}
