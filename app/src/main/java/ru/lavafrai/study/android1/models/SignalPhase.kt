package ru.lavafrai.study.android1.models

import ru.lavafrai.study.android1.ui.PhasePickerState

data class SignalPhase(
    val degrees: Float
) {

}

fun PhasePickerState.asSignalPhase() = SignalPhase(degrees = angleDegrees)
