package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.extractPositiveIntegers

class Day15(
    dataFile: DataFile,
) {
    private val numbers = fileToString(2020, 15, dataFile).extractPositiveIntegers()

    private fun List<Int>.valueAtTurn(targetTurn: Int): Int {
        val history = this.associateWith { n -> this.indexOf(n) + 1 }.toMutableMap()
        var currentTurn = this.size
        var latest = this.last()

        while (currentTurn != targetTurn) {
            val prevLatest = latest
            latest = history[latest]
                ?.let {
                    when (it) {
                        currentTurn -> 0 // We just saw this number
                        else -> currentTurn - it // Diff between now and last time we saw the num
                    }
                }
                ?: 0 // We haven't seen this number before
            history[prevLatest] = currentTurn

            currentTurn += 1
        }
        return latest
    }

    fun part1() = numbers.valueAtTurn(2020)

    fun part2() = numbers.valueAtTurn(30000000)
}

fun day15() {
    val day = Day15(DataFile.Part1)
    report(2020, 15, day.part1(), day.part2())
}
