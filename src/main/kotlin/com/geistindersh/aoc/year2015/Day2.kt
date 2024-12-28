package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.extractPositiveIntegers

class Day2(
    dataFile: DataFile,
) {
    private val data = fileToStream(2015, 2, dataFile).map { it.extractPositiveIntegers().sorted() }.toList()

    private fun surfaceArea(
        l: Int,
        h: Int,
        w: Int,
    ) = 2 * l * w + 2 * w * h + 2 * h * l

    private fun areaOfSmallestSide(
        l: Int,
        h: Int,
        w: Int,
    ) = (l * h * w) / l.coerceAtLeast(h).coerceAtLeast(w)

    private fun perimeter(ints: List<Int>) = 2 * (ints[0] + ints[1])

    private fun ribbonLength(ints: List<Int>) = ribbonLength(ints[0], ints[1], ints[2])

    private fun ribbonLength(
        l: Int,
        h: Int,
        w: Int,
    ) = l * h * w

    fun part1() = data.sumOf { (l, h, w) -> surfaceArea(l, h, w) + areaOfSmallestSide(l, h, w) }

    fun part2() = data.sumOf { perimeter(it) + ribbonLength(it) }
}

fun day2() {
    val day = Day2(DataFile.Part1)
    report(2015, 2, day.part1(), day.part2())
}
