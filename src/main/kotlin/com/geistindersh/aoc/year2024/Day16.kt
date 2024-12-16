package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2D
import java.util.PriorityQueue

class Day16(
    dataFile: DataFile,
) {
    private val data = fileToString(2024, 16, dataFile).toGrid2D()
    private val start = data.filterValues { it == 'S' }.keys.first()
    private val end = data.filterValues { it == 'E' }.keys.first()

    private data class Path(
        val position: Point2D,
        val direction: Direction = Direction.East,
        val score: Long = 0,
        val path: List<Point2D> = emptyList(),
    )

    private fun shortestPathScore(): Long {
        val queue = PriorityQueue<Path>(compareBy { it.score }).apply { add(Path(start)) }
        val seen = mutableSetOf<Pair<Point2D, Direction>>()
        while (queue.isNotEmpty()) {
            val path = queue.poll()
            if (path.position == end) return path.score
            if (!seen.add(path.position to path.direction)) continue

            val next = path.position + path.direction
            if (next in data && data[next]!! != '#') {
                queue.add(path.copy(position = next, score = path.score + 1))
            }

            for (dir in listOf(path.direction.turnLeft(), path.direction.turnRight())) {
                queue.add(path.copy(direction = dir, score = path.score + 1000))
            }
        }
        return -1L
    }

    private fun pointsForShortestPaths(): Set<Point2D> {
        val minScore = shortestPathScore()
        val queue = ArrayDeque<Path>().apply { add(Path(start)) }
        val visited = mutableMapOf<Pair<Point2D, Direction>, Long>()
        val shortestPathPoints = mutableSetOf<Point2D>(end)
        while (queue.isNotEmpty()) {
            val path = queue.removeFirst()
            if (path.score > minScore) continue
            val key = path.position to path.direction
            if (key in visited && visited[key]!! < path.score) continue
            visited[key] = path.score
            if (path.position == end && path.score == minScore) {
                shortestPathPoints.addAll(path.path)
                continue
            }

            val next = path.position + path.direction
            if (next in data && data[next]!! != '#') {
                queue.add(path.copy(position = next, score = path.score + 1, path = path.path + path.position))
            }

            for (dir in listOf(path.direction.turnLeft(), path.direction.turnRight())) {
                queue.add(path.copy(direction = dir, score = path.score + 1000))
            }
        }
        return shortestPathPoints
    }

    fun part1() = shortestPathScore()

    fun part2() = pointsForShortestPaths().count()
}

fun day16() {
    val day = Day16(DataFile.Part1)
    report(2024, 16, day.part1(), day.part2())
}
