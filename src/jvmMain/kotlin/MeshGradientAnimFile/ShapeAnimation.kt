package MeshGradientAnim

import MeshGradientAnim.Trajectory.TrajectoryElement
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val INTERVAL_30_FPS = 33L
const val INTERVAL_60_FPS = 16L
const val INTERVAL_90_FPS = 11L
const val INTERVAL_120_FPS = 8L

private val trajectory1 = TrajectoryElement("pointMesh1.txt")
private var speedAnim = 0f

@Composable
fun ShapeAnimation(
    modifier: Modifier,
    speedAnimation: Float = 0.5f,
) {
    if (speedAnimation < 0.93 && speedAnimation > 0) speedAnim = speedAnimation
    var canvasWidth by remember { mutableStateOf(0f) }
    var canvasHeight by remember { mutableStateOf(0f) }

    var move by remember { mutableStateOf(arrayOf(0f, 0f)) }
    var rotate by remember { mutableStateOf(0f) }

    trajectory1.setParam(
        originalWidth = 1920f,
        originalHeight = 1080f,
        resizedWidth = canvasWidth,
        resizedHeight = canvasHeight,
        speedAnimation = 0.9f
    )

    LaunchedEffect(1) {
        launch {
            moveProcessor(
                trajectoryElement = trajectory1,
                move = { move = it },
                interval = INTERVAL_120_FPS,
            )
        }

        launch {
            rotateProcessor { rotate = it }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize().blur(0.dp).background(Color.Black)
            .onGloballyPositioned { layoutCoordinates ->
                canvasWidth = layoutCoordinates.size.width.toFloat()
                canvasHeight = layoutCoordinates.size.height.toFloat()
            }
        ) {

            Box(
                modifier = Modifier.absoluteOffset { IntOffset(x = 300.toInt(), y = 300.toInt()) }.rotate(rotate),
            ) {
                Text(
                    text = "TEXT TEXT",
                    color = Color.White,
                    fontSize = 50.sp
                )
            }
        }

        Text(modifier = Modifier.align(Alignment.BottomCenter), text = "Speed: $speedAnimation, Angle: $rotate" +
                "Width: $canvasWidth, Height: $canvasHeight,\n x=${move[0].toInt()} y=${move[1].toInt()}")
    }
}

private suspend fun moveProcessor(
    trajectoryElement: TrajectoryElement,
    positionX: Float = 0f,
    positionY: Float = 0f,
    interval: Long = INTERVAL_90_FPS,
    move: (Array<Float>) -> Unit
) = coroutineScope {
    launch {
        while (true) {
            for (i in 0 until  trajectoryElement.getPointsSize()) {
                move(trajectoryElement.getPoint(index = i.toLong(), posX = positionX, posY = positionY, isResize = true))
                delay((interval * trajectoryElement.syncSpeed() * (1 - speedAnim)).toLong())
            }
        }
    }
}

private suspend fun rotateProcessor(
    angle: (Float) -> Unit
) = coroutineScope {
    launch {
        var angle = 0f
        while (true) {
            angle += 0.5f
            angle(angle)
            delay(1)
        }
    }
}