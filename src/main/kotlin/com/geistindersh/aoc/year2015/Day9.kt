package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.algorithms.toGraph
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day9(
    dataFile: DataFile,
) {
    private val routes =
        fileToStream(2015, 9, dataFile)
            .map {
                val parts = it.split(" ")
                Pair(parts[0], parts[2]) to parts.last().toInt()
            }.toList()
            .toGraph()

    fun part1() = routes.travelingSalesman()

    fun part2() = routes.travelingSalesman({ a, b -> a.coerceAtLeast(b) })
}

fun day9() {
    val day = Day9(DataFile.Part1)
    report(2015, 9, day.part1(), day.part2())
}
