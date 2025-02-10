package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.year2019.intcomputer.IntComputer

class Day9(
    dataFile: DataFile,
) : AoC<Long, Int> {
    private val data = fileToString(2019, 9, dataFile).split(',').map(String::toLong)

    private fun solution(input: Long) = IntComputer(data, listOf(input)).run().getOutput()!!

    override fun part1() = solution(1L)

    override fun part2() = 0
}

fun day9() {
    val day = Day9(DataFile.Part1)
    report(2019, 9, day.part1(), day.part2())
}
