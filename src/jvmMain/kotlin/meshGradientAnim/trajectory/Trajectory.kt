package meshGradientAnim.trajectory

class Trajectory(private val trajectory: List<Float>) {
    private var speedAnimation: Float = 0f
    private var pointsSize = 0

    object ResizeValues {
        var resizedWidth: Float? = null
        var originalWidth: Float? = null
        var resizedHeight: Float? = null
        var originalHeight: Float? = null
    }

    init {
        pointsSize = getPointsSize()
    }

    /**
     * startPoint * 2 = index
     */
    private fun readPoint(id: Int) : List<Float> {
        return listOf<Float>(
            trajectory[id * 2],
            trajectory[id * 2 + 1]
        )
    }

    private fun getResizedPoints(point: Array<Float>) : Array<Float> {
        return arrayOf(
            point[0] * (ResizeValues.resizedWidth!! / ResizeValues.originalWidth!!),
            point[1] * (ResizeValues.resizedHeight!! / ResizeValues.originalHeight!!)
        )
    }

    fun setResized(
        resizedWidth: Float,
        resizedHeight: Float,
    ) {
        /**
         * Updated canvas size
         */
        ResizeValues.resizedWidth = resizedWidth
        ResizeValues.resizedHeight = resizedHeight

    }

    fun setParam(
        originalWidth: Float,
        originalHeight: Float,
        speedAnimation: Float = 0f,
    ) {
        /**
         * Size of project canvas
         */
        ResizeValues.originalWidth = originalWidth
        ResizeValues.originalHeight = originalHeight

        /**
         * Speed Animation 0.0 - 1.0
         */
        this.speedAnimation = speedAnimation
    }

    fun syncSpeed() : Float {
        /** (1- speedAnimation) - inverting value, min speed 0.0 - max speed 1.0 */
        return (ResizeValues.resizedWidth!! / ResizeValues.originalWidth!!) + 1
    }

    fun getPointsSize() : Int {
        return if ((trajectory.size % 2) > 0) {
            println("ERROR: Trajectory - not enough points")
            0
        } else trajectory.size / 2
    }

    /**
     * Get pointPosition by index,
     * first call getPointsSize() return count of points
     *
     * Return Array<Float> x - [0], y - [1]
     */
    fun getPoint(index: Long, posX: Float = 0f, posY: Float = 0f, isResize: Boolean = false) : Array<Float> {
        if (index.toInt() < getPointsSize()) {
            val point = readPoint(index.toInt())
            return if (isResize)
                getResizedPoints(arrayOf(point[0] + posX, point[1] + posY))
            else
                arrayOf(point[0] + posX, point[1] + posY)
        }
        println("ERROR: GetPoint index bigger Trajectory Element file, Max index: ${getPointsSize() - 1}")
        return arrayOf(0f + posX, 0f + posY)
    }

    /**
     * Get pointPosition by percent 0 - 100%
     * send value 0.0f - 100.0f
     *
     * Return Array<Float> x - [0], y - [1]
     */
    fun getPoint(percent: Float, posX: Float = 0f, posY: Float = 0f) : Array<Float> {
        /** percent to point */
        val index: Int = ((getPointsSize() - 1) / percent).toInt()

        if (index < getPointsSize()) {
            val point = readPoint(index)
            return arrayOf(point[0] + posX, point[1] + posY)
        }
        println("ERROR: GetPoint index bigger Trajectory Element file, Max index: ${getPointsSize() - 1}")
        return arrayOf(0f + posX, 0f + posY)
    }

    /**
     * Get pointPosition by step 0-100 Int
     * send value 0 - 100
     *
     * Return Array<Float> x - [0], y - [1]
     */
    fun getPoint(step: Int, posX: Float = 0f, posY: Float = 0f) : Array<Float> {
        /** value to point */
        val index: Int = ((getPointsSize() - 1) / step)

        if (index < getPointsSize()) {
            val point = readPoint(index)
            return arrayOf(point[0] + posX, point[1] + posY)
        }
        println("ERROR: GetPoint index bigger Trajectory Element file, Max index: ${getPointsSize() - 1}")
        return arrayOf(0f + posX, 0f + posY)
    }
}