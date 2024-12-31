package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2D
import java.util.PriorityQueue

class Day16(
    dataFile: DataFile,
) : AoC<Long, Int> {
    private val data = fileToString(2024, 16, dataFile).toGrid2D()
    private val start = data.filterValues { it == 'S' }.keys.first()
    private val end = data.filterValues { it == 'E' }.keys.first()

    private data class Path(
        val position: Point2D,
        val direction: Direction = Direction.East,
        val score: Long = 0,
        val path: List<Point2D> = emptyList(),
    ) {
        fun generateKey() = position to direction
    }

    /**
     * Get the minimum score required to traverse from [start] to [end]
     * @param start The starting point
     * @param end The ending point
     * @return The minimum score from [start] to [end]
     */
    private fun getScoreOfShortestPath(
        start: Point2D,
        end: Point2D,
    ): Long {
        val queue = PriorityQueue<Path>(compareBy { it.score }).apply { add(Path(start)) }
        val seen = mutableSetOf<Pair<Point2D, Direction>>()
        while (queue.isNotEmpty()) {
            val path = queue.poll()
            if (path.position == end) return path.score
            if (!seen.add(path.generateKey())) continue

            val next = path.position + path.direction
            if (data[next]!! != '#') {
                queue.add(path.copy(position = next, score = path.score + 1))
            }

            for (dir in listOf(path.direction.turnLeft(), path.direction.turnRight())) {
                queue.add(path.copy(direction = dir, score = path.score + 1000))
            }
        }
        return -1L
    }

    /**
     * Get all possible shortest paths from [start] to [end]
     * @param start The starting point
     * @param end The ending point
     * @return The shortest paths
     */
    private fun getShortestPaths(
        start: Point2D,
        end: Point2D,
    ): Set<List<Point2D>> {
        val minScore = getScoreOfShortestPath(start, end)
        val queue = ArrayDeque<Path>().apply { add(Path(start)) }
        val visited = mutableMapOf<Pair<Point2D, Direction>, Long>()
        val shortestPathPoints = mutableSetOf(listOf(end))
        while (queue.isNotEmpty()) {
            val path = queue.removeFirst()
            if (path.score > minScore) continue

            val key = path.generateKey()
            val value = visited[key]
            if (value != null && path.score > value) continue
            if (value == null || path.score < value) visited[key] = path.score
            if (path.position == end && path.score == minScore) {
                shortestPathPoints.add(path.path)
                continue
            }

            val next = path.position + path.direction
            if (data[next]!! != '#') {
                queue.add(path.copy(position = next, score = path.score + 1, path = path.path + path.position))
            }

            for (dir in listOf(path.direction.turnLeft(), path.direction.turnRight())) {
                queue.add(path.copy(direction = dir, score = path.score + 1000))
            }
        }
        return shortestPathPoints
    }

    override fun part1() = getScoreOfShortestPath(start, end)

    override fun part2() = getShortestPaths(start, end).flatMapTo(mutableSetOf<Point2D>()) { it }.count()
}

fun day16() {
    val day = Day16(DataFile.Part1)
    report(2024, 16, day.part1(), day.part2())
}
