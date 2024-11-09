package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day16(dataFile: DataFile) {
    private val fields: Map<String, Set<Int>>
    private val userTickets: List<Int>
    private val nearbyTickets: List<Int>

    init {
        val data = fileToString(2020, 16, dataFile).split("\n\n")
        fields = data[0]
            .split("\n")
            .associate { line ->
                val name = line.substringBefore(": ")
                val ranges = NUMBER_REGEX
                    .findAll(line)
                    .map { it.value.toInt() }
                    .windowed(2, 2) { it.first()..it.last() }
                    .flatMap { it.toSet() }
                    .toSet()
                name to ranges
            }
        userTickets = NUMBER_REGEX.findAll(data[1]).map { it.value.toInt() }.toList()
        nearbyTickets = NUMBER_REGEX.findAll(data[2]).map { it.value.toInt() }.toList()
    }

    fun part1() = nearbyTickets
        .filter { ticket -> fields.values.none { ticket in it } }
        .reduce(Int::plus)

    fun part2() = 0

    companion object {
        private val NUMBER_REGEX = "[0-9]+".toRegex()
    }
}

fun day16() {
    val day = Day16(DataFile.Part1)
    report(2020, 16, day.part1(), day.part2())
}
