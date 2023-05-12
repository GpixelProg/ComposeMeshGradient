package meshGradientAnim

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import meshGradientAnim.trajectory.Trajectory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import meshGradientAnim.trajectory.INTERVAL_90_FPS
import meshGradientAnim.trajectory.moveProcessor
import meshGradientAnim.trajectoryList.trajectory0
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

private val trajectory1 = Trajectory(trajectory = trajectory0)

@Composable
fun ShapeAnimation(
    modifier: Modifier,
    speedAnimation: Float,
) {
    var canvasWidth by remember { mutableStateOf(0f) }
    var canvasHeight by remember { mutableStateOf(0f) }

    var move by remember { mutableStateOf(arrayOf(0f, 0f)) }
    var move1 by remember { mutableStateOf(arrayOf(0f, 0f)) }
    var move2 by remember { mutableStateOf(arrayOf(0f, 0f)) }
    var rotate by remember { mutableStateOf(0f) }

    trajectory1.setParam(
        originalWidth = 1920f,
        originalHeight = 1080f,
        speedAnimation = 0.9f
    )

    /**
     * Animated Color
     */
    val isColor = remember { mutableStateListOf(0, 0, 0) }

    val animatedColor: Color by animateColorAsState(
        targetValue = when(isColor[0]) {
            0 -> Color.Red
            1 -> Color.Green
            2 -> Color.Blue
            3 -> Color.White
            else -> Color.Black
        },
        animationSpec = tween(4500)
    )

    val animatedColor1: Color by animateColorAsState(
        targetValue = when(isColor[1]) {
            0 -> Color.Red
            1 -> Color.Green
            2 -> Color.Blue
            3 -> Color.White
            else -> Color.Black
        },
        animationSpec = tween(4500)
    )

    val animatedColor2: Color by animateColorAsState(
        targetValue = when(isColor[2]) {
            0 -> Color.Red
            1 -> Color.Green
            2 -> Color.Blue
            3 -> Color.White
            else -> Color.Black
        },
        animationSpec = tween(4500)
    )

    /**
     * Rotate
     */
    var rotated by remember { mutableStateOf(false) }

    val angle by animateFloatAsState(
        targetValue = if (rotated) 360f else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
    )

    rotate = angle

    LaunchedEffect(1) {
        launch {
            while (true) {
                rotated = !rotated
                for (i in 0 until 3)
                    isColor[i] = Random.nextInt(from = 0, until = 3)
                delay(2000)
            }
        }

        val posX = -250f
        val posY = -200f

        launch {
            moveProcessor(
                trajectory = trajectory1,
                start = 1,
                speedAnimation = speedAnimation,
                positionX = posX,
                positionY = posY,
                move = { move = it },
                interval = INTERVAL_90_FPS,
            )
        }

        launch {
            moveProcessor(
                trajectory = trajectory1,
                start = 3,
                speedAnimation = speedAnimation,
                positionX = posX,
                positionY = posY,
                move = { move1 = it },
                interval = INTERVAL_90_FPS,
            )
        }

        launch {
            moveProcessor(
                trajectory = trajectory1,
                start = 5,
                speedAnimation = speedAnimation,
                positionX = posX,
                positionY = posY,
                move = { move2 = it },
                interval = INTERVAL_90_FPS,
            )
        }

        launch {
//            rotateProcessor { rotate = it }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .blur(50.dp)
            .background(Color.Black)
            .onGloballyPositioned { layoutCoordinates ->
                canvasWidth = layoutCoordinates.size.width.toFloat()
                canvasHeight = layoutCoordinates.size.height.toFloat()
            }
        ) {
            Box(
                modifier = Modifier
                    .absoluteOffset { IntOffset(x = move[0].toInt(), y = move[1].toInt()) }
                    .rotate(rotate),
            ) {
                Icon(
                    painter = painterResource("shape0.svg"),
                    contentDescription = "",
                    modifier = Modifier.size(200.dp),
                    tint = animatedColor
                )
            }

            Box(
                modifier = Modifier
                    .absoluteOffset { IntOffset(x = move1[0].toInt(), y = move1[1].toInt()) }
                    .rotate(rotate),
            ) {
                Icon(
                    painter = painterResource("shape0.svg"),
                    contentDescription = "",
                    modifier = Modifier.size(200.dp),
                    tint = animatedColor1
                )
            }

            Box(
                modifier = Modifier
                    .absoluteOffset { IntOffset(x = move2[0].toInt(), y = move2[1].toInt()) }
                    .rotate(rotate),
            ) {
                Icon(
                    painter = painterResource("shape0.svg"),
                    contentDescription = "",
                    modifier = Modifier.size(200.dp),
                    tint = animatedColor2
                )
            }
        }

        Text(modifier = Modifier.align(Alignment.BottomCenter), text = "Speed: $speedAnimation, Angle: $rotate" +
                "Width: $canvasWidth, Height: $canvasHeight,\n x=${move[0].toInt()} y=${move[1].toInt()}",
            color = Color.White,
            fontSize = 0.sp
        )
    }
}

internal suspend fun rotateProcessor(
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