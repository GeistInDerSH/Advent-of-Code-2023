package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2D

class Day6(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val grid = fileToString(2024, 6, dataFile).toGrid2D()
    private val start = grid.filterValues { it == '^' }.keys.first()

    private fun hasLoop(newObstacle: Point2D): Boolean {
        val memory = HashSet<Pair<Point2D, Direction>>(4096)
        var pos = start
        var dir = Direction.North
        while (true) {
            if (!memory.add(pos to dir)) return true
            val next = pos + dir
            if (next !in grid) return false
            if (next == newObstacle || grid[next] == '#') {
                dir = dir.turnRight()
            } else {
                pos = next
            }
        }
    }

    private fun traverse() =
        generateSequence(start to Direction.North) { (pos, dir) ->
            val newPos = pos + dir
            if (grid.getOrDefault(newPos, '.') == '#') {
                pos to dir.turnRight()
            } else {
                newPos to dir
            }
        }.map { it.first }
            .takeWhile { it in grid }
            .toSet()

    override fun part1() = traverse().count()

    override fun part2() =
        traverse()
            .parallelStream()
            .mapToInt { if (hasLoop(it)) 1 else 0 }
            .reduce(Int::plus)
            .orElseThrow { IllegalStateException("Failed to determine loops for 2024 Day 6 Part 2") }
}

fun day6() {
    val day = Day6(DataFile.Part1)
    report(2024, 6, day.part1(), day.part2())
}
