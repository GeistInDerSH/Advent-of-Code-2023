package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day1(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val calories =
        fileToString(2022, 1, dataFile)
            .split("\n\n")
            .map { line ->
                line
                    .split("\n")
                    .map { it.toInt() }
                    .reduce(Int::plus)
            }

    override fun part1() = calories.max()

    override fun part2() =
        calories
            .sorted()
            .reversed()
            .take(3)
            .reduce(Int::plus)
}

fun day1() {
    val day = Day1(DataFile.Part1)
    report(2022, 1, day.part1(), day.part2())
}
