package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day3(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val wires =
        fileToStream(2019, 3, dataFile)
            .map { line ->
                line
                    .split(",")
                    .flatMap {
                        val direction = Direction.tryFromChar(it.first()) ?: throw IllegalArgumentException("Invalid direction: $it")
                        val count = it.drop(1).toInt()
                        (0..<count).map { direction }
                    }
            }.toList()

    private fun List<Direction>.getPath(): List<Point2D> {
        var pos = START
        val path = mutableListOf<Point2D>()
        for (dir in this) {
            pos += dir
            path.add(pos)
        }
        return path
    }

    private fun List<List<Direction>>.getIntersection() = wires.map { it.getPath().toSet() }.reduce { a, b -> a.intersect(b) }

    override fun part1() = wires.getIntersection().minOf { START.manhattanDistance(it) }

    override fun part2() =
        wires
            .map { it.getPath() }
            .let { paths ->
                wires
                    .getIntersection()
                    .minOf { pos -> paths.sumOf { it.indexOf(pos) + 1 } }
            }

    companion object {
        private val START = Point2D(0, 0)
    }
}

fun day3() {
    val day = Day3(DataFile.Part1)
    report(2019, 3, day.part1(), day.part2())
}
