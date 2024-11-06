package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.enums.Point
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.iterators.takeWhileInclusive
import com.geistindersh.aoc.helper.report

class Day11(dataFile: DataFile) {
    private val chart = fileToStream(2020, 11, dataFile)
        .flatMapIndexed { row, line ->
            line.mapIndexedNotNull { col, c ->
                when (c) {
                    '#' -> Point(row, col) to true
                    'L' -> Point(row, col) to false
                    else -> null
                }
            }
        }
        .toMap()
        .let { SeatingChart(it) }

    private data class SeatingChart(val chart: Map<Point, Boolean>, val changed: Int = 0) {
        fun next(): SeatingChart {
            val updatedChart = chart.toMutableMap()

            var changed = 0
            for ((point, isOccupied) in chart) {
                val neighbors = point.neighborsAll().filter { it in chart }
                if (isOccupied) {
                    if (neighbors.count { chart[it]!! } > 3) {
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

            return SeatingChart(updatedChart, changed)
        }

        override fun toString(): String {
            val maxRow = chart.keys.maxOf { it.row } + 1
            val maxCol = chart.keys.maxOf { it.col } + 1
            val arr = Array(maxRow) { CharArray(maxCol) { '.' } }

            for (row in 0..maxRow) {
                for (col in 0..maxCol) {
                    val p = Point(row, col)
                    val isFilled = chart[p] ?: continue
                    arr[row][col] = if (isFilled) '#' else 'L'
                }
            }

            return arr.joinToString("\n") { it.joinToString("") }
        }
    }

    fun part1() = generateSequence(chart) { it.next() }
        .drop(1)
        .takeWhileInclusive { it.changed > 0 }
        .last()
        .chart
        .values
        .count { it }

    fun part2() = 0
}

fun day11() {
    val day = Day11(DataFile.Part1)
    report(2020, 11, day.part1(), day.part2())
}
