package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.math.floorModulo
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.extractIntegers

class Day14(
    dataFile: DataFile,
    width: Int = 101,
    height: Int = 103,
) {
    private val robots = fileToStream(2024, 14, dataFile).map { Robot.from(it, height, width) }.toList()

    private data class Robot(
        val col: Int,
        val row: Int,
        val colVelocity: Int,
        val rowVelocity: Int,
        val height: Int,
        val width: Int,
    ) {
        val heightMid = height.floorDiv(2)
        val widthMid = width.floorDiv(2)

        fun quadrant() =
            when {
                col == widthMid || row == heightMid -> null
                col < widthMid && row < heightMid -> 0
                col > widthMid && row < heightMid -> 1
                col < widthMid -> 2
                else -> 3
            }

        fun next() = next(1)

        fun next(steps: Int) =
            this.copy(
                col = (col + steps * colVelocity).floorModulo(width),
                row = (row + steps * rowVelocity).floorModulo(height),
            )

        companion object {
            fun from(
                line: String,
                height: Int,
                width: Int,
            ) = line.extractIntegers().let { Robot(it[0], it[1], it[2], it[3], height, width) }
        }
    }

    fun part1() =
        robots
            .mapNotNull { it.next(100).quadrant() }
            .groupingBy { it }
            .eachCount()
            .values
            .reduce(Int::times)

    fun part2() =
        generateSequence(0 to robots) { (round, bots) -> (round + 1) to bots.map(Robot::next) }
            .first { (_, bots) ->
                val unique = mutableSetOf<Pair<Int, Int>>()
                bots.all { unique.add(it.row to it.col) }
            }.first
}

fun day14() {
    val day = Day14(DataFile.Part1)
    report(2024, 14, day.part1(), day.part2())
}
