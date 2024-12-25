package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2DRemoveNull

class Day25(
    dataFile: DataFile,
) {
    private val lockAndKeys =
        fileToString(2024, 25, dataFile)
            .split("\n\n")
            .map { it.toGrid2DRemoveNull { if (it == '#') it else null }.keys }
    private val locks = lockAndKeys.filter { it.containsAll(LOCK_BASE) }
    private val keys = lockAndKeys.filter { it.containsAll(KEY_BASE) }

    fun part1() = keys.sumOf { key -> locks.count { lock -> (key.intersect(lock)).isEmpty() } }

    fun part2() = "Deliver The Cronicle!"

    companion object {
        private val LOCK_BASE = listOf(Point2D(0, 0), Point2D(0, 1), Point2D(0, 2), Point2D(0, 3), Point2D(0, 4))
        private val KEY_BASE = listOf(Point2D(6, 0), Point2D(6, 1), Point2D(6, 2), Point2D(6, 3), Point2D(6, 4))
    }
}

fun day25() {
    val day = Day25(DataFile.Part1)
    report(2024, 25, day.part1(), day.part2())
}
