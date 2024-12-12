package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day12(
    dataFile: DataFile,
) {
    private val grid =
        fileToStream(2024, 12, dataFile)
            .flatMapIndexed { row, line ->
                line.mapIndexed { col, value -> Point2D(row, col) to value }
            }.toMap()
    private val plants = grid.values.toSet()

    private fun Set<Point2D>.area() = this.size

    private fun Set<Point2D>.perimeter(): Int {
        val char = grid[this.first()]!!
        return this.sumOf { point ->
            4 - point.neighbors().count { it in grid && grid[it]!! == char }
        }
    }

    private fun Set<Point2D>.sides(): Int {
        var corners = 0

        for (point in this) {
            for (ns in listOf(Direction.North, Direction.South)) {
                for (ew in listOf(Direction.East, Direction.West)) {
                    val nsIn = point + ns in this
                    val ewIn = point + ew in this
                    if (!nsIn && !ewIn) {
                        corners += 1
                    } else if (nsIn && ewIn && point + ns + ew !in this) {
                        corners += 1
                    }
                }
            }
        }

        return corners
    }

    private fun Char.getPlots(): List<Set<Point2D>> {
        val plots = mutableListOf<Set<Point2D>>()
        val char = this
        val toVisit = grid.filterValues { it == char }.keys.toMutableSet()
        val queue = ArrayDeque<Point2D>()
        while (toVisit.isNotEmpty()) {
            queue.add(toVisit.first())
            val bucket = mutableSetOf<Point2D>()
            while (queue.isNotEmpty()) {
                val point = queue.removeFirst()
                if (point !in toVisit) continue
                toVisit.remove(point)
                bucket.add(point)
                queue.addAll(point.neighbors().filter { it in grid && grid[it]!! == char })
            }
            plots.add(bucket)
        }
        return plots
    }

    fun part1() = plants.map { it.getPlots() }.sumOf { it.sumOf { plot -> plot.area() * plot.perimeter() } }

    fun part2() = plants.map { it.getPlots() }.sumOf { it.sumOf { plot -> plot.area() * plot.sides() } }
}

fun day12() {
    val day = Day12(DataFile.Part1)
    report(2024, 12, day.part1(), day.part2())
}
