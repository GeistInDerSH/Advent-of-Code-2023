package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2D

class Day16(
    dataFile: DataFile,
) {
    private val data = fileToString(2024, 16, dataFile).toGrid2D()
    private val start = data.filterValues { it == 'S' }.keys.first()
    private val end = data.filterValues { it == 'E' }.keys.first()

    private data class Path(
        val position: Point2D,
        val direction: Direction,
        val path: List<Point2D>,
        val score: Long,
    )

    private fun trace(path: List<Point2D>) {
        val maxRow = data.keys.maxOf { it.row }
        val maxCol = data.keys.maxOf { it.col }
        var buff = Array(maxRow + 1) { CharArray(maxCol + 1) { ' ' } }
        for (row in 0..maxRow) {
            for (col in 0..maxCol) {
                val point = Point2D(row, col)
                buff[row][col] = data[point]!!
            }
        }
        for ((row, col) in path) {
            buff[row][col] = '0'
        }
        val str = buff.joinToString("\n") { it.joinToString("") }
        println(str)
    }

    private fun traversePath(): Set<Path> {
        val queue = ArrayDeque<Path>().apply { add(Path(start, Direction.East, emptyList(), 0)) }
        val seen = mutableMapOf<Point2D, Long>()
        val pathReachingEnd = mutableSetOf<Path>()

        while (queue.isNotEmpty()) {
            val path = queue.removeFirst()
            if (path.position == end) {
                println(path)
                pathReachingEnd.add(path)
                continue
            }
            if (path.position in seen && seen[path.position]!! < path.score) continue
            seen[path.position] = path.score

            val reverse = path.direction.turnAround()
            val validDirections = Direction.entries.filter { it != reverse }
            val newPath = path.path + path.position
            for (dir in validDirections) {
                val next = path.position + dir
                if (next !in data || data[next] == '#') continue
                val newScore =
                    when (dir) {
                        path.direction -> path.score + 1
                        else -> path.score + 1001
                    }
                queue.add(Path(next, dir, newPath, newScore))
            }
        }

        for (p in pathReachingEnd) {
            println()
            println(p)
            trace(p.path)
            println()
        }

        return pathReachingEnd
    }

    fun part1() = traversePath().minOf { it.score }

    fun part2() = 0
}

fun day16() {
    val day = Day16(DataFile.Part1)
    report(2024, 16, day.part1(), day.part2())
}
