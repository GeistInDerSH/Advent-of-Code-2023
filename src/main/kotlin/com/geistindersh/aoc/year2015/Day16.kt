package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day16(
    dataFile: DataFile,
) {
    private val aunts =
        fileToStream(2015, 16, dataFile)
            .map { it.replace(",", "").replace(":", "") }
            .map {
                val pairs = it.split(" ").windowed(2, 2) { (a, b) -> a to b.toInt() }
                val aunt = pairs[0].second
                aunt to pairs.drop(1).toMap()
            }.toMap()
    private val knownInfo =
        listOf(
            "children" to 3,
            "cats" to 7,
            "samoyeds" to 2,
            "pomeranians" to 3,
            "akitas" to 0,
            "vizslas" to 0,
            "goldfish" to 5,
            "trees" to 3,
            "cars" to 2,
            "perfumes" to 1,
        )

    fun part1() =
        aunts
            .filter { (_, v) ->
                knownInfo.all { (entry, value) -> v.getOrDefault(entry, value) == value }
            }.keys
            .first()

    fun part2() =
        aunts
            .filter { (_, v) ->
                knownInfo.all { (entry, value) ->
                    val amount = v.getOrDefault(entry, null)
                    when {
                        amount == null -> true
                        entry in setOf("cats", "trees") -> amount > value
                        entry in setOf("pomeranians", "goldfish") -> amount < value
                        amount == value -> true
                        else -> false
                    }
                }
            }.keys
            .first()
}

fun day16() {
    val day = Day16(DataFile.Part1)
    report(2015, 16, day.part1(), day.part2())
}
