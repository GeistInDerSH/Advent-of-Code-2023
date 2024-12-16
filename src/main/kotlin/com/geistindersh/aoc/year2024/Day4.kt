package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2D

class Day4(
    dataFile: DataFile,
) {
    private val data = fileToString(2024, 4, dataFile).toGrid2D()

    private fun Point2D.matchCount() =
        PART_1_OPTIONS
            .count { points ->
                points
                    .map { this + it }
                    .zip("XMAS".toList())
                    .all { it.first in data && data[it.first] == it.second }
            }

    private fun Point2D.hasXmas() =
        PART_2_OPTIONS
            .any { points ->
                points
                    .map { this + it.first to it.second }
                    .all { it.first in data && data[it.first] == it.second }
            }

    fun part1() =
        data
            .filter { it.value == 'X' }
            .keys
            .sumOf { it.matchCount() }

    fun part2() =
        data
            .filter { it.value == 'A' }
            .keys
            .count { it.hasXmas() }

    companion object {
        private val PART_1_OPTIONS =
            listOf(
                // Horizontal: Right
                (0..3).map { Point2D(0, it) },
                // Horizontal: Left (reversed)
                (0..3).map { Point2D(0, -1 * it) },
                // Vertical: Below
                (0..3).map { Point2D(it, 0) },
                // Vertical: Above (reversed)
                (0..3).map { Point2D(-1 * it, 0) },
                // Diagonal: Down right
                (0..3).map { Point2D(it, it) },
                // Diagonal: Down left
                (0..3).map { Point2D(it, -1 * it) },
                // Diagonal: Up left (reversed)
                (0..3).map { Point2D(-1 * it, it) },
                // Diagonal: Up right
                (0..3).map { Point2D(-1 * it, -1 * it) },
            )

        private val PART_2_OPTIONS =
            run {
                val up = (-1..1).zip((-1..1).reversed()).map { Point2D(it.first, it.second) }
                val down = (-1..1).map { Point2D(it, it) }
                val mas = "MAS".toList()
                val sam = "SAM".toList()

                listOf(
                    down.zip(mas) + up.zip(mas),
                    down.zip(mas) + up.zip(sam),
                    down.zip(sam) + up.zip(sam),
                    down.zip(sam) + up.zip(mas),
                )
            }
    }
}

fun day4() {
    val day = Day4(DataFile.Part1)
    report(2024, 4, day.part1(), day.part2())
}
