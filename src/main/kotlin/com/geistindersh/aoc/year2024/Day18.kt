package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import java.util.PriorityQueue

class Day18(
    dataFile: DataFile,
    private val gridMaxRow: Int = 70,
    private val gridMaxCol: Int = 70,
) {
    private val bytePoints =
        fileToStream(2024, 18, dataFile)
            .map { it.split(",").map(String::toInt) }
            .map { (a, b) -> Point2D(a, b) }
            .toList()

    private fun Point2D.inGrid() = this.row in (0..gridMaxRow) && this.col in (0..gridMaxCol)

    private fun Point2D.isEnd() = this.row == gridMaxRow && this.col == gridMaxCol

    private fun Set<Point2D>.traverse(): Int {
        val queue = PriorityQueue<Pair<Point2D, Int>>(compareBy { it.second }).apply { add(Point2D(0, 0) to 0) }
        val seen = mutableSetOf<Point2D>()
        while (queue.isNotEmpty()) {
            val (point, score) = queue.poll()
            if (!seen.add(point)) continue
            if (point.isEnd()) return score

            for (next in point.neighbors()) {
                if (!next.inGrid()) continue
                if (next in this) continue
                queue.add(next to score + 1)
            }
        }
        return -1
    }

    fun part1(byteCount: Int = 1024) = bytePoints.take(byteCount).toSet().traverse()

    fun part2() =
        generateSequence(bytePoints.count() to bytePoints) { (it.first - 1) to bytePoints.take(it.first) }
            .first { it.second.toSet().traverse() != -1 }
            .first
            .let { bytePoints[it + 1] }
}

fun day18() {
    val day = Day18(DataFile.Part1)
    report(2024, 18, day.part1(), day.part2())
}
