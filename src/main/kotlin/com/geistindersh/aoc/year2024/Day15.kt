package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day15(
    rawGrid: String,
    rawMoves: String,
) {
    constructor(sections: List<String>) : this(sections.first(), sections.last())
    constructor(dataFile: DataFile) : this(fileToString(2024, 15, dataFile).split("\n\n"))

    private val grid =
        rawGrid
            .split("\n")
            .flatMapIndexed { row, line -> line.mapIndexed { col, char -> Point2D(row, col) to char } }
            .toMap()
            .let { grid ->
                // Remove the cursor from the map so that it doesn't interfere with the
                // movement. This allows for a speedup in the general case of no objects being pushed
                val start = grid.filterValues { it == '@' }.firstNotNullOf { it.key }
                val withoutCursor = grid.toMutableMap().apply { set(start, '.') }
                Grid(withoutCursor, start)
            }
    private val moves = rawMoves.mapNotNull { Direction.tryFromArrow(it) }
    private val expandedGrid = ExpandedGrid.from(grid)

    private interface Warehouse {
        /**
         * Generate the next state of the [Warehouse] when applying the given [move]
         *
         * @param move The direction to move the cursor
         *
         * @return The new [Warehouse]
         */
        fun next(move: Direction): Warehouse

        /**
         * @return The score of the [Warehouse]
         */
        fun score(): Long
    }

    data class Grid(
        val grid: Map<Point2D, Char>,
        val position: Point2D,
    ) : Warehouse {
        fun maxRow() = grid.keys.maxOf { it.row }

        fun maxCol() = grid.keys.maxOf { it.col }

        override fun score() = grid.filterValues { it == 'O' }.keys.sumOf { it.row * 100L + it.col }

        override fun next(move: Direction): Grid {
            val newPosition = position + move
            return when (grid[newPosition]!!) {
                // General case, we don't push anything. Because we don't keep the cursor in the map, just update
                // the position
                '.' -> copy(position = newPosition)
                'O' -> {
                    val newGrid = grid.toMutableMap()
                    var nextPos = newPosition + move
                    while (nextPos in grid && grid[nextPos]!! == 'O') nextPos += move
                    when (grid[nextPos]!!) {
                        '.' -> {
                            var moving = nextPos
                            while (moving != position) {
                                val prev = moving - move
                                newGrid[moving] = newGrid[prev]!!
                                newGrid[prev] = '.'
                                moving = prev
                            }

                            Grid(newGrid, newPosition)
                        }
                        else -> this
                    }
                }
                else -> this
            }
        }

        override fun toString(): String {
            val rowMax = maxRow()
            val colMax = maxCol()
            val buff = Array(rowMax + 1) { Array(colMax + 1) { ' ' } }
            for ((k, v) in grid) {
                buff[k.row][k.col] = v
            }
            buff[position.row][position.col] = '@'
            return buff.joinToString("\n") { it.joinToString("") }
        }

        @Suppress("unused")
        fun print() {
            println(this.toString())
        }
    }

    data class ExpandedGrid(
        val grid: Map<Point2D, Char>,
        val position: Point2D,
    ) : Warehouse {
        override fun score() = grid.filterValues { it == '[' }.keys.sumOf { it.row * 100L + it.col }

        private sealed class Obstacle {
            data class Empty(
                val c: Char = '.',
            ) : Obstacle()

            data class Wall(
                val c: Char = '#',
            ) : Obstacle()

            data class Box(
                val lhs: Point2D,
                val rhs: Point2D,
            ) : Obstacle() {
                fun toPair() = lhs to rhs

                companion object {
                    fun from(pair: Pair<Point2D, Point2D>) = Box(pair.first, pair.second)
                }
            }
        }

        /**
         * Get all [Obstacle] touching the given pair of points in the given [Direction].
         *
         * @param move The direction to check for [Obstacle]s in
         *
         * @return The unique [Obstacle]s a [Obstacle.Box] may encounter when moving in that [Direction]
         */
        private fun Pair<Point2D, Point2D>.getTouchingAllObstacles(move: Direction): Set<Obstacle> {
            val (left, right) = this
            val newLeft = left + move
            val newRight = right + move
            val touchingLeft =
                when (grid[left + move]!!) {
                    '#' -> setOf(Obstacle.Wall())
                    '[' -> setOf(Obstacle.Box(newLeft, newRight))
                    ']' -> setOf(Obstacle.Box(newLeft + Direction.West, newLeft))
                    else -> setOf(Obstacle.Empty())
                }
            val touchingRight =
                when (grid[right + move]!!) {
                    '#' -> setOf(Obstacle.Wall())
                    '[' -> setOf(Obstacle.Box(newRight, newRight + Direction.East))
                    ']' -> setOf(Obstacle.Box(newLeft, newRight))
                    else -> setOf(Obstacle.Empty())
                }
            return touchingLeft + touchingRight
        }

        /**
         * Get all [Obstacle]s that are touching the current point, in the [Direction] recursively
         *
         * @param move The direction that the boxes are going to be moved
         *
         * @return All boxes touching the initial box in the direction
         */
        private fun Pair<Point2D, Point2D>.getTouchingObstacles(move: Direction): List<Obstacle> {
            val queue = ArrayDeque<Pair<Point2D, Point2D>>().apply { add(this@getTouchingObstacles) }
            val boxes: MutableList<Obstacle> = mutableListOf(Obstacle.Box.from(this@getTouchingObstacles))
            while (queue.isNotEmpty()) {
                val point = queue.removeFirst()
                for (touch in point.getTouchingAllObstacles(move)) {
                    boxes.add(touch)
                    when (touch) {
                        is Obstacle.Wall -> continue
                        is Obstacle.Empty -> continue
                        is Obstacle.Box -> queue.add(touch.toPair())
                    }
                }
            }
            return boxes
        }

        override fun next(move: Direction): ExpandedGrid {
            val newPosition = position + move
            return when (grid[newPosition]!!) {
                // General case, we don't push anything. Because we don't keep the cursor in the map, just update
                // the position
                '.' -> copy(position = newPosition)
                '[', ']' -> {
                    // We can push East/West without needing the check for half-boxes. So, similar logic to
                    // Grid.next can be used
                    if (move == Direction.East || move == Direction.West) {
                        var nextPos = newPosition + move
                        while (nextPos in grid && (grid[nextPos]!! == '[' || grid[nextPos]!! == ']')) nextPos += move
                        when (grid[nextPos]!!) {
                            '.' -> {
                                val newGrid = grid.toMutableMap()
                                var moving = nextPos
                                while (moving != position) {
                                    val prev = moving - move
                                    newGrid[moving] = newGrid[prev]!!
                                    newGrid[prev] = '.'
                                    moving = prev
                                }

                                ExpandedGrid(newGrid, newPosition)
                            }
                            else -> this
                        }
                    } else {
                        // If we are moving North/South, we need to check to see where we are pushing the box
                        // as we may be pushing multiple boxes fanning out from our starting box
                        val box =
                            if (grid[newPosition] == '[') {
                                newPosition to newPosition + Direction.East
                            } else {
                                newPosition + Direction.West to newPosition
                            }
                        // If any of the touching are a wall, we can't push any further
                        val touching = box.getTouchingObstacles(move)
                        if (touching.any { it is Obstacle.Wall }) {
                            this
                        } else {
                            val newGrid = grid.toMutableMap()
                            // Reverse the boxes to avoid clobbering an earlier box when moving
                            val boxes = touching.filterIsInstance<Obstacle.Box>().map { it.toPair() }.reversed()
                            for ((lhs, rhs) in boxes) {
                                newGrid[lhs + move] = '['
                                newGrid[rhs + move] = ']'
                                newGrid[lhs] = '.'
                                newGrid[rhs] = '.'
                            }

                            ExpandedGrid(newGrid, newPosition)
                        }
                    }
                }
                else -> this
            }
        }

        companion object {
            fun from(grid: Grid): ExpandedGrid {
                val newGrid =
                    grid
                        .toString()
                        .replace("#", "##")
                        .replace("O", "[]")
                        .replace(".", "..")
                        .replace("@", "@.")
                        .split("\n")
                        .flatMapIndexed { row, line -> line.mapIndexed { col, char -> Point2D(row, col) to char } }
                        .toMap()
                        .toMutableMap()
                val start = newGrid.filterValues { it == '@' }.keys.first()
                newGrid[start] = '.'
                return ExpandedGrid(newGrid.toMap(), start)
            }
        }
    }

    private fun moveBoxes(warehouse: Warehouse) =
        generateSequence(0 to warehouse) { (i, g) -> if (i > moves.lastIndex) null else i + 1 to g.next(moves[i]) }
            .takeWhile { true }
            .last()
            .second
            .score()

    fun part1() = moveBoxes(grid)

    fun part2() = moveBoxes(expandedGrid)
}

fun day15() {
    val day = Day15(DataFile.Part1)
    report(2024, 15, day.part1(), day.part2())
}
