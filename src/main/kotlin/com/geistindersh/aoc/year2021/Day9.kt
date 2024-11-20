package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day9(
    dataFile: DataFile,
) {
    private val heightMap =
        fileToStream(2021, 9, dataFile)
            .flatMapIndexed { row, s ->
                s.mapIndexed { col, c -> Point2D(row, col) to c.digitToInt() }
            }.toMap()

    private fun findLowPoints() =
        heightMap
            .entries
            .filter { (point, value) ->
                point
                    .neighbors()
                    .mapNotNull { heightMap[it] }
                    .minBy { it } > value
            }

    fun part1() = findLowPoints().sumOf { it.value + 1 }

    fun part2(): Int {
        val lowPoints = findLowPoints().map { it.key }
        val basinSizes = mutableListOf<Int>()

        for (low in lowPoints) {
            val queue = ArrayDeque<Point2D>().apply { add(low) }
            val basin = mutableSetOf<Point2D>()
            while (queue.isNotEmpty()) {
                val point = queue.removeFirst()
                if (point in basin) continue
                basin.add(point)

                val currentHeight = heightMap[point]!!
                if (currentHeight == 8) continue
                val toAdd =
                    point
                        .neighbors()
                        .mapNotNull { if (it in heightMap) it to heightMap[it]!! else null }
                        .filter { (_, height) -> height - 2 <= currentHeight && height != 9 }
                        .map { it.first }
                        .filter { it !in basin }
                queue.addAll(toAdd)
            }

            basinSizes.add(basin.size)
        }

        return basinSizes.sorted().takeLast(3).reduce(Int::times)
    }
}

fun day9() {
    val day = Day9(DataFile.Part1)
    report(2021, 9, day.part1(), day.part2())
}
