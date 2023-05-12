import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import meshGradientAnim.trajectory.Trajectory
import meshGradientAnim.*
import meshGradientAnim.trajectoryList.trajectory0

@Composable
fun BackgroundAnim() {
    val canvasSize = remember { mutableStateListOf<Float>(0f, 0f) }
    val trajectory0 = Trajectory(trajectory = trajectory0)

    trajectory0.setParam(
        originalWidth = 1920f,
        originalHeight = 1080f,
        speedAnimation = 0.0f
    )

    val rotateComponent = RotateComponent(
        rotateFrom = 50f,
        rotateUntil = 0f,
        pullInterval = 5000,
        animationSpec = tween(
            durationMillis = 5000,
            easing = FastOutSlowInEasing
        )
    )

    val colorComponent = ColorComponent(
        colorList = listOf(
            Color.White,
            Color.Red,
            Color.Blue,
            Color.Green
        ),
        animationSpec = tween(4500),
        pullInterval = 4500
    )

    BackgroundMesh(
        background = Color.Black,
        layoutSize = { w, h ->
            canvasSize[0] = w
            canvasSize[1] = h
        },
        blur = 50.dp
    ) {
        Text(
            modifier = Modifier.align(Alignment.BottomCenter),
            text = "W:${canvasSize[0]}, H:${canvasSize[1]}",
            color = Color.White,
            fontSize = 50.sp
        )

        ShapeComponent(
            modifier = Modifier.size(400.dp),
            painter = painterResource("Vector 2.svg"),
            trajectoryMovement = TrajectoryMovement(
                trajectory = trajectory0
            ),
            rotateComponent = rotateComponent,
            colorComponent = null,
            resized = canvasSize
        )
    }
}