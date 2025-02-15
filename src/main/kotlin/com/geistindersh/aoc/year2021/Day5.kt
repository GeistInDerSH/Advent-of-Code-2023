package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.iterators.takeWhileInclusive
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.extractPositiveIntegers

class Day5(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val lines =
        fileToStream(2021, 5, dataFile)
            .map { line ->
                val (start, end) =
                    line
                        .extractPositiveIntegers()
                        .windowed(2, 2)
                        .map { Point2D(it[0], it[1]) }
                        .toList()
                start to end
            }.toList()

    private fun List<Pair<Point2D, Point2D>>.fillInLines() =
        this
            .flatMap { (start, end) ->
                val nsDirection = if (start.row < end.row) Direction.South else Direction.North
                val ewDirection = if (start.col < end.col) Direction.East else Direction.West

                val direction =
                    when {
                        start.row == end.row -> ewDirection.toPair()
                        start.col == end.col -> nsDirection.toPair()
                        else -> nsDirection + ewDirection
                    }
                generateSequence(start) { it + direction }
                    .takeWhileInclusive { it != end }
                    .toList()
            }

    override fun part1() =
        lines
            .filter { (start, end) -> start.row == end.row || start.col == end.col }
            .fillInLines()
            .groupingBy { it }
            .eachCount()
            .count { it.value > 1 }

    override fun part2() =
        lines
            .fillInLines()
            .groupingBy { it }
            .eachCount()
            .count { it.value > 1 }
}

fun day5() {
    val day = Day5(DataFile.Part1)
    report(2021, 5, day.part1(), day.part2())
}
