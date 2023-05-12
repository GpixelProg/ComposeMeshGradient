package meshGradientAnim.trajectory

import kotlinx.coroutines.delay

private var speedAnim = 0f

const val INTERVAL_30_FPS = 33L
const val INTERVAL_60_FPS = 16L
const val INTERVAL_90_FPS = 11L
const val INTERVAL_120_FPS = 8L

internal suspend fun moveProcessor(
    trajectory: Trajectory,
    positionX: Float = 0f,
    positionY: Float = 0f,
    interval: Long = INTERVAL_60_FPS,
    start: Long = 0,
    speedAnimation: Float = 0.0f,
    move: (Array<Float>) -> Unit
) {
    if (speedAnimation < 0.90 && speedAnimation > 0) speedAnim = speedAnimation

    var index: Int = if (start == 0L) 0
    else ((trajectory.getPointsSize() - 1) / start).toInt()

    println("ANIM Index:$index")
    println("POS X:$positionX, Y:$positionY")

    while (true) {
        for (i in index until  trajectory.getPointsSize()) {
            move(trajectory.getPoint(index = i.toLong(), posX = positionX, posY = positionY, isResize = true))
            delay((interval * trajectory.syncSpeed() * (1 - speedAnim)).toLong())
        }
        index = 0
    }
}