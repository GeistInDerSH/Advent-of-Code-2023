package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day11(
    dataFile: DataFile,
) {
    private val stones = fileToString(2024, 11, dataFile).split(" ")

    private fun List<String>.update() =
        this.flatMap {
            when {
                it == "0" -> listOf("1")
                it.length % 2 == 0 -> {
                    val mid = it.length / 2
                    listOf(it.substring(0, mid).toLong().toString(), it.substring(mid).toLong().toString())
                }
                else -> listOf((it.toLong() * 2024).toString())
            }
        }

    fun part1() =
        generateSequence(stones) { it.update() }
            .drop(25)
            .first()
            .count()

    fun part2() = 0
}

fun day11() {
    val day = Day11(DataFile.Part1)
    report(2024, 11, day.part1(), day.part2())
}
