package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import kotlin.collections.mutableSetOf

class Day23(
    dataFile: DataFile,
) {
    private val lanConnections: Map<String, Set<String>> =
        fileToStream(2024, 23, dataFile)
            .flatMap {
                val (l, r) = it.split("-")
                listOf(l to r, r to l)
            }.fold(mutableMapOf<String, MutableSet<String>>()) { map, pair ->
                val set = map.getOrDefault(pair.first, mutableSetOf<String>())
                set.add(pair.second)
                map[pair.first] = set
                map
            }

    fun part1() = 0

    fun part2() = 0
}

fun day23() {
    val day = Day23(DataFile.Part1)
    report(2024, 23, day.part1(), day.part2())
}
