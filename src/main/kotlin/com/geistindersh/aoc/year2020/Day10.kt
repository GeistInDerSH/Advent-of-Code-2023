package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day10(
    dataFile: DataFile,
) {
    private val jolts = fileToStream(2020, 10, dataFile).map(String::toInt).toList()
    private val joltsSet = jolts.toSet()

    fun part1(): Int {
        // Start at 1, with a default value of 1 to skip the 0 state
        val options = mutableMapOf<Int, Int>().withDefault { 1 }
        var current = 1
        while (current in joltsSet) {
            if (current + 1 in joltsSet) {
                options[1] = options.getValue(1) + 1
                current += 1
                continue
            } else if (current + 3 in joltsSet) {
                options[3] = options.getValue(3) + 1
                current += 3
                continue
            }
            break
        }
        return options.getValue(1) * options.getValue(3)
    }

    fun part2(): Long {
        val jolts = jolts.sorted().let { it + listOf(it.last() + 3) }
        val counter = mutableMapOf(0 to 1L)

        for (adapter in jolts) {
            counter[adapter] = (1..3).sumOf { counter.getOrDefault(adapter - it, 0) }
        }

        return counter[jolts.last()]!!
    }
}

fun day10() {
    val day = Day10(DataFile.Part1)
    report(2020, 10, day.part1(), day.part2())
}
