package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day13(dataFile: DataFile) {
    private val data = "[0-9]+".toRegex()
        .findAll(fileToString(2020, 13, dataFile))
        .map { it.value.toInt() }
        .toList()
    private val departTimestamp = data[0]
    private val times = data.drop(1)

    fun part1() = times
        .map { it to it - (departTimestamp % it) }
        .minBy { it.second }
        .toList()
        .reduce(Int::times)

    fun part2() = 0
}

fun day13() {
    val day = Day13(DataFile.Part1)
    report(2020, 13, day.part1(), day.part2())
}
