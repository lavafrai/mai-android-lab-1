package ru.lavafrai.study.android1.utils

import android.app.Activity
import androidx.compose.runtime.compositionLocalOf

class AndroidOrientation(
    val orientation: Int,
    val activity: Activity,
) {
    fun setOrientation(orientation: Int) {
        activity.requestedOrientation = orientation
    }
}

val LocalAndroidOrientation = compositionLocalOf<AndroidOrientation> {
    error("No AndroidOrientation provided")
}