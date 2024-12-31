package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2D
import java.util.PriorityQueue

class Day20(
    dataFile: DataFile,
    private val savings: Int,
) : AoC<Int, Int> {
    private val track = fileToString(2024, 20, dataFile).toGrid2D()
    private val start = track.filterValues { it == 'S' }.keys.first()
    private val end = track.filterValues { it == 'E' }.keys.first()

    private data class Path(
        val position: Point2D,
        val score: Int,
        val path: List<Point2D>,
    )

    private fun getShortestPath(
        start: Point2D,
        end: Point2D,
    ): List<Point2D> {
        val queue = PriorityQueue<Path>(compareBy { it.score }).apply { add(Path(start, 0, emptyList())) }
        val seen = mutableSetOf<Point2D>()
        while (queue.isNotEmpty()) {
            val path = queue.poll()
            if (path.position == end) return path.path
            if (!seen.add(path.position)) continue

            for (next in path.position.neighbors()) {
                if (track[next]!! == '#') continue
                queue.add(Path(next, path.score + 1, path.path + path.position))
            }
        }
        return emptyList()
    }

    private fun getDistancesWithCheatsMax(
        start: Point2D,
        end: Point2D,
        maxSkip: Int,
    ): Map<Int, Set<Pair<Point2D, Point2D>>> {
        val path = getShortestPath(start, end)
        val withoutCheats = path.count()
        val cheatTimes = mutableMapOf<Int, MutableSet<Pair<Point2D, Point2D>>>()
        // Because we found the shortest path, add those points to a map to note how far to the end from that point.
        // Any new points on the map after the skip will be added, otherwise we look up the distance to the end. This
        // saves from needing to recalculate that distance multiple times
        val memoryToEnd =
            path
                .withIndex()
                .associate { (idx, point) -> point to path.count() - idx }
                .toMutableMap()
                .apply { set(end, 0) }
        val neighborsQueue = ArrayDeque<Path>()
        for ((idx, point) in path.withIndex()) {
            val fromStart = idx

            val seen = mutableSetOf<Point2D>()
            neighborsQueue.add(Path(point, 0, emptyList()))

            while (neighborsQueue.isNotEmpty()) {
                val path = neighborsQueue.removeFirst()
                if (path.position !in track) continue
                if (path.score > maxSkip) continue
                if (!seen.add(path.position)) continue
                if (track[path.position]!! != '#') {
                    // We are at a position on the map that is a valid spot to race from
                    // calculate the distance to the end, or look it up if we have already been there
                    // and add the start->end point to the cheatTimes map.
                    val toEnd =
                        memoryToEnd.getOrPut(path.position) {
                            val pathToEnd = getShortestPath(path.position, end)
                            val count = pathToEnd.count()
                            for ((i, point) in pathToEnd.withIndex()) {
                                if (point in memoryToEnd) continue
                                memoryToEnd[point] = count - i
                            }
                            count
                        }

                    val timeSaved = withoutCheats - (fromStart + toEnd + path.score)
                    if (timeSaved > 0) {
                        val cheats = cheatTimes.getOrPut(timeSaved) { mutableSetOf() }
                        cheats.add(point to path.position)
                    }
                }

                for (next in path.position.neighbors()) {
                    neighborsQueue.add(Path(next, path.score + 1, emptyList()))
                }
            }
        }

        return cheatTimes
    }

    private fun solution(
        cheatTime: Int,
        savings: Int,
    ) = getDistancesWithCheatsMax(start, end, cheatTime)
        .filterKeys { it >= savings }
        .values
        .sumOf { it.count() }

    override fun part1() = solution(2, savings)

    override fun part2() = solution(20, savings)
}

fun day20() {
    val day = Day20(DataFile.Part1, 100)
    report(2024, 20, day.part1(), day.part2())
}
