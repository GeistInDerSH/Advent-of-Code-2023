package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2D

class Day9(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val heightMap = fileToString(2021, 9, dataFile).toGrid2D { it.digitToInt() }

    private fun findLowPoints() =
        heightMap
            .entries
            .filter { (point, value) ->
                point
                    .neighbors()
                    .mapNotNull { heightMap[it] }
                    .minBy { it } > value
            }

    override fun part1() = findLowPoints().sumOf { it.value + 1 }

    override fun part2(): Int {
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
