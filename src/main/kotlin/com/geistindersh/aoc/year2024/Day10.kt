package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2D

class Day10(
    dataFile: DataFile,
) {
    private val trailMap = fileToString(2024, 10, dataFile).toGrid2D(transform = { it.digitToInt() })
    private val paths = trailMap.filterValues { it == 0 }.keys.map { it.pathsToEnd() }

    private fun Point2D.pathsToEnd(): Set<Set<Point2D>> {
        val start = this to emptySet<Point2D>()
        val queue = ArrayDeque(listOf(start))
        val seen = mutableSetOf<Pair<Point2D, Set<Point2D>>>()
        val paths = mutableSetOf<Set<Point2D>>()

        while (queue.isNotEmpty()) {
            val head = queue.removeFirst()
            if (!seen.add(head)) continue

            val (currentPoint, traveledPath) = head
            val path = traveledPath + currentPoint
            if (trailMap[currentPoint]!! == 9) {
                paths.add(path)
                continue
            }

            val nextStepUp = trailMap[currentPoint]!! + 1
            for (point in currentPoint.neighbors()) {
                if (!(point in trailMap && trailMap[point] == nextStepUp)) continue
                val toAdd = point to path
                if (toAdd in seen) continue
                queue.add(toAdd)
            }
        }

        return paths
    }

    fun part1() = paths.sumOf { p -> p.flatMapTo(mutableSetOf()) { it }.count { trailMap[it]!! == 9 } }

    fun part2() = paths.sumOf { it.size }
}

fun day10() {
    val day = Day10(DataFile.Part1)
    report(2024, 10, day.part1(), day.part2())
}
