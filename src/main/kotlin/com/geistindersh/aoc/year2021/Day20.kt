package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.binary.setBit
import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2D

class Day20(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val startingImage =
        fileToString(2021, 20, dataFile).let {
            val (lookupTable, rawImage) = it.split("\n\n")
            val points = rawImage.toGrid2D { if (it == '#') 1 else 0 }
            Image(points, lookupTable, 0)
        }

    private data class Image(
        val points: Map<Point2D, Int>,
        val lookupTable: String,
        val default: Int,
    ) {
        private val minRow = points.keys.minOf { it.row }
        private val maxRow = points.keys.maxOf { it.row }
        private val minCol = points.keys.minOf { it.col }
        private val maxCol = points.keys.maxOf { it.col }

        private fun orderedNeighbors() =
            listOf(
                Direction.North + Direction.West,
                Direction.North.toPair(),
                Direction.North + Direction.East,
                Direction.West.toPair(),
                Pair(0, 0),
                Direction.East.toPair(),
                Direction.South + Direction.West,
                Direction.South.toPair(),
                Direction.South + Direction.East,
            )

        fun next(): Image {
            val newPoints =
                (minRow - 1..maxRow + 1)
                    .flatMap { row ->
                        (minCol - 1..maxCol + 1).map { col -> Point2D(row, col) }
                    }.associateWith { point ->
                        val index =
                            orderedNeighbors()
                                .reversed() // set the points to the south first in the int
                                .map { point + it }
                                .map { points.getOrDefault(it, default) }
                                .foldIndexed(0) { idx, acc, bit -> acc.setBit(idx, bit) }
                        if (lookupTable[index] == '#') 1 else 0
                    }
            val charPos = if (default == 0) 0 else lookupTable.lastIndex
            val default = if (lookupTable[charPos] == '#') 1 else 0
            return this.copy(points = newPoints, default = default)
        }
    }

    private fun pixelsLitAfterSteps(steps: Int): Int =
        generateSequence(startingImage) { it.next() }
            .drop(steps)
            .first()
            .points
            .values
            .sum()

    override fun part1() = pixelsLitAfterSteps(2)

    override fun part2() = pixelsLitAfterSteps(50)
}

fun day20() {
    val day = Day20(DataFile.Part1)
    report(2021, 20, day.part1(), day.part2())
}
