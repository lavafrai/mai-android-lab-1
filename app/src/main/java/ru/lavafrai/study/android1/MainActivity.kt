package ru.lavafrai.study.android1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import ru.lavafrai.study.android1.navigation.AppNavHost
import ru.lavafrai.study.android1.ui.theme.Android1Theme
import ru.lavafrai.study.android1.utils.AndroidOrientation
import ru.lavafrai.study.android1.utils.LocalAndroidOrientation
import ru.lavafrai.study.android1.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val orientationManager = AndroidOrientation(
            orientation = requestedOrientation,
            activity = this
        )
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(
                LocalAndroidOrientation provides orientationManager
            ) {
                Android1Theme {
                    Android1App()
                }
            }
        }
    }
}

@Composable
fun Android1App() {
    val navController = rememberNavController()
    val mainViewModel = viewModel<MainViewModel> { MainViewModel(navController) }
    AppNavHost(
        viewModel = mainViewModel,
        navController = navController,
    )
}