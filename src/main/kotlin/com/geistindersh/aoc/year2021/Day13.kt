package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day13(dataFile: DataFile) {
    private val points = fileToStream(2021, 13, dataFile)
        .takeWhile(String::isNotBlank)
        .map { line ->
            val (col, row) = "[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList()
            Point2D(row, col)
        }
        .toSet()
    private val foldInstructions = fileToStream(2021, 13, dataFile)
        .dropWhile(String::isNotBlank)
        .drop(1)
        .map { it.split(" ").last().split("=") }
        .map { (axis, line) -> axis to line.toInt() }
        .toList()

    fun part1(): Int {
        val (axis, foldIndex) = foldInstructions.first()
        return points
            .mapNotNull { point ->
                when (axis) {
                    "x" -> when {
                        point.col < foldIndex -> point
                        else -> point.copy(col = foldIndex - (point.col - foldIndex))
                    }

                    "y" -> when {
                        point.row < foldIndex -> point
                        else -> point.copy(row = foldIndex - (point.row - foldIndex))
                    }

                    else -> null
                }
            }
            .toSet()
            .count()
    }

    fun part2(): String {
        var points = points
        for ((axis, foldIndex) in foldInstructions) {
            val newPoints = mutableSetOf<Point2D>()

            for (point in points) {
                val newPoint = when (axis) {
                    "x" -> when {
                        point.col < foldIndex -> point
                        else -> point.copy(col = foldIndex - (point.col - foldIndex))
                    }

                    "y" -> when {
                        point.row < foldIndex -> point
                        else -> point.copy(row = foldIndex - (point.row - foldIndex))
                    }

                    else -> continue
                }
                newPoints.add(newPoint)
            }
            points = newPoints
        }

        val rowMax = points.maxOf { it.row }
        val colMax = points.maxOf { it.col }

        val sb = StringBuilder()
        for (row in 0..rowMax) {
            for (col in 0..colMax) {
                val point = Point2D(row, col)
                val char = if (point in points) '#' else '.'
                sb.append(char)
            }
            sb.append("\n")
        }
        return sb.toString()
    }
}

fun day13() {
    val day = Day13(DataFile.Part1)
    report(2021, 13, day.part1(), day.part2())
}
