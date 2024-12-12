package com.geistindersh.aoc.year2024

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

    private fun Set<Point2D>.perimeter() =
        this.sumOf { point ->
            val char = grid[point]!!
            4 - point.neighbors().count { it in grid && grid[it]!! == char }
        }

    private fun Char.getPlots(): List<Set<Point2D>> {
        val plots = mutableListOf<Set<Point2D>>()
        val chr = this
        val toVisit = grid.filterValues { it == chr }.keys.toMutableSet()
        val queue = ArrayDeque<Point2D>()
        while (toVisit.isNotEmpty()) {
            queue.add(toVisit.first())
            val bucket = mutableSetOf<Point2D>()
            while (queue.isNotEmpty()) {
                val point = queue.removeFirst()
                if (point !in toVisit) continue
                toVisit.remove(point)
                bucket.add(point)
                queue.addAll(point.neighbors().filter { it in grid && grid[it]!! == chr })
            }
            plots.add(bucket)
        }
        return plots
    }

    fun part1() = plants.map { it.getPlots() }.sumOf { it.sumOf { plot -> plot.area() * plot.perimeter() } }

    fun part2() = 0
}

fun day12() {
    val day = Day12(DataFile.Part1)
    report(2024, 12, day.part1(), day.part2())
}
