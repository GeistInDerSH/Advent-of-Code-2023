package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day6(
    dataFile: DataFile,
) {
    private val data =
        fileToStream(2024, 6, dataFile)
            .flatMapIndexed { row, line ->
                line.mapIndexed { col, value -> Point2D(row, col) to value }
            }.toMap()
    private val maxColumn = data.maxOf { it.key.col }
    private val maxRow = data.maxOf { it.key.row }
    private val start = data.filterValues { it == '^' }.keys.first()

    fun part1() =
        generateSequence(start to Direction.North) { (pos, dir) ->
            val newPos = pos + dir
            if (data.getOrDefault(newPos, '.') == '#') {
                pos to dir.turnRight()
            } else {
                newPos to dir
            }
        }.map { it.first }
            .takeWhile { it in data.keys }
            .toSet()
            .count()

    fun part2() = 0
}

fun day6() {
    val day = Day6(DataFile.Part1)
    report(2024, 6, day.part1(), day.part2())
}
