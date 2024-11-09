package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day5(dataFile: DataFile) {
    private val searches = fileToStream(2020, 5, dataFile).map { Zone(it) }.toList()

    private data class Zone(val instruction: String) {
        fun getSeatPosition(): Point2D {
            var rowRange = 0..127
            var colRange = 0..7
            for (c in instruction) {
                when (c) {
                    'F' -> {
                        val midPoint = (rowRange.last + rowRange.first) / 2
                        rowRange = rowRange.first..midPoint
                    }

                    'B' -> {
                        val midPoint = (rowRange.last + rowRange.first) / 2
                        rowRange = midPoint + 1..rowRange.last
                    }

                    'L' -> {
                        val midPoint = (colRange.last + colRange.first) / 2
                        colRange = colRange.first..midPoint
                    }

                    'R' -> {
                        val midPoint = (colRange.last + colRange.first) / 2
                        colRange = midPoint + 1..colRange.last
                    }

                    else -> continue
                }
            }

            return Point2D(rowRange.first, colRange.first)
        }

        fun getScore() = getScore(getSeatPosition())

        companion object {
            fun getScore(point: Point2D) = point.row * 8 + point.col
        }
    }

    fun part1() = searches.maxOf { it.getScore() }
    fun part2(): Int {
        val seatingChart: MutableMap<Point2D, Int?> = (0..127)
            .flatMap { row -> (0..7).map { Point2D(row, it) to null } }
            .toMap()
            .toMutableMap()

        for (search in searches) {
            seatingChart[search.getSeatPosition()] = search.getScore()
        }

        return seatingChart
            .toSortedMap(compareBy<Point2D> { it.row }.thenBy { it.col })
            .entries
            .dropWhile { it.value == null } // Skip rows that DNE
            .first { it.value == null } // Our seat is the first with no value
            .key
            .let { Zone.getScore(it) }
    }
}

fun day5() {
    val day = Day5(DataFile.Part1)
    report(2020, 5, day.part1(), day.part2())
}
