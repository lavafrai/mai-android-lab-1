package ru.lavafrai.study.android1.navigation

import android.content.pm.ActivityInfo
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.lavafrai.study.android1.navigation.util.navTypeOf
import ru.lavafrai.study.android1.pages.SetupPage
import ru.lavafrai.study.android1.utils.LocalAndroidOrientation
import ru.lavafrai.study.android1.viewmodels.MainViewModel
import kotlin.reflect.typeOf


@Composable
fun AppNavHost(
    viewModel: MainViewModel,
    navController: NavHostController = rememberNavController(),
) {
    val orientationConfiguration = LocalAndroidOrientation.current

    NavHost(
        navController,
        startDestination = MainDestination
    ) {
        composable<MainDestination> {
            orientationConfiguration.setOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)
            SetupPage(
                openSignalPreview = { amplitude, frequency, phase ->
                    viewModel.openSignalPreview(
                        amplitude = amplitude,
                        frequency = frequency,
                        phase = phase.degrees,
                    )
                }
            )
        }

        composable<PreviewDestination>(
            typeMap = mapOf(
                typeOf<Double>() to navTypeOf<Double>(),
                typeOf<Float>() to navTypeOf<Float>(),
            )
        ) {
            orientationConfiguration.setOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
            Text("Preview Screen: ${it}")
        }
    }
}