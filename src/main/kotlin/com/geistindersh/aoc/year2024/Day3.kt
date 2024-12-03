package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day3(
    dataFile: DataFile,
) {
    private val input = fileToStream(2024, 3, dataFile).joinToString("")

    fun part1() =
        mulRegex
            .findAll(input)
            .map {
                it.groups
                    .mapNotNull { g -> g?.value?.toLongOrNull() }
                    .reduce(Long::times)
            }.reduce(Long::plus)

    fun part2(): Long {
        var isDont = false
        var total = 0L
        for (match in doDontMulRegex.findAll(input)) {
            when (match.value) {
                "don't()" -> isDont = true
                "do()" -> isDont = false
                else -> {
                    if (isDont) continue
                    val groups = match.groups.mapNotNull { g -> g?.value?.toLongOrNull() }
                    total += groups.reduce(Long::times)
                }
            }
        }
        return total
    }

    companion object {
        private val mulRegex = "mul\\(([0-9]+),([0-9]+)\\)".toRegex()
        private val doDontMulRegex = "(do\\(\\)|don't\\(\\)|mul\\(([0-9]+),([0-9]+)\\))".toRegex()
    }
}

fun day3() {
    val day = Day3(DataFile.Part1)
    report(2024, 3, day.part1(), day.part2())
}
