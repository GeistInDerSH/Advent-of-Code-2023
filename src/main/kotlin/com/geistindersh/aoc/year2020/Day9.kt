package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day9(dataFile: DataFile, private val windowSize: Int) {
    private val numbers = fileToStream(2020, 9, dataFile).map(String::toLong).toList()

    private fun firstMissingSumInWindow() = numbers
        .windowed(windowSize + 1)
        .map { nums ->
            val preamble = nums.dropLast(1)
            if (preamble.any { nums.last() - it in preamble }) {
                null
            } else {
                nums.last()
            }
        }
        .first { it != null }!!

    fun part1() = firstMissingSumInWindow()
    fun part2(): Long {
        val target = firstMissingSumInWindow()
        for (start in numbers.indices) {
            var sum = numbers[start]
            var end = start + 1
            while (sum < target) { // Grow the window until we exceed the target
                sum += numbers[end]
                end += 1
            }

            if (sum == target) {
                val min = (start..<end).minOf { numbers[it] }
                val max = (start..<end).maxOf { numbers[it] }
                return min + max
            }
        }
        return -1
    }
}

fun day9() {
    val day = Day9(DataFile.Part1, 25)
    report(2020, 9, day.part1(), day.part2())
}
