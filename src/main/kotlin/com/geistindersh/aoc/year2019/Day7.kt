package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.iterators.permutations
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.year2019.intcomputer.IntComputer

class Day7(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val input = fileToString(2019, 7, dataFile).split(',').map(String::toInt)

    fun List<Int>.chainedThrusterSignal(): Int =
        this.fold(0) { value, thruster -> IntComputer(input, listOf(thruster, value)).run().getOutput()!! }

    override fun part1() = FULL_SET.toSet().permutations().maxOf { it.chainedThrusterSignal() }

    override fun part2() = 0

    companion object {
        private val FULL_SET = (0..4).toSet()
    }
}

fun day7() {
    val day = Day7(DataFile.Part1)
    report(2019, 7, day.part1(), day.part2())
}
