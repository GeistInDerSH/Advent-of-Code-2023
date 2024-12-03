package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day3(
    dataFile: DataFile,
) {
    private val mulRegex = "mul\\(([0-9]+),([0-9]+)\\)".toRegex()
    private val data =
        fileToStream(2024, 3, dataFile)
            .flatMap { line ->
                mulRegex
                    .findAll(line)
                    .map {
                        it.groups.mapNotNull { g -> g?.value?.toLongOrNull() }.reduce(Long::times)
                    }.toList()
            }.toList()

    fun part1() = data.reduce(Long::plus)

    fun part2() = 0
}

fun day3() {
    val day = Day3(DataFile.Part1)
    report(2024, 3, day.part1(), day.part2())
}
