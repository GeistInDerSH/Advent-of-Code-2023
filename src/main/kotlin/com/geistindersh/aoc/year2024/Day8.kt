package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.iterators.pairCombinations
import com.geistindersh.aoc.helper.report

class Day8(
    dataFile: DataFile,
) {
    private val grid =
        fileToStream(2024, 8, dataFile)
            .flatMapIndexed { row, line ->
                line.mapIndexed { col, c -> Point2D(row, col) to c }
            }.toMap()
    private val frequencies = grid.values.toSet().filterNot { it == '.' }

    private fun antinodes(node: Char) =
        sequence {
            val points = grid.filterValues { it == node }.keys
            for ((a, b) in points.pairCombinations()) {
                val p = a - b
                yield(a + p)
                yield(b - p)
            }
        }

    private fun antinodesRepeated(node: Char) =
        sequence {
            val points = grid.filterValues { it == node }.keys
            yieldAll(points)
            for ((a, b) in points.pairCombinations()) {
                val p = a - b

                var incPoint = a + p
                while (incPoint in grid) {
                    yield(incPoint)
                    incPoint += p
                }

                var decPoint = b - p
                while (decPoint in grid) {
                    yield(decPoint)
                    decPoint -= p
                }
            }
        }

    fun part1() =
        frequencies
            .flatMap { antinodes(it) }
            .filter { it in grid }
            .toSet()
            .size

    fun part2() =
        frequencies
            .flatMap { antinodesRepeated(it) }
            .toSet()
            .size
}

fun day8() {
    val day = Day8(DataFile.Part1)
    report(2024, 8, day.part1(), day.part2())
}
