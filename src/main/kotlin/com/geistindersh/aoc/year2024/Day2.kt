package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import kotlin.math.absoluteValue

class Day2(
    dataFile: DataFile,
) {
    private val lines =
        fileToStream(2024, 2, dataFile)
            .map { line -> "[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList() }
            .toList()

    private fun List<Int>.isSafe(): Boolean {
        val inRange =
            this
                .windowed(2) { (l, r) -> (l - r).absoluteValue }
                .all { it in 1..3 }
        if (!inRange) return false
        val sorted = this.sorted()
        return sorted == this || sorted == this.reversed()
    }

    /**
     * Check all possible removal points in the worst case, or only one in the best case
     */
    private fun List<Int>.canBeMadeSafe() =
        this.indices.any {
            this.toMutableList().apply { removeAt(it) }.isSafe()
        }

    fun part1() = lines.count { it.isSafe() }

    fun part2() =
        lines
            .map {
                when {
                    it.isSafe() -> 1
                    it.canBeMadeSafe() -> 1
                    else -> 0
                }
            }.sum()
}

fun day2() {
    val day = Day2(DataFile.Part1)
    report(2024, 2, day.part1(), day.part2())
}
