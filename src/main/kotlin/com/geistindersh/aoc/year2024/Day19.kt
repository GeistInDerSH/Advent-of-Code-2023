package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day19(
    private val patterns: List<String>,
    private val designs: List<String>,
) {
    constructor(dataFile: DataFile) : this(fileToString(2024, 19, dataFile))
    constructor(input: String) : this(
        input.substringBefore("\n").split(',').map { it.trim() },
        input.substringAfter("\n").split("\n"),
    )

    fun part1() = 0

    fun part2() = 0
}

fun day19() {
    val day = Day19(DataFile.Part1)
    report(2024, 19, day.part1(), day.part2())
}
