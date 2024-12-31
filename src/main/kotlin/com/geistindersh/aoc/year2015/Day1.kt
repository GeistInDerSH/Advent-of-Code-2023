package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day1(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val floors =
        fileToString(2015, 1, dataFile)
            .mapNotNull {
                when (it) {
                    '(' -> 1
                    ')' -> -1
                    else -> null
                }
            }.toList()

    override fun part1() = floors.sum()

    override fun part2(): Int {
        var floor = 0
        floors.forEachIndexed { idx, value ->
            floor += value
            if (floor < 0) return idx + 1
        }
        return -1
    }
}

fun day1() {
    val day = Day1(DataFile.Part1)
    report(2015, 1, day.part1(), day.part2())
}
