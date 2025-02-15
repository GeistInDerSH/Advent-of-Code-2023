package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.iterators.pairCombinations
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2D

class Day8(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val grid = fileToString(2024, 8, dataFile).toGrid2D()
    private val frequencies = grid.values.toSet().filterNot { it == '.' }

    private fun antinodes(node: Char) =
        sequence {
            val points = grid.filterValues { it == node }.keys.pairCombinations()
            for ((a, b) in points) {
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

    override fun part1() =
        frequencies
            .flatMap(::antinodes)
            .filter { it in grid }
            .toSet()
            .size

    override fun part2() =
        frequencies
            .flatMap(::antinodesRepeated)
            .toSet()
            .size
}

fun day8() {
    val day = Day8(DataFile.Part1)
    report(2024, 8, day.part1(), day.part2())
}
