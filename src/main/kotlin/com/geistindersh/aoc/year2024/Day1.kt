package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import kotlin.math.absoluteValue

class Day1(
    dataFile: DataFile,
) {
    private val pairs =
        fileToStream(2024, 1, dataFile)
            .map { it.split("   ") }
            .map { (l, r) -> l.toInt() to r.toInt() }
            .toList()
    private val left = pairs.map { it.first }.sorted()
    private val right = pairs.map { it.second }.sorted()

    fun part1() = left.zip(right).sumOf { (l, r) -> (l - r).absoluteValue }

    fun part2() = left.sumOf { l -> l * right.count { l == it } }
}

fun day1() {
    val day = Day1(DataFile.Part1)
    report(2024, 1, day.part1(), day.part2())
}
