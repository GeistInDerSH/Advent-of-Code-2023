package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day1(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val depths = fileToStream(2021, 1, dataFile).map(String::toInt).toList()

    private fun radar(windowSize: Int) =
        depths
            .windowed(windowSize) { it.sum() }
            .windowed(2) { it.first() < it.last() }
            .count { it }

    override fun part1() = radar(1)

    override fun part2() = radar(3)
}

fun day1() {
    val day = Day1(DataFile.Part1)
    report(2021, 1, day.part1(), day.part2())
}
