package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.iterators.takeWhileInclusive
import com.geistindersh.aoc.helper.report

class Day25(
    dataFile: DataFile,
) {
    private val targetPoint =
        fileToString(2015, 25, dataFile)
            .replace(".", "")
            .replace(",", "")
            .split(" ")
            .mapNotNull(String::toIntOrNull)
            .let { Point2D(it[0], it[1]) }

    private data class Code(
        val point: Point2D,
        val code: Long,
    ) {
        fun next(): Code {
            val (row, col) = point
            val newPoint = if (row - 1 < 1) Point2D(col + 1, 1) else Point2D(row - 1, col + 1)
            val newCode = (code * 252533) % 33554393
            return Code(newPoint, newCode)
        }
    }

    fun part1() =
        generateSequence(Code(Point2D(1, 1), 20151125)) { it.next() }
            .takeWhileInclusive { it.point != targetPoint }
            .last()
            .code

    fun part2() = "Push the button!"
}

fun day25() {
    val day = Day25(DataFile.Part1)
    report(2015, 25, day.part1(), day.part2())
}
