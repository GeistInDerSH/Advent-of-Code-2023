package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day24(
    lines: List<String>,
) {
    constructor(dataFile: DataFile) : this(fileToStream(2022, 24, dataFile).toList())

    private val storms =
        lines
            .flatMapIndexed { row, line ->
                line.mapIndexedNotNull { col, value ->
                    val point = Point2D(row, col)
                    when (value) {
                        '^' -> point to Direction.North
                        '>' -> point to Direction.East
                        'v' -> point to Direction.South
                        '<' -> point to Direction.West
                        else -> null
                    }
                }
            }
    private val start = Point2D(0, lines.first().indexOf('.'))
    private val end = Point2D(lines.lastIndex, lines.last().indexOf('.'))
    private val mapEnd = Point2D(lines.lastIndex, lines.last().lastIndex)

    private fun moveStorms(storms: List<Pair<Point2D, Direction>>): List<Pair<Point2D, Direction>> =
        storms
            .map { (point, dir) ->
                val newPoint = point + dir
                val pos =
                    when {
                        newPoint.row == 0 -> Point2D(mapEnd.row - 1, newPoint.col)
                        newPoint.col == 0 -> Point2D(newPoint.row, mapEnd.col - 1)
                        newPoint.row == mapEnd.row -> Point2D(1, newPoint.col)
                        newPoint.col == mapEnd.col -> Point2D(newPoint.row, 1)
                        else -> newPoint
                    }
                pos to dir
            }

    private fun isInBounds(point: Point2D) = point.row in 1..<mapEnd.row && point.col in 1..<mapEnd.col

    private fun navigate(
        start: Point2D,
        end: Point2D,
        initialStorms: List<Pair<Point2D, Direction>>,
    ): Pair<Int, List<Pair<Point2D, Direction>>> {
        var minutes = 0
        var storms = initialStorms
        var locations = setOf(start)

        while (true) {
            minutes += 1
            storms = moveStorms(storms)
            val unsafeSpots = storms.map { it.first }.toSet()
            locations =
                buildSet {
                    for (location in locations) {
                        for (direction in Direction.entries) {
                            val move = location + direction
                            if (move == end) return minutes to storms
                            if (move !in unsafeSpots && isInBounds(move)) add(move)
                        }
                        if (location !in unsafeSpots && isInBounds(location)) add(location)
                        if (location == start) add(location)
                    }
                }
        }
    }

    fun part1(): Int = navigate(start, end, storms).first

    fun part2(): Int {
        val (t1, s1) = navigate(start, end, storms)
        val (t2, s2) = navigate(end, start, s1)
        val t3 = navigate(start, end, s2).first
        return t1 + t2 + t3
    }
}

fun day24() {
    val day = Day24(DataFile.Part1)
    report(2022, 24, day.part1(), day.part2())
}
