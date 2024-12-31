package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day3(
    dataFile: DataFile,
) : AoC<Long, Long> {
    private val input = fileToStream(2024, 3, dataFile).joinToString("")

    private fun MatchGroupCollection.reduceGroup() =
        this
            .mapNotNull { it?.value?.toLongOrNull() }
            .reduce(Long::times)

    override fun part1() =
        MUL_REGEX
            .findAll(input)
            .fold(0L) { acc, match -> acc + match.groups.reduceGroup() }

    override fun part2(): Long {
        var isEnabled = true
        var total = 0L
        for (match in DO_DONT_MUL_REGEX.findAll(input)) {
            when (match.value) {
                "don't()" -> isEnabled = false
                "do()" -> isEnabled = true
                else -> {
                    if (!isEnabled) continue
                    total += match.groups.reduceGroup()
                }
            }
        }
        return total
    }

    companion object {
        private val MUL_REGEX = """mul\((\d+),(\d+)\)""".toRegex()
        private val DO_DONT_MUL_REGEX = """(do\(\)|don't\(\)|mul\((\d+),(\d+)\))""".toRegex()
    }
}

fun day3() {
    val day = Day3(DataFile.Part1)
    report(2024, 3, day.part1(), day.part2())
}
