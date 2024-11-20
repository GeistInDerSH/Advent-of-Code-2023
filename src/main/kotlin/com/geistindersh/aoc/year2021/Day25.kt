package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day25(
    dataFile: DataFile,
) {
    private val valley =
        fileToStream(2021, 25, dataFile)
            .flatMapIndexed { row, s ->
                s.mapIndexed { col, c ->
                    when (c) {
                        '>' -> Point2D(row, col) to Direction.East

                        'v' -> Point2D(row, col) to Direction.South
                        else -> Point2D(row, col) to null
                    }
                }
            }.toMap()
            .let { Valley(it) }

    private data class Valley(
        val valley: Map<Point2D, Direction?>,
        val moved: Int = -1,
    ) {
        private val rowMax = valley.keys.maxOf { it.row }
        private val colMax = valley.keys.maxOf { it.col }

        fun next(): Valley {
            val newMap = valley.toMutableMap()
            var moves = 0

            for (row in 0..rowMax) {
                val rowStart = newMap[Point2D(row, 0)]
                var col = 0
                while (col <= colMax) {
                    val point = Point2D(row, col)
                    col += 1
                    val direction = newMap[point] ?: continue
                    if (direction != Direction.East) continue

                    val newPoint = (point + direction).let { if (it.col > colMax) it.copy(col = 0) else it }
                    if (newPoint.col == 0 && rowStart == null) {
                        newMap[newPoint] = Direction.East
                        newMap[point] = null
                        moves += 1
                    } else if (newPoint.col > 0 && newMap[newPoint] == null) {
                        newMap[newPoint] = Direction.East
                        newMap[point] = null
                        col += 1
                        moves += 1
                    }
                }
            }
            for (col in 0..colMax) {
                val colStart = newMap[Point2D(0, col)]
                var row = 0
                while (row <= rowMax) {
                    val point = Point2D(row, col)
                    row += 1
                    val direction = newMap[point] ?: continue
                    if (direction != Direction.South) continue

                    val newPoint = (point + direction).let { if (it.row > rowMax) it.copy(row = 0) else it }
                    if (newPoint.row == 0 && colStart == null) {
                        newMap[newPoint] = Direction.South
                        newMap[point] = null
                        moves += 1
                    } else if (newPoint.row > 0 && newMap[newPoint] == null) {
                        newMap[newPoint] = Direction.South
                        newMap[point] = null
                        moves += 1
                        row += 1
                    }
                }
            }

            return Valley(newMap, moves)
        }
    }

    fun part1() =
        generateSequence(valley) { it.next() }
            .takeWhile { it.moved != 0 }
            .count()

    fun part2() = "Start the sleigh!"
}

fun day25() {
    val day = Day25(DataFile.Part1)
    report(2021, 25, day.part1(), day.part2())
}
