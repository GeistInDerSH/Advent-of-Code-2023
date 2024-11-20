package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day3(
    dataFile: DataFile,
) {
    private val lines = fileToStream(2022, 3, dataFile).toList()
    private val scores =
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
            .mapIndexed { index, c -> Pair(c, index + 1) }
            .toMap()

    fun part1(): Int =
        lines
            .flatMap { line ->
                val midpoint = line.length / 2
                val s1 = line.take(midpoint).toSet()
                val s2 = line.takeLast(midpoint).toSet()
                s1.intersect(s2)
            }.fold(0) { acc, c -> acc + scores[c]!! }

    fun part2(): Int =
        lines
            .windowed(3, 3)
            .map { lst ->
                lst[0]
                    .toSet()
                    .intersect(lst[1].toSet())
                    .intersect(lst[2].toSet())
            }.sumOf { set -> set.sumOf { scores[it]!! } }
}

fun day3() {
    val day = Day3(DataFile.Part1)
    report(2022, 3, day.part1(), day.part2())
}
