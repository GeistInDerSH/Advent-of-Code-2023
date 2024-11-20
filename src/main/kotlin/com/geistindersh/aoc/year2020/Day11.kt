package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.iterators.takeWhileInclusive
import com.geistindersh.aoc.helper.report

class Day11(
    dataFile: DataFile,
) {
    private val chart =
        fileToStream(2020, 11, dataFile)
            .flatMapIndexed { row, line ->
                line.mapIndexedNotNull { col, c ->
                    when (c) {
                        '#' -> Point2D(row, col) to true
                        'L' -> Point2D(row, col) to false
                        else -> null
                    }
                }
            }.toMap()
            .let { chart ->
                val visibility = generateVisibilityMap(chart)
                SeatingChart(chart, visibility)
            }

    private fun generateVisibilityMap(chart: Map<Point2D, Boolean>): Map<Point2D, List<Point2D>> {
        // Use the "raw" directions as opposed to Point::neighborsAll so we can repeatedly
        // add the current direction until we find a visible point
        val directions =
            listOf(
                Direction.North.toPair(),
                Direction.East.toPair(),
                Direction.South.toPair(),
                Direction.West.toPair(),
                Direction.North + Direction.East,
                Direction.North + Direction.West,
                Direction.South + Direction.East,
                Direction.South + Direction.West,
            )

        val visibility = mutableMapOf<Point2D, List<Point2D>>()
        val maxRow = chart.keys.maxOf { it.row } + 1
        val maxCol = chart.keys.maxOf { it.col } + 1
        for (row in 0..maxRow) {
            for (col in 0..maxCol) {
                val canSee = mutableListOf<Point2D>()
                val point = Point2D(row, col)
                for (dir in directions) {
                    var neighbor = point + dir
                    while (true) {
                        if (neighbor.row < 0 || neighbor.row > maxRow) break
                        if (neighbor.col < 0 || neighbor.col > maxCol) break
                        if (neighbor in chart) {
                            canSee.add(neighbor)
                            break
                        }
                        neighbor += dir
                    }
                }
                visibility[point] = canSee.toList()
            }
        }
        return visibility
    }

    private data class SeatingChart(
        val chart: Map<Point2D, Boolean>,
        val visibility: Map<Point2D, List<Point2D>>,
        val changed: Int = 0,
        val neighborThreshold: Int = 3,
    ) {
        fun next(): SeatingChart {
            val updatedChart = chart.toMutableMap()

            var changed = 0
            for ((point, isOccupied) in chart) {
                val neighbors = point.neighborsAll().filter { it in chart }
                if (isOccupied) {
                    if (neighbors.count { chart[it]!! } > neighborThreshold) {
                        updatedChart[point] = false
                        changed += 1
                        continue
                    }
                } else {
                    if (neighbors.none { chart[it]!! }) {
                        updatedChart[point] = true
                        changed += 1
                    }
                }
            }

            return this.copy(chart = updatedChart, changed = changed)
        }

        fun nextWithVisibility(): SeatingChart {
            val updatedChart = chart.toMutableMap()

            var changed = 0
            for ((point, isOccupied) in chart) {
                val neighbors = visibility[point] ?: continue
                if (isOccupied) {
                    if (neighbors.count { chart[it]!! } > neighborThreshold) {
                        updatedChart[point] = false
                        changed += 1
                        continue
                    }
                } else {
                    if (neighbors.none { chart[it]!! }) {
                        updatedChart[point] = true
                        changed += 1
                    }
                }
            }

            return this.copy(chart = updatedChart, changed = changed)
        }

        override fun toString(): String {
            val maxRow = chart.keys.maxOf { it.row } + 1
            val maxCol = chart.keys.maxOf { it.col } + 1
            val arr = Array(maxRow) { CharArray(maxCol) { '.' } }

            for (row in 0..maxRow) {
                for (col in 0..maxCol) {
                    val p = Point2D(row, col)
                    val isFilled = chart[p] ?: continue
                    arr[row][col] = if (isFilled) '#' else 'L'
                }
            }

            return arr.joinToString("\n") { it.joinToString("") }
        }
    }

    fun part1() =
        generateSequence(chart) { it.next() }
            .drop(1)
            .takeWhileInclusive { it.changed > 0 }
            .last()
            .chart
            .values
            .count { it }

    fun part2() =
        generateSequence(chart.copy(neighborThreshold = 4)) { it.nextWithVisibility() }
            .drop(1)
            .takeWhileInclusive { it.changed > 0 }
            .last()
            .chart
            .values
            .count { it }
}

fun day11() {
    val day = Day11(DataFile.Part1)
    report(2020, 11, day.part1(), day.part2())
}
