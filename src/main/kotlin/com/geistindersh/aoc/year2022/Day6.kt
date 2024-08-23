package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day6(dataFile: DataFile) {
    private val noise = fileToString(2022, 6, dataFile)

    private fun getSignalOffset(windowSize: Int): Int {
        return windowSize +
                (0..(noise.count() - windowSize))
                    .firstNotNullOf {
                        val chars = noise.substring(it, it + windowSize)
                        val unique = chars.toSet()
                        if (chars.count() == windowSize && unique.count() == windowSize) {
                            it
                        } else {
                            null
                        }
                    }
    }

    fun part1() = getSignalOffset(4)
    fun part2() = getSignalOffset(14)
}

fun day6() {
    val day = Day6(DataFile.Part1)
    report(2022, 6, day.part1(), day.part2())
}