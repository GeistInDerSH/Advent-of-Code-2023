package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day24(lines: List<String>) {
    constructor(dataFile: DataFile) : this(fileToStream(2022, 24, dataFile).toList())

    private val storms = lines
        .flatMapIndexed { row, line ->
            line.mapIndexedNotNull { col, value ->
                val point = Point(row, col)
                when (value) {
                    '^' -> point to Direction.North
                    '>' -> point to Direction.East
                    'v' -> point to Direction.South
                    '<' -> point to Direction.West
                    else -> null
                }
            }
        }
    private val start = Point(0, lines.first().indexOf('.'))
    private val end = Point(lines.lastIndex, lines.last().indexOf('.'))
    private val mapEnd = Point(lines.lastIndex, lines.last().lastIndex)

    private fun moveStorms(storms: List<Pair<Point, Direction>>): List<Pair<Point, Direction>> {
        return storms
            .map { (point, dir) ->
                val newPoint = point + dir
                val pos = when {
                    newPoint.row == 0 -> Point(mapEnd.row - 1, newPoint.col)
                    newPoint.col == 0 -> Point(newPoint.row, mapEnd.col - 1)
                    newPoint.row == mapEnd.row -> Point(1, newPoint.col)
                    newPoint.col == mapEnd.col -> Point(newPoint.row, 1)
                    else -> newPoint
                }
                pos to dir
            }
    }

    private fun isInBounds(point: Point) = point.row in 1..<mapEnd.row && point.col in 1..<mapEnd.col

    private fun navigate(): Int {
        var minutes = 0
        var storms = storms
        var locations = setOf(start)

        while (true) {
            minutes += 1
            storms = moveStorms(storms)
            val unsafeSpots = storms.map { it.first }.toSet()
            locations = buildSet {
                for (location in locations) {
                    for (direction in Direction.entries) {
                        val move = location + direction
                        if (move == end) return minutes
                        if (move !in unsafeSpots && isInBounds(move)) add(move)
                    }
                    if (location !in unsafeSpots && isInBounds(location)) add(location)
                    if (location == start) add(location)
                }
            }
        }
    }


    fun part1() = navigate()
    fun part2() = 0
}

fun day24() {
    val day = Day24(DataFile.Part1)
    report(2022, 24, day.part1(), day.part2())
}
