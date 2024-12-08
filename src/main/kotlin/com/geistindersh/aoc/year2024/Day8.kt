package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.iterators.pairCombinations
import com.geistindersh.aoc.helper.report

class Day8(
    dataFile: DataFile,
) {
    private val data =
        fileToStream(2024, 8, dataFile)
            .flatMapIndexed { row, line ->
                line.mapIndexed { col, c -> Point2D(row, col) to c }
            }.toMap()
    private val frequencies = data.values.toSet() - '.'
    private val maxRow = data.keys.maxOf { it.row }
    private val maxCol = data.keys.maxOf { it.col }

    private fun antinodes(node: Char): Set<Point2D> {
        val points = data.filterValues { it == node }.keys.toList()
        return buildSet {
            for ((a, b) in points.pairCombinations()) {
                val rowDist = a.row - b.row
                val colDist = a.col - b.col
                val p = Point2D(rowDist, colDist)
                add(a + p)
                add(b - p)
            }
        }
    }

    fun part1() =
        frequencies
            .flatMap { antinodes(it) }
            .filter { it.row in 0..maxRow && it.col in 0..maxCol }
            .toSet()
            .size

    fun part2() = 0
}

fun day8() {
    val day = Day8(DataFile.Part1)
    report(2024, 8, day.part1(), day.part2())
}
