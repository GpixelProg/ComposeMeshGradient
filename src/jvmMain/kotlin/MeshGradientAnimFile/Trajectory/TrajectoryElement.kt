package MeshGradientAnimFile.Trajectory

import java.io.File
import kotlin.system.measureNanoTime

class TrajectoryElement(fileTrajectory: String) {
    private val filePath = this.javaClass.classLoader.getResource(fileTrajectory)?.path

    private val pointsX: MutableList<Float> = mutableListOf()
    private val pointsY: MutableList<Float> = mutableListOf()

    private var speedAnimation: Float = 0f

    object ResizeValues {
        var resizedWidth: Float? = null
        var originalWidth: Float? = null
        var resizedHeight: Float? = null
        var originalHeight: Float? = null
    }

    init {
        readPoints()
        getPointsSize()
    }

    private fun readPoints() {
        val time0: Double = measureNanoTime {
            val file = filePath?.let { File(it) }!!
            val pointsList = file.readLines()

            for (line in pointsList) {
                val strValue: List<String> = line.split(",")
                pointsX.add(strValue[0].toFloat())
                pointsY.add(strValue[1].toFloat())
            }
        } / 1000000.0
        println("Points: ${pointsX.size} time: $time0 ms")
    }

    private fun getResizedPoints(point: Array<Float>) : Array<Float> {
        return arrayOf(
            point[0] * (ResizeValues.resizedWidth!! / ResizeValues.originalWidth!!),
            point[1] * (ResizeValues.resizedHeight!! / ResizeValues.originalHeight!!)
        )
    }

    fun setParam(
        originalWidth: Float,
        originalHeight: Float,
        resizedWidth: Float,
        resizedHeight: Float,
        speedAnimation: Float = 0f,
    ) {
        /**
         * Updated canvas size
         */
        ResizeValues.resizedWidth = resizedWidth
        ResizeValues.resizedHeight = resizedHeight


        /**
         * Size of project canvas
         */
        ResizeValues.originalWidth = originalWidth
        ResizeValues.originalHeight = originalHeight

        /**
         * Speed Animation 0.0 - 1.0
         */
        this.speedAnimation = speedAnimation

//        println("width: $resizedWidth, height: $resizedHeight")
    }

    fun syncSpeed() : Float {
        /** (1- speedAnimation) - inverting value, min speed 0.0 - max speed 1.0 */
        return (ResizeValues.resizedWidth!! / ResizeValues.originalWidth!!) + 1
    }

    fun getPointsSize() : Int {
        return pointsX.size
    }

    /**
     * Get pointPosition by index,
     * first call getPointsSize() return count of points
     *
     * Return Array<Float> x - [0], y - [1]
     */
    fun getPoint(index: Long, posX: Float = 0f, posY: Float = 0f, isResize: Boolean = false) : Array<Float> {
        if (index.toInt() < getPointsSize()) {
            return if (isResize) getResizedPoints(arrayOf(pointsX[index.toInt()], pointsY[index.toInt()]))
            else arrayOf(pointsX[index.toInt()], pointsY[index.toInt()])
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
            return arrayOf(pointsX[index], pointsY[index])
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
            return arrayOf(pointsX[index], pointsY[index])
        }
        println("ERROR: GetPoint index bigger Trajectory Element file, Max index: ${getPointsSize() - 1}")
        return arrayOf(0f + posX, 0f + posY)
    }
}