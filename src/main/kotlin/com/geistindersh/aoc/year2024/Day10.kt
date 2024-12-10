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

    private fun Point2D.trailScore(): Int {
        val start = this to data[this]!!
        val queue = PriorityQueue<Pair<Point2D, Int>>(compareBy { it.second }).apply { add(start) }
        val seen = mutableSetOf<Point2D>()

        while (queue.isNotEmpty()) {
            val (point, grade) = queue.poll()
            if (point in seen) continue
            seen.add(point)
            val toAdd = point.neighbors().filter { it in data && data[it]!! == grade + 1 }.map { it to data[it]!! }
            queue.addAll(toAdd)
        }

        return seen.filter { data[it]!! == 9 }.size
    }

    fun part1(): Int =
        data.filterValues { it == 0 }.keys.sumOf {
            val score = it.trailScore()
            println("$it\t$score")
            score
        }

    fun part2() = 0
}

fun day10() {
    val day = Day10(DataFile.Part1)
    report(2024, 10, day.part1(), day.part2())
}
