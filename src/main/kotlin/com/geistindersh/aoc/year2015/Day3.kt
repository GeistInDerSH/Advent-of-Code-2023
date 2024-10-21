package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day3(dataFile: DataFile) {
    private val data = fileToString(2015, 3, dataFile)
        .mapNotNull {
            when (it) {
                '^' -> Direction.North
                'v' -> Direction.South
                '<' -> Direction.West
                '>' -> Direction.East
                else -> null
            }
        }
        .toList()

    fun part1() = data
        .fold(listOf(Pair(0, 0))) { acc, direction -> acc + (direction + acc.last()) }
        .toSet()
        .count()

    fun part2() = 0
}

fun day3() {
    val day = Day3(DataFile.Part1)
    report(2015, 3, day.part1(), day.part2())
}
