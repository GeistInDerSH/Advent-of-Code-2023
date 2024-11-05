package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.enums.Point
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day5(dataFile: DataFile) {
    private val searches = fileToStream(2020, 5, dataFile).map { Zone(it) }.toList()

    private data class Zone(val instruction: String) {
        fun getSeatPosition(): Point {
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

            return Point(rowRange.first, colRange.first)
        }

        fun getScore(): Int {
            val (row, col) = getSeatPosition()
            return row * 8 + col
        }

    }

    fun part1() = searches.maxOf { it.getScore() }
    fun part2() = 0
}

fun day5() {
    val day = Day5(DataFile.Part1)
    report(2020, 5, day.part1(), day.part2())
}
