package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.year2019.intcomputer.IntComputer

class Day5(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val numbers = fileToString(2019, 5, dataFile).split(",").map(String::toInt)

    override fun part1(): Int {
        val computer = IntComputer(numbers, listOf(1))
        computer.run()
        return computer.getOutput() ?: -1
    }

    override fun part2(): Int {
        val computer = IntComputer(numbers, listOf(5))
        computer.run()
        return computer.getOutput() ?: -1
    }
}

fun day5() {
    val day = Day5(DataFile.Part1)
    report(2019, 5, day.part1(), day.part2())
}
