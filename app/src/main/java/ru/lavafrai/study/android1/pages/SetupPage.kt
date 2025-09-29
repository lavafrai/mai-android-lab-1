package ru.lavafrai.study.android1.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SettingsBackupRestore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import ru.lavafrai.study.android1.models.SignalPhase


@Composable
fun SetupPage(
    openSignalPreview: (amplitude: Float, frequency: Float, phase: SignalPhase) -> Unit,
) {
    // val phasePickerState = rememberPhasePickerState(initialAngle = 0f)
    var signalPhase by remember { mutableStateOf(SignalPhase(0f)) }
    var signalAmplitude by remember { mutableFloatStateOf(12f) }
    var signalFrequency by remember { mutableFloatStateOf(100f) } // in KHz
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(title = { Text(text = "Настройка сигнала") }, scrollBehavior = scrollBehavior)
        },
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            SignalFrequencyPicker(
                value = signalFrequency,
                onValueChange = { signalFrequency = it },
            )
            SignalAmplitudePicker(
                value = signalAmplitude,
                onValueChange = { signalAmplitude = it },
            )
            SignalPhasePicker(
                value = signalPhase,
                onValueChange = { signalPhase = it },
            )
            Button(
                shapes = ButtonDefaults.shapes(),
                onClick = { openSignalPreview(signalAmplitude, signalFrequency * 1000, signalPhase) },
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Text("Просмотр сигнала")
            }
        }
    }
}

@Composable
fun SignalFrequencyPicker(
    value: Float,
    onValueChange: (Float) -> Unit,
) = Card(
    modifier = Modifier
        .fillMaxWidth(),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text("Частота сигнала", style = MaterialTheme.typography.headlineSmall)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 10f..1000f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        Text("Выбранное значение: ${"%.1f".format(value)} КГц")
    }
}

@Composable
fun SignalAmplitudePicker(
    value: Float,
    onValueChange: (Float) -> Unit,
) = Card(
    modifier = Modifier
        .fillMaxWidth(),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text("Амплитуда сигнала", style = MaterialTheme.typography.headlineSmall)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 1f..100f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        Text("Выбранное значение: ${"%.1f".format(value)} В")
    }
}

@Composable
fun SignalPhasePicker(
    value: SignalPhase,
    onValueChange: (SignalPhase) -> Unit,
) = Card(
    modifier = Modifier
        .fillMaxWidth(),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text("Фаза сигнала", style = MaterialTheme.typography.headlineSmall)

        val sliderState = rememberSliderState(
            value = value.degrees,
            valueRange = -180f..180f,
        )
        LaunchedEffect(sliderState.value) {
            onValueChange(SignalPhase(sliderState.value))
        }
        Slider(
            state = sliderState,
            track = { SliderDefaults.CenteredTrack(sliderState = sliderState) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = "Выбранное значение: ${"%.1f".format(sliderState.value)}°",
            )
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 16.dp) {
                IconButton(
                    shapes = IconButtonDefaults.shapes(),
                    onClick = { sliderState.value = 0f },
                    colors = IconButtonDefaults.filledIconButtonColors()
                ) {
                    Icon(Icons.Rounded.SettingsBackupRestore, "revert default value")
                }
            }
        }
    }
}

