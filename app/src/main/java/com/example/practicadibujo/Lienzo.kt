package com.example.practicadibujo


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class Lienzo(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val squareSize = 100f
    private var numRows = 0
    private var numCols = 0
    private var score = 0

    private lateinit var matrix: Array<IntArray>

    private val snakeSegments = mutableListOf<Pair<Int, Int>>()
    private var foodX = 0
    private var foodY = 0

    private var direction = Direction.RIGHT
    private var isGameOver = false

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            if (!isGameOver) {
                moveSnake()

                // Colision Cabeza comida
                if (snakeSegments[0] == Pair(foodX, foodY)) {
                    addSegmentToSnake()
                    score++
                    generateNewFoodPosition()
                }

                // Colision Cabeza Cuerpo
                if (snakeSegments.subList(1, snakeSegments.size).contains(snakeSegments[0])) {
                    endGame()
                }

                invalidate()
                handler.postDelayed(this, 200)
            }
        }
    }

    init {
        handler.post(runnable)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        numRows = (height / squareSize).toInt()
        numCols = (width / squareSize).toInt()
        initializeMatrix()
        placeInitialSnake()
        generateNewFoodPosition()
    }

    private fun initializeMatrix() {
        // Inicializa la matriz
        matrix = Array(numRows) { IntArray(numCols) }
    }

    private fun placeInitialSnake() {
        // Coloca la serpiente inicial en la matriz
        val startX = 0
        val startY = 10
        snakeSegments.add(Pair(startX, startY))
        matrix[startY][startX] = 1 // 1 indica que la celda está ocupada por la serpiente
    }

    private fun moveSnake() {
        // Mueve la serpiente en la matriz
        val head = snakeSegments[0]
        val newHead = when (direction) {
            Direction.RIGHT -> Pair((head.first + 1) % numCols, head.second)
            Direction.LEFT -> Pair((head.first - 1 + numCols) % numCols, head.second)
            Direction.UP -> Pair(head.first, (head.second - 1 + numRows) % numRows)
            Direction.DOWN -> Pair(head.first, (head.second + 1) % numRows)
        }

        // Actualiza la matriz
        matrix[head.second][head.first] = 0
        snakeSegments.add(0, newHead)
        matrix[newHead.second][newHead.first] = 1

        // Actualiza la cola
        val tail = snakeSegments.removeAt(snakeSegments.size - 1)
        matrix[tail.second][tail.first] = 0
    }

    private fun addSegmentToSnake() {
        val lastSegment = snakeSegments.last()
        val newSegment = when (direction) {
            Direction.RIGHT -> Pair((lastSegment.first - 1 + numCols) % numCols, lastSegment.second)
            Direction.LEFT -> Pair((lastSegment.first + 1) % numCols, lastSegment.second)
            Direction.UP -> Pair(lastSegment.first, (lastSegment.second + 1) % numRows)
            Direction.DOWN -> Pair(lastSegment.first, (lastSegment.second - 1 + numRows) % numRows)
        }
        snakeSegments.add(newSegment)
        matrix[newSegment.second][newSegment.first] = 1
    }

    private fun generateNewFoodPosition() {
        do {
            foodX = (0 until numCols).random()
            foodY = (0 until numRows).random()
        } while (matrix[foodY][foodX] == 1)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()

        val borderPaint = Paint().apply {
            color = Color.RED
            strokeWidth = 10f
            style = Paint.Style.STROKE
        }
        canvas.drawRect(0f, 0f, width, height, borderPaint)

        val objPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL
        }

        val foodPaint = Paint().apply {
            color = Color.YELLOW
            style = Paint.Style.FILL
        }

        // Dibuja la serpiente
        for ((x, y) in snakeSegments) {
            canvas.drawRect(x * squareSize, y * squareSize, (x + 1) * squareSize, (y + 1) * squareSize, objPaint)
        }

        // Dibuja la comida
        canvas.drawRect(foodX * squareSize, foodY * squareSize, (foodX + 1) * squareSize, (foodY + 1) * squareSize, foodPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false

        val x = event.x
        val y = event.y

        if (isGameOver) return true

        when {
            y < height / 4 && direction != Direction.DOWN -> direction = Direction.UP
            y > 3 * height / 4 && direction != Direction.UP -> direction = Direction.DOWN
            x < width / 4 && direction != Direction.RIGHT -> direction = Direction.LEFT
            x > 3 * width / 4 && direction != Direction.LEFT -> direction = Direction.RIGHT
        }

        return true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacks(runnable)
    }

    private fun endGame() {
        isGameOver = true
        Toast.makeText(context, "Fin del Juego. Puntuación: $score", Toast.LENGTH_LONG).show()
    }

    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }
}


