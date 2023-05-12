package meshGradientAnim

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.IntOffset
import meshGradientAnim.trajectory.Trajectory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import meshGradientAnim.trajectory.*

data class TrajectoryMovement(
    val trajectory: Trajectory,
    val positionX: Float = 0f,
    val positionY: Float = 0f,
    val startId: Long = 0,
    val moveSpeed: Float = 0f,
    val framerate: Long = INTERVAL_60_FPS,
)

data class RotateComponent(
    val rotateFrom: Float = 0f,
    val rotateUntil: Float = 360f,
    val pullInterval: Int,
    val animationSpec: AnimationSpec<Float>
)

data class ColorComponent(
    val colorList: List<Color>,
    val animationSpec: AnimationSpec<Color>,
    val pullInterval: Long,
)

@Composable
fun ShapeComponent(
    modifier: Modifier = Modifier,
    /** Image/Icon to show */
    painter: Painter,
    /** Movement along the trajectory */
    trajectoryMovement: TrajectoryMovement,
    /** Rotate */
    rotateComponent: RotateComponent,
    /** Color */
    colorComponent: ColorComponent?,
    /** Resized */
    resized: List<Float>
) {
    var move by remember { mutableStateOf(arrayOf(0f, 0f)) }
    var pullRotate by remember { mutableStateOf(false) }
    var colorId by remember { mutableStateOf(0) }

    trajectoryMovement.trajectory.setResized(
        resizedWidth = resized[0],
        resizedHeight = resized[1]
    )

    LaunchedEffect(Unit) {
        launch {
            moveProcessor(
                trajectory = trajectoryMovement.trajectory,
                start = trajectoryMovement.startId,
                speedAnimation = trajectoryMovement.moveSpeed,
                positionX = trajectoryMovement.positionX,
                positionY = trajectoryMovement.positionY,
                interval = trajectoryMovement.framerate,
                move = { move = it }
            )
        }

        launch {
            while (true) {
                pullRotate = !pullRotate
                delay(rotateComponent.pullInterval.toLong())
            }
        }

        launch {
            if (colorComponent != null) {
                while (true) {
                    colorId = if (colorId < colorComponent.colorList.size - 1)
                        colorId + 1
                    else 0
                    delay(colorComponent.pullInterval)
                }
            }
        }
    }

    val angle by animateFloatAsState(
        targetValue = if (pullRotate) rotateComponent.rotateUntil else rotateComponent.rotateFrom,
        animationSpec = rotateComponent.animationSpec
    )

    var animatedColor: Color? = null
    if (colorComponent != null) {
        animatedColor = animateColorAsState(
            targetValue = colorComponent.colorList[colorId],
            animationSpec = colorComponent.animationSpec
        ).value
    }

    Box(
        modifier = Modifier
            .absoluteOffset { IntOffset(move[0].toInt(), move[1].toInt()) }
            .rotate(angle),
    ) {
        if (animatedColor != null) {
            Icon(
                modifier = modifier,
                painter = painter,
                tint = animatedColor,
                contentDescription = null,
            )
        } else {
            Image(
                modifier = modifier,
                painter = painter,
                contentDescription = null
            )
        }
    }
}


