package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2D

class Day18(
    dataFile: DataFile,
    private val steps: Int,
) : AoC<Int, Int> {
    private val points = fileToString(2015, 18, dataFile).toGrid2D()
    private val stuckOn =
        points.let { map ->
            val rowMax = map.keys.maxOf { it.row }
            val colMax = map.keys.maxOf { it.col }
            setOf(Point2D(0, 0), Point2D(rowMax, 0), Point2D(0, colMax), Point2D(rowMax, colMax))
        }

    private fun Map<Point2D, Char>.gameOfLife() = this.gameOfLife(emptySet())

    private fun Map<Point2D, Char>.gameOfLife(stuckOn: Set<Point2D>) =
        generateSequence(this) { currentMap ->
            currentMap.entries.associate { (k, v) ->
                val enabledNeighbors = k.neighborsAll().mapNotNull { currentMap[it] }.count { it == '#' }

                when {
                    k in stuckOn -> k to '#'
                    v == '#' && enabledNeighbors == 2 -> k to '#'
                    v == '#' && enabledNeighbors == 3 -> k to '#'
                    v == '.' && enabledNeighbors == 3 -> k to '#'
                    else -> k to '.'
                }
            }
        }

    @Suppress("unused")
    private fun Map<Point2D, Char>.print() {
        val rows = this.keys.maxOf { it.row }
        val cols = this.keys.maxOf { it.col }
        for (row in 0..rows) {
            for (col in 0..cols) {
                print(this[Point2D(row, col)]!!)
            }
            println()
        }
        println()
    }

    override fun part1() =
        points
            .gameOfLife()
            .drop(steps)
            .first()
            .count { it.value == '#' }

    override fun part2() =
        points
            .toMutableMap()
            .also {
                stuckOn.forEach { point ->
                    it[point] = '#'
                }
            }.gameOfLife(stuckOn)
            .drop(steps)
            .first()
            .count { it.value == '#' }
}

fun day18() {
    val day = Day18(DataFile.Part1, 100)
    report(2015, 18, day.part1(), day.part2())
}
