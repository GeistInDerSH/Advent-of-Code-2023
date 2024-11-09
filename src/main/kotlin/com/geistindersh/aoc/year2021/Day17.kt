package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.iterators.takeWhileInclusive
import com.geistindersh.aoc.helper.report

class Day17(dataFile: DataFile) {
    private val targetArea = "-?[0-9]+"
        .toRegex()
        .findAll(fileToString(2021, 17, dataFile))
        .map { it.value.toInt() }
        .windowed(2, 2) { it.sorted() }
        .map { (start, end) -> (start..end) }
        .toList()
        .let {
            val (colRange, rowRange) = it
            TargetArea(colRange, rowRange)
        }
    private val start = Point2D(0, 0)

    private data class Throw(val rowVelocity: Int, val colVelocity: Int, val point: Point2D) {
        fun next(): Throw {
            val x = (rowVelocity - 1).coerceAtLeast(0)
            val y = colVelocity - 1
            val point = Point2D(point.row + colVelocity, point.col + rowVelocity)
            return Throw(x, y, point)
        }
    }

    private class TargetArea(val colRange: IntRange, val rowRange: IntRange) {
        val minRowVelocity = minimumRowVelocity()

        fun contains(toss: Throw) = toss.point.row in rowRange && toss.point.col in colRange
        fun hasOvershot(toss: Throw): Boolean = toss.point.row < rowRange.first

        fun minimumRowVelocity() = (0..Int.MAX_VALUE)
            .takeWhileInclusive { (0..it).sum() <= colRange.first }
            .last()

        fun possibleRowVelocityRange() = minRowVelocity..colRange.last

        fun maxHeightLandingInBoundsOrNull(point: Point2D, colVelocity: Int) =
            maxHeightLandingInBoundsOrNull(point, minRowVelocity, colVelocity)

        fun maxHeightLandingInBoundsOrNull(point: Point2D, rowVelocity: Int, colVelocity: Int): Int? {
            val landed = mutableSetOf<Point2D>()
            var shot = Throw(rowVelocity, colVelocity, point)
            var landsInArea = false
            while (!hasOvershot(shot)) {
                shot = shot.next()
                landed.add(shot.point)
                if (contains(shot)) landsInArea = true
            }
            return if (landsInArea) landed.maxOf { it.row } else null
        }
    }

    fun part1() = (0..targetArea.colRange.last * 2)
        .mapNotNull { targetArea.maxHeightLandingInBoundsOrNull(start, colVelocity = it) }
        .maxOf { it }

    fun part2() = targetArea
        .possibleRowVelocityRange()
        .flatMap { vRow ->
            (targetArea.rowRange.first..targetArea.colRange.last * 2)
                .mapNotNull { vCol -> targetArea.maxHeightLandingInBoundsOrNull(start, vRow, vCol) }
        }
        .count()
}

fun day17() {
    val day = Day17(DataFile.Part1)
    report(2021, 17, day.part1(), day.part2())
}
