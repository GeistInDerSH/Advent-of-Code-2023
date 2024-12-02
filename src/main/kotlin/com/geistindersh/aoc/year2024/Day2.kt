package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import kotlin.math.absoluteValue

class Day2(
    dataFile: DataFile,
) {
    private val data =
        fileToStream(2024, 2, dataFile)
            .map { line -> "[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList() }
            .toList()

    fun part1(): Int {
        var count = 0

        for (seq in data) {
            val isIncreasing = seq[0] < seq[1]
            var all = true
            for ((a, b) in seq.windowed(2)) {
                if (isIncreasing && a > b) {
                    all = false
                    break
                } else if (!isIncreasing && a < b) {
                    all = false
                    break
                } else if ((a - b).absoluteValue !in 1..3) {
                    all = false
                    break
                }
            }

            if (all) count++
        }

        return count
    }

    fun part2() = 0
}

fun day2() {
    val day = Day2(DataFile.Part1)
    report(2024, 2, day.part1(), day.part2())
}
