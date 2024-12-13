package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day13(
    dataFile: DataFile,
) {
    private val data =
        fileToString(2024, 13, dataFile)
            .split("\n\n")
            .map { ClawMachine.from(it) }
            .toList()

    private data class Button(
        val x: Long,
        val y: Long,
    )

    private data class Prize(
        val x: Long,
        val y: Long,
    )

    private data class ClawMachine(
        val a: Button,
        val b: Button,
        val prize: Prize,
    ) {
        fun tryGetPrize(): Pair<Int, Int>? {
            for (aPresses in 0..<100) {
                val ax = a.x * aPresses
                val ay = a.y * aPresses
                for (bPresses in 0..<100) {
                    val bx = b.x * bPresses
                    val by = b.y * bPresses
                    if (ax + bx == prize.x && ay + by == prize.y) {
                        return aPresses to bPresses
                    }
                }
            }
            return null
        }

        private fun determinate(
            a: Long,
            b: Long,
            c: Long,
            d: Long,
        ) = (a * d) - (b * c)

        fun tryGetPrizeNoLimit(): Pair<Long, Long>? {
            val div = determinate(a.x, a.y, b.x, b.y)
            val x = determinate(prize.x, b.x, prize.y, b.y)
            val y = determinate(a.x, prize.x, a.y, prize.y)
            if (x % div != 0L || y % div != 0L) return null
            return (x / div) to (y / div)
        }

        companion object {
            private val NUMBER_REGEX = "[0-9]+".toRegex()

            fun from(str: String): ClawMachine {
                val lines = str.split("\n")
                val a =
                    NUMBER_REGEX
                        .findAll(lines[0])
                        .map { it.value.toLong() }
                        .toList()
                        .let { Button(it[0], it[1]) }
                val b =
                    NUMBER_REGEX
                        .findAll(lines[1])
                        .map { it.value.toLong() }
                        .toList()
                        .let { Button(it[0], it[1]) }
                val prize =
                    NUMBER_REGEX
                        .findAll(lines[2])
                        .map { it.value.toLong() }
                        .toList()
                        .let { Prize(it[0], it[1]) }
                return ClawMachine(a, b, prize)
            }
        }
    }

    fun part1() =
        data
            .mapNotNull { it.tryGetPrize() }
            .map { (a, b) -> a * 3 + b }
            .sumOf { it }

    fun part2() =
        data
            .map { it.copy(prize = Prize(it.prize.x + 10000000000000L, it.prize.y + 10000000000000L)) }
            .mapNotNull { it.tryGetPrizeNoLimit() }
            .map { (a, b) -> a * 3 + b }
            .sumOf { it }
}

fun day13() {
    val day = Day13(DataFile.Part1)
    report(2024, 13, day.part1(), day.part2())
}
