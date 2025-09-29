package ru.lavafrai.study.android1.navigation

import kotlinx.serialization.Serializable

@Serializable
object MainDestination

@Serializable
data class PreviewDestination(
    val amplitude: Float, // in Volts
    val frequency: Float, // in Hz
    val phase: Float, // in degrees
)