package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day18(
    dataFile: DataFile,
) {
    private val data =
        fileToStream(2024, 18, dataFile)
            .map { it.split(",").map(String::toLong) }
            .map { (a, b) -> a to b }
            .toList()

    fun part1() = 0

    fun part2() = 0
}

fun day18() {
    val day = Day18(DataFile.Part1)
    report(2024, 18, day.part1(), day.part2())
}
