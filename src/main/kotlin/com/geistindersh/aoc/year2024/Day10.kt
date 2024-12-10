package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import java.util.PriorityQueue

class Day10(
    dataFile: DataFile,
) {
    private val data =
        fileToStream(2024, 10, dataFile)
            .flatMapIndexed { row, line ->
                line.mapIndexed { col, digit -> Point2D(row, col) to digit.digitToInt() }
            }.toMap()
    private val startingPoints = data.filterValues { it == 0 }.keys

    private fun Point2D.trailScore(): Int {
        val start = this to data[this]!!
        val queue = PriorityQueue<Pair<Point2D, Int>>(compareBy { it.second }).apply { add(start) }
        val seen = mutableSetOf<Point2D>()

        while (queue.isNotEmpty()) {
            val (point, grade) = queue.poll()
            if (!seen.add(point)) continue
            val nextPairs =
                point
                    .neighbors()
                    .filter { it !in seen && it in data && data[it]!! == grade + 1 }
                    .map { it to data[it]!! }
            queue.addAll(nextPairs)
        }

        return seen.count { data[it]!! == 9 }
    }

    private fun Point2D.trailCount(): Int {
        val start = this to emptySet<Point2D>()
        val queue = ArrayDeque<Pair<Point2D, Set<Point2D>>>().apply { add(start) }
        val seen = mutableSetOf<Pair<Point2D, Set<Point2D>>>()

        var count = 0
        while (queue.isNotEmpty()) {
            val head = queue.removeFirst()
            if (!seen.add(head)) continue

            val (point, path) = head
            val newPath = path.toMutableSet().apply { add(point) }
            if (data[point]!! == 9) {
                seen.add(point to newPath)
                count += 1
                continue
            }

            val nextStepUp = data[point]!! + 1
            for (nextPoint in point.neighbors().filter { it in data && data[it] == nextStepUp }) {
                val toAdd = nextPoint to newPath
                if (toAdd in seen) continue
                queue.add(toAdd)
            }
        }

        return count
    }

    fun part1() = startingPoints.sumOf { it.trailScore() }

    fun part2() = startingPoints.sumOf { it.trailCount() }
}

fun day10() {
    val day = Day10(DataFile.Part1)
    report(2024, 10, day.part1(), day.part2())
}
