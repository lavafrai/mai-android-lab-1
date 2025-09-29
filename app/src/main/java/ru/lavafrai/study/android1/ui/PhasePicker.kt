package ru.lavafrai.study.android1.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * Класс состояния для PhasePicker.
 *
 * @param initialAngle Начальный угол в градусах.
 */
@Stable
class PhasePickerState(initialAngle: Float = 0f) {
    var angleDegrees by mutableFloatStateOf(initialAngle.coerceIn(0f, 360f))

    companion object {
        /**
         * Saver для сохранения и восстановления состояния при изменениях конфигурации.
         */
        val Saver: Saver<PhasePickerState, Float> = Saver(
            save = { it.angleDegrees },
            restore = { PhasePickerState(it) }
        )
    }
}

/**
 * Создает и запоминает экземпляр [PhasePickerState].
 *
 * @param initialAngle Начальный угол в градусах.
 */
@Composable
fun rememberPhasePickerState(initialAngle: Float = 0f): PhasePickerState {
    return rememberSaveable(saver = PhasePickerState.Saver) {
        PhasePickerState(initialAngle)
    }
}

/**
 * Компонент для выбора фазы сигнала с помощью стрелки на круговом циферблате.
 *
 * @param state Состояние компонента, управляющее текущим углом.
 * @param modifier Модификатор для настройки макета.
 * @param size Размер компонента.
 * @param dialColor Цвет циферблата.
 * @param handColor Цвет стрелки-селектора.
 */
@Composable
fun PhasePicker(
    state: PhasePickerState,
    modifier: Modifier = Modifier,
    size: Dp = 256.dp,
    dialColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    handColor: Color = MaterialTheme.colorScheme.primary
) {
    val density = LocalDensity.current
    val sizePx = with(density) { size.toPx() }
    val radius = sizePx / 2f
    val center = Offset(radius, radius)

    val updateAngle: (Offset) -> Unit = { touchPosition ->
        val dx = touchPosition.x - center.x
        val dy = touchPosition.y - center.y
        val angleRad = atan2(dy, dx)
        var angleDeg = Math.toDegrees(angleRad.toDouble()).toFloat()
        angleDeg = (angleDeg + 90f + 360f) % 360f
        state.angleDegrees = angleDeg
    }

    val textPaint = remember {
        Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = with(density) { 14.sp.toPx() }
            color = Color.White.toArgb()
            textAlign = android.graphics.Paint.Align.CENTER
        }
    }
    val labels = mapOf(0f to "0°", 90f to "90°", 180f to "180°", 270f to "270°")

    Box(
        modifier = modifier
            .size(size)
            .pointerInput(center) {
                detectTapGestures(onTap = { offset -> updateAngle(offset) })
            }
            .pointerInput(center) {
                detectDragGestures(
                    onDrag = { change, _ -> updateAngle(change.position) },
                    onDragStart = { offset -> updateAngle(offset) }
                )
            }
            .wrapContentSize(Alignment.Center)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(color = dialColor, radius = radius, center = center)

            // Draw labels
            drawIntoCanvas { canvas ->
                labels.forEach { (angle, label) ->
                    val angleRad = Math.toRadians(angle.toDouble() - 90.0) // Корректировка для Canvas
                    val labelRadius = radius * 0.8f
                    val x = center.x + labelRadius * cos(angleRad).toFloat()
                    val y = center.y + labelRadius * sin(angleRad).toFloat()
                    canvas.nativeCanvas.drawText(label, x, y - (textPaint.descent() + textPaint.ascent()) / 2, textPaint)
                }
            }

            // Draw pointer
            val angleRad = Math.toRadians(state.angleDegrees.toDouble() - 90.0)
            val handLength = radius * 0.9f
            val handEnd = Offset(
                x = center.x + handLength * cos(angleRad).toFloat(),
                y = center.y + handLength * sin(angleRad).toFloat()
            )

            drawLine(
                color = handColor,
                start = center,
                end = handEnd,
                strokeWidth = 4.dp.toPx()
            )
            // Point in center
            drawCircle(color = handColor, radius = 6.dp.toPx(), center = center)
        }
    }
}