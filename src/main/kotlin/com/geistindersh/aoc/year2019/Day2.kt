package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.year2019.intcomputer.IntComputer

class Day2(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val numbers = fileToString(2019, 2, dataFile).split(",").map(String::toInt)

    fun part1(
        noun: Int,
        verb: Int,
    ) = numbers
        .toMutableList()
        .apply {
            this[1] = noun
            this[2] = verb
        }.let { IntComputer(it) }
        .run()
        .getMemory()
        .first()

    // Overload match so that we can test without swapping values
    @Suppress("UNUSED_PARAMETER")
    fun part1(override: Map<Int, Int>) = part1(numbers[1], numbers[2])

    override fun part1() = part1(12, 2)

    fun part2(target: Int): Pair<Int, Int> =
        (0..100)
            .flatMap { noun -> (0..100).map { verb -> noun to verb } }
            .first { (noun, verb) -> part1(noun, verb) == target }

    override fun part2() = part2(19690720).let { 100 * it.first + it.second }
}

fun day2() {
    val day = Day2(DataFile.Part1)
    report(2019, 2, day.part1(), day.part2())
}
