package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import kotlin.math.absoluteValue

class Day3(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val wires =
        fileToStream(2019, 3, dataFile)
            .map { it.split(",") }
            .toList()
    private val expanded =
        wires.map { wire ->
            wire.flatMap {
                val direction =
                    when (it.first()) {
                        'R' -> Direction.East
                        'U' -> Direction.South
                        'D' -> Direction.North
                        'L' -> Direction.West
                        else -> throw IllegalStateException("Unexpected direction: $it")
                    }
                val count = it.drop(1).toInt()
                (0..<count).map { direction }
            }
        }

    private fun List<Direction>.getPath(): Set<Point2D> {
        var pos = START
        val path = mutableSetOf<Point2D>()
        for (dir in this) {
            pos += dir
            path.add(pos)
        }
        return path
    }

    override fun part1() =
        expanded
            .map { it.getPath() }
            .reduce { a, b -> a.intersect(b) }
            .minOf { it.row.absoluteValue + it.col.absoluteValue }

    override fun part2() = 0

    companion object {
        private val START = Point2D(0, 0)
    }
}

fun day3() {
    val day = Day3(DataFile.Part1)
    report(2019, 3, day.part1(), day.part2())
}
