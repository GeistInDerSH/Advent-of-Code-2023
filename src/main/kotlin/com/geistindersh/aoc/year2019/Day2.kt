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

    fun part1(override: Map<Int, Int>) =
        numbers
            .toMutableList()
            .let {
                for ((k, v) in override.entries) {
                    it[k] = v
                }
                it
            }.run()
            .first()

    override fun part1() = part1(mapOf(1 to 12, 2 to 2))

    override fun part2() = 0
}

fun day2() {
    val day = Day2(DataFile.Part1)
    report(2019, 2, day.part1(), day.part2())
}
