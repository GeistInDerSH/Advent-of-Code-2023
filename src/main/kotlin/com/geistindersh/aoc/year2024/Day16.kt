package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2D
import kotlin.math.min

class Day16(
    dataFile: DataFile,
) {
    private val data = fileToString(2024, 16, dataFile).toGrid2D()
    private val start = data.filterValues { it == 'S' }.keys.first()
    private val end = data.filterValues { it == 'E' }.keys.first()

    private data class Path(
        val position: Point2D,
        val direction: Direction,
        val path: Set<Point2D>,
        val score: Long,
    )

    private fun traversePath(): Set<Path> {
        val queue = ArrayDeque<Path>().apply { add(Path(start, Direction.East, emptySet(), 0)) }
        val seen = mutableMapOf<Point2D, Long>()
        val pathReachingEnd = mutableSetOf<Path>()

        while (queue.isNotEmpty()) {
            val path = queue.removeFirst()
            if (path.position == end) {
                pathReachingEnd.add(path.copy(path = path.path + end))
                seen[end] = min(path.score, seen.getOrDefault(path.position, Long.MAX_VALUE))
                queue.removeIf { it.score > seen[end]!! }
                continue
            }
            if (end in seen && seen[end]!! < path.score) continue
            if (path.position in seen && seen[path.position]!! < path.score) continue
            seen[path.position] = path.score

            val reverse = path.direction.turnAround()
            val validDirections = Direction.entries.filter { it != reverse }
            val newPath = path.path + path.position
            for (dir in validDirections) {
                val next = path.position + dir
                if (next in newPath) continue
                if (data[next]!! == '#') continue
                if (dir != path.direction) {
                    seen[path.position] = path.score + 1000
                }
                val newScore = path.score + if (dir == path.direction) 1 else 1001
                queue.add(Path(next, dir, newPath, newScore))
            }
        }

        val minScore = pathReachingEnd.minOf { it.score }
        return pathReachingEnd.filter { it.score == minScore }.toSet()
    }

    fun part1() = traversePath().minOf { it.score }

    fun part2() =
        traversePath()
            .flatMap { it.path }
            .toSet()
            .count()
}

fun day16() {
    val day = Day16(DataFile.Part1)
    report(2024, 16, day.part1(), day.part2())
}
