package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day6(
    dataFile: DataFile,
) {
    private val responses =
        fileToString(2020, 6, dataFile)
            .split("\n\n")
            .map { Responses(it.split("\n")) }
            .toList()

    private data class Responses(
        val people: List<String>,
    ) {
        val yesAnswers = people.flatMap { it.toSet() }.groupingBy { it }.eachCount()
    }

    fun part1() = responses.sumOf { it.yesAnswers.size }

    fun part2() =
        responses
            .flatMap { resp -> resp.yesAnswers.entries.filter { it.value == resp.people.size } }
            .count()
}

fun day6() {
    val day = Day6(DataFile.Part1)
    report(2020, 6, day.part1(), day.part2())
}
