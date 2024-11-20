package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day22(
    dataFile: DataFile,
) {
    private val grid =
        fileToStream(2022, 22, dataFile)
            .takeWhile { it.isNotEmpty() }
            .flatMapIndexed { row, line ->
                line.mapIndexedNotNull { col, c ->
                    if (c == ' ') {
                        null
                    } else {
                        Point2D(row = row, col = col) to c
                    }
                }
            }.associate { it }
    private val commands = Steps.parseLine(fileToStream(2022, 22, dataFile).last())

    private sealed class Steps {
        data object Left : Steps()

        data object Right : Steps()

        data class Walk(
            val steps: Int,
        ) : Steps()

        companion object {
            private val re = "[0-9]+|[LR]".toRegex()

            fun parseLine(line: String) =
                re
                    .findAll(line)
                    .map {
                        when (it.value) {
                            "L" -> Left
                            "R" -> Right
                            else -> Walk(it.value.toInt())
                        }
                    }.toList()
        }
    }

    private fun walk(fn: (Point2D, Direction) -> Pair<Point2D, Direction>): Int {
        var pos =
            grid
                .keys
                .filter { it.row == 0 }
                .minOf { it.col }
                .let { Point2D(row = 0, col = it) }
        var direction = Direction.East

        for (cmd in commands) {
            when (cmd) {
                is Steps.Left -> direction = direction.turnLeft()
                is Steps.Right -> direction = direction.turnRight()
                is Steps.Walk -> {
                    val (p, d) =
                        generateSequence(pos to direction) { (p, d) ->
                            val step = p + d
                            when {
                                step in grid && grid[step] == '#' -> Pair(p, d)
                                step !in grid -> {
                                    val (wrap, wrapDirection) = fn(p, d)
                                    if (grid[wrap] == '.') {
                                        Pair(wrap, wrapDirection)
                                    } else {
                                        Pair(p, d)
                                    }
                                }

                                else -> Pair(step, d)
                            }
                        }.take(cmd.steps + 1)
                            .last()
                    pos = p
                    direction = d
                }
            }
        }

        val score =
            when (direction) {
                Direction.North -> 3
                Direction.South -> 1
                Direction.East -> 0
                Direction.West -> 2
            }

        return 1000 * (pos.row + 1) + 4 * (pos.col + 1) + score
    }

    private fun walkMap(
        point: Point2D,
        direction: Direction,
    ): Pair<Point2D, Direction> {
        val newDir = direction.turnRight().turnRight()
        return generateSequence(point) { it + newDir }
            .takeWhile { it in grid }
            .last() to direction
    }

    private fun walkMap3d(
        point: Point2D,
        direction: Direction,
    ): Pair<Point2D, Direction> {
        val size = 50
        return when (Triple(direction, point.col / size, point.row / size)) {
            Triple(Direction.North, 1, 0) -> Point2D(2 * size + point.col, 0) to Direction.East
            Triple(Direction.West, 1, 0) -> Point2D(3 * size - 1 - point.row, 0) to Direction.East
            Triple(Direction.North, 2, 0) -> Point2D(4 * size - 1, point.col - 2 * size) to Direction.North
            Triple(Direction.East, 2, 0) -> Point2D(3 * size - 1 - point.row, 2 * size - 1) to Direction.West
            Triple(Direction.South, 2, 0) -> Point2D(-size + point.col, 2 * size - 1) to Direction.West
            Triple(Direction.East, 1, 1) -> Point2D(size - 1, size + point.row) to Direction.North
            Triple(Direction.West, 1, 1) -> Point2D(size * 2, point.row - size) to Direction.South
            Triple(Direction.North, 0, 2) -> Point2D(point.col + size, size) to Direction.East
            Triple(Direction.West, 0, 2) -> Point2D(size * 3 - 1 - point.row, size) to Direction.East
            Triple(Direction.East, 1, 2) -> Point2D(size * 3 - 1 - point.row, size * 3 - 1) to Direction.West
            Triple(Direction.South, 1, 2) -> Point2D(2 * size + point.col, size - 1) to Direction.West
            Triple(Direction.East, 0, 3) -> Point2D(size * 3 - 1, point.row - size * 2) to Direction.North
            Triple(Direction.South, 0, 3) -> Point2D(0, point.col + size * 2) to Direction.South
            Triple(Direction.West, 0, 3) -> Point2D(0, point.row - size * 2) to Direction.South
            else -> throw Exception("Unknown argument Triple($direction, ${point.col / 50}, ${point.row / 50})")
        }
    }

    fun part1() = walk(::walkMap)

    fun part2() = walk(::walkMap3d)
}

fun day22() {
    val day = Day22(DataFile.Part1)
    report(2022, 22, day.part1(), day.part2())
}
