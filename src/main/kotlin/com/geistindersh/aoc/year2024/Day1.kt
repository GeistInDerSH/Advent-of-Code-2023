package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import kotlin.math.absoluteValue

class Day1(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val pairs =
        fileToStream(2024, 1, dataFile)
            .map { it.split("   ").map(String::toInt) }
            .map { (l, r) -> l to r }
            .toList()
    private val left = pairs.map { it.first }.sorted()
    private val right = pairs.map { it.second }.sorted()

    override fun part1() = left.zip(right).sumOf { (l, r) -> (l - r).absoluteValue }

    override fun part2() = left.sumOf { l -> l * right.count { l == it } }
}

fun day1() {
    val day = Day1(DataFile.Part1)
    report(2024, 1, day.part1(), day.part2())
}
