package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2D
import kotlin.collections.sumOf

class Day12(
    dataFile: DataFile,
) {
    private val grid = fileToString(2024, 12, dataFile).toGrid2D()
    private val plots = grid.values.toSet().flatMap { it.getPlots() }

    /**
     * @return The number of points in the set
     */
    private fun Set<Point2D>.area() = this.size

    /**
     * @return The number of edges not touching a point inside the set
     */
    private fun Set<Point2D>.perimeter(): Int = this.sumOf { point -> point.neighbors().count { it !in this } }

    /**
     * Get the number of vertices for the shape defined by the set of points. Fortunately, the number of vertices
     * is equal to the number of edges for a 2D shape.
     * @return The number of vertices in the shape
     */
    private fun Set<Point2D>.vertices() =
        this
            .sumOf { point ->
                verticesToCheck
                    .count { (ns, ew, corner) ->
                        val hasNs = (point + ns) in this
                        val hasEw = (point + ew) in this
                        when {
                            !(hasNs || hasEw) -> true
                            !(hasNs && hasEw) -> false
                            else -> (point + corner) !in this
                        }
                    }
            }

    /**
     * @return All possible plots of points for the given [Char]
     */
    private fun Char.getPlots(): List<Set<Point2D>> {
        val plots = mutableListOf<Set<Point2D>>()
        val plotName = this
        val toVisit = grid.filterValues { it == plotName }.keys.toMutableSet()
        val queue = ArrayDeque<Point2D>()
        while (toVisit.isNotEmpty()) {
            // Flood fill from this point, and remove all points touching it
            queue.add(toVisit.first())
            val bucket = mutableSetOf<Point2D>()
            while (queue.isNotEmpty()) {
                val point = queue.removeFirst()
                if (point !in toVisit) continue
                toVisit.remove(point)
                bucket.add(point)
                queue.addAll(point.neighbors().filter { it in grid && grid[it]!! == plotName })
            }
            plots.add(bucket)
        }
        return plots
    }

    fun part1() = plots.sumOf { it.area() * it.perimeter() }

    fun part2() = plots.sumOf { it.area() * it.vertices() }

    companion object {
        private val verticesToCheck =
            listOf(
                Triple(Direction.North, Direction.East, Direction.North + Direction.East),
                Triple(Direction.North, Direction.West, Direction.North + Direction.West),
                Triple(Direction.South, Direction.East, Direction.South + Direction.East),
                Triple(Direction.South, Direction.West, Direction.South + Direction.West),
            )
    }
}

fun day12() {
    val day = Day12(DataFile.Part1)
    report(2024, 12, day.part1(), day.part2())
}
