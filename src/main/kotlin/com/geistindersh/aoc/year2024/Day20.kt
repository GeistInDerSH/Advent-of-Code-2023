package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2D
import java.util.PriorityQueue

class Day20(
    dataFile: DataFile,
) {
    private val track = fileToString(2024, 20, dataFile).toGrid2D()
    private val start = track.filterValues { it == 'S' }.keys.first()
    private val end = track.filterValues { it == 'E' }.keys.first()

    private data class Path(
        val position: Point2D,
        val score: Long,
        val path: List<Point2D>,
    )

    private fun getShortestPath(
        start: Point2D,
        end: Point2D,
    ): List<Point2D> {
        val queue = PriorityQueue<Path>(compareBy { it.score }).apply { add(Path(start, 0L, emptyList())) }
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

    private fun getDistancesWithCheats(
        start: Point2D,
        end: Point2D,
    ): Map<Int, Int> {
        val path = getShortestPath(start, end)
        val withoutCheats = path.count()
        val cheatTimes = mutableMapOf<Int, Int>()
        val memoryToEnd = path.withIndex().associate { (idx, point) -> point to path.count() - idx }.toMutableMap()
        memoryToEnd[end] = 0
        for ((idx, point) in path.withIndex()) {
            val fromStart = idx

            for (dir in Direction.entries) {
//                val next = point + dir
//                if (track[next]!! != '#') continue
                val land = point + dir + dir
                if (land !in track) continue
                if (track[land]!! == '#') continue

                val toEnd =
                    memoryToEnd.getOrPut(land) {
                        val pathToEnd = getShortestPath(land, end)
                        val count = pathToEnd.count()
                        for ((i, point) in pathToEnd.withIndex()) {
                            if (point in memoryToEnd) continue
                            memoryToEnd[point] = count - i
                        }
                        count
                    }

                val timeSaved = withoutCheats - (fromStart + toEnd + 2)
                if (timeSaved <= 0) continue
//                println("Jumping from $point to $land saved: ($timing)")
                cheatTimes[timeSaved] = cheatTimes.getOrDefault(timeSaved, 0) + 1
            }
        }

        return cheatTimes
    }

    init {
        println(getShortestPath(start, end).count())
        getDistancesWithCheats(start, end).toSortedMap().forEach { println("${it.value} saves of ${it.key}s") }
    }

    fun part1() = getDistancesWithCheats(start, end).filterKeys { it >= 100 }.values.sum()

    fun part2() = 0
}

fun day20() {
    val day = Day20(DataFile.Part1)
    report(2024, 20, day.part1(), day.part2())
}
