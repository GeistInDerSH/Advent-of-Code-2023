package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day4(
    dataFile: DataFile,
) {
    private val data =
        fileToStream(2024, 4, dataFile)
            .flatMapIndexed { row, line ->
                line.mapIndexed { col, value -> Point2D(row, col) to value }
            }.toMap()

    private fun Point2D.hasRight() =
        (0..3)
            .map { this + Point2D(0, it) }
            .zip("XMAS".toList())
            .all { it.first in data && data[it.first] == it.second }

    private fun Point2D.hasLeft() =
        (-3..0)
            .map { this + Point2D(0, it) }
            .zip("XMAS".toList().reversed())
            .all { it.first in data && data[it.first] == it.second }

    private fun Point2D.hasSouth() =
        (0..3)
            .map { this + Point2D(it, 0) }
            .zip("XMAS".toList())
            .all { it.first in data && data[it.first] == it.second }

    private fun Point2D.hasNorth() =
        (-3..0)
            .map { this + Point2D(it, 0) }
            .zip("XMAS".toList().reversed())
            .all { it.first in data && data[it.first] == it.second }

    private fun Point2D.diagonalMatchCount(): Int {
        var count = 0
        val hasDownDiagonal =
            (0..3)
                .map { this + Point2D(it, it) }
                .zip("XMAS".toList())
                .all { it.first in data && data[it.first] == it.second }
        if (hasDownDiagonal) count += 1

        val hasDownDiagonalReverse =
            (0..3)
                .map { this + Point2D(it, -1 * it) }
                .zip("XMAS".toList())
                .all { it.first in data && data[it.first] == it.second }
        if (hasDownDiagonalReverse) count += 1

        val hasUpDiagonalReverse =
            (-3..0)
                .map { this + Point2D(it, it) }
                .zip("XMAS".toList().reversed())
                .all { it.first in data && data[it.first] == it.second }
        if (hasUpDiagonalReverse) count += 1

        val hasUpDiagonal =
            (0..3)
                .map { this + Point2D(-1 * it, it) }
                .zip("XMAS".toList())
                .all { it.first in data && data[it.first] == it.second }
        if (hasUpDiagonal) count += 1
        return count
    }

    private fun Point2D.matchCount(): Int {
        var count = 0
        if (this.hasLeft()) count += 1
        if (this.hasRight()) count += 1
        if (this.hasNorth()) count += 1
        if (this.hasSouth()) count += 1
        return count + this.diagonalMatchCount()
    }

    private fun Point2D.hasXmas(): Boolean {
        val masMas =
            listOf(Point2D(-1, -1) to 'M', Point2D(1, -1) to 'M', Point2D(0, 0) to 'A', Point2D(1, 1) to 'S', Point2D(-1, 1) to 'S')
        val masSam =
            listOf(Point2D(-1, -1) to 'M', Point2D(1, -1) to 'S', Point2D(0, 0) to 'A', Point2D(1, 1) to 'S', Point2D(-1, 1) to 'M')
        val samSam =
            listOf(Point2D(-1, -1) to 'S', Point2D(1, -1) to 'S', Point2D(0, 0) to 'A', Point2D(1, 1) to 'M', Point2D(-1, 1) to 'M')
        val samMas =
            listOf(Point2D(-1, -1) to 'S', Point2D(1, -1) to 'M', Point2D(0, 0) to 'A', Point2D(1, 1) to 'M', Point2D(-1, 1) to 'S')

        return listOf(masMas, masSam, samSam, samMas)
            .any { col ->
                col
                    .map { (this + it.first) to it.second }
                    .all { it.first in data && data[it.first] == it.second }
            }
    }

    init {
        data.filter { it.value == 'A' }.forEach { println("$it\t${it.key.hasXmas()}") }
    }

    fun part1() = data.filter { it.value == 'X' }.keys.sumOf { it.matchCount() }

    fun part2() = data.filter { it.value == 'A' }.keys.count { it.hasXmas() }
}

fun day4() {
    val day = Day4(DataFile.Part1)
    report(2024, 4, day.part1(), day.part2())
}
