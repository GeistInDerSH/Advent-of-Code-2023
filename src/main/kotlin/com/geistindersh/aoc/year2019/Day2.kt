package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day2(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val numbers = fileToString(2019, 2, dataFile).split(",").map(String::toInt)

    private fun List<Int>.run(): List<Int> {
        val nums = this.toMutableList()
        var pos = 0
        while (pos < this.size) {
            when (nums[pos]) {
                1 -> {
                    val l = nums[pos + 1]
                    val r = nums[pos + 2]
                    val dest = nums[pos + 3]
                    nums[dest] = nums[l] + nums[r]
                    pos += 4
                }
                2 -> {
                    val l = nums[pos + 1]
                    val r = nums[pos + 2]
                    val dest = nums[pos + 3]
                    nums[dest] = nums[l] * nums[r]
                    pos += 4
                }
                99 -> break
                else -> throw Exception("Unrecognized input: ${this[pos]}")
            }
        }
        return nums
    }

    fun part1(
        noun: Int,
        verb: Int,
    ) = numbers
        .toMutableList()
        .apply {
            this[1] = noun
            this[2] = verb
        }.run()
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
