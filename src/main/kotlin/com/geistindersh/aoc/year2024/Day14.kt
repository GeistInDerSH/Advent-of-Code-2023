package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.math.floorModulo
import com.geistindersh.aoc.helper.report

class Day14(
    dataFile: DataFile,
) {
    private val robots = fileToStream(2024, 14, dataFile).map(Robot::from).toList()

    private data class Robot(
        val col: Int,
        val row: Int,
        val colVelocity: Int,
        val rowVelocity: Int,
        val height: Int = 103,
        val width: Int = 101,
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
            private val NUMBER_REGEX = "-?[0-9]+".toRegex()

            fun from(line: String) =
                NUMBER_REGEX.findAll(line).map { it.value.toInt() }.toList().let {
                    Robot(it[0], it[1], it[2], it[3])
                }
        }
    }

    private fun robotsWithBounds(
        height: Int,
        width: Int,
    ) = robots.map { it.copy(height = height, width = width) }

    fun part1(
        height: Int,
        width: Int,
    ) = robotsWithBounds(height, width)
        .mapNotNull { it.next(100).quadrant() }
        .groupingBy { it }
        .eachCount()
        .values
        .reduce(Int::times)

    fun part1() = part1(height = 103, width = 101)

    fun part2(
        height: Int,
        width: Int,
    ) = generateSequence(0 to robotsWithBounds(height, width)) { (round, bots) -> round + 1 to bots.map { it.next() } }
        .first { (_, bots) -> bots.map { it.row to it.col }.toSet().size == robots.size }
        .first

    fun part2() = part2(height = 103, width = 101)
}

fun day14() {
    val day = Day14(DataFile.Part1)
    report(2024, 14, day.part1(), day.part2())
}
