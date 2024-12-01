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
            .map { (a, b) -> a.toInt() to b.toInt() }
            .toList()
    private val left = pairs.map { it.first }.sorted()
    private val right = pairs.map { it.second }.sorted()

    fun part1() =
        left
            .zip(right)
            .sumOf { (left, right) -> (left - right).absoluteValue }

    fun part2() =
        left
            .map { l -> l to right.count { l == it } }
            .sumOf { (left, right) -> left * right }
}

fun day1() {
    val day = Day1(DataFile.Part1)
    report(2024, 1, day.part1(), day.part2())
}
