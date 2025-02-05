package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day4(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val range = fileToString(2019, 4, dataFile).split('-').map(String::toInt).let { it[0]..it[1] }

    private fun String.hasAdjacentNumbers() = this.windowed(2).any { it[0] == it[1] }

    private fun String.hasAdjacentNumbersStrict() =
        this
            .groupingBy { it }
            .eachCount()
            .values
            .any { it == 2 }

    private fun String.isIncreasing() = this.windowed(2).all { it[0].code <= it[1].code }

    override fun part1() = range.asSequence().map { it.toString() }.count { it.isIncreasing() && it.hasAdjacentNumbers() }

    override fun part2() = range.asSequence().map { it.toString() }.count { it.isIncreasing() && it.hasAdjacentNumbersStrict() }
}

fun day4() {
    val day = Day4(DataFile.Part1)
    report(2019, 4, day.part1(), day.part2())
}
