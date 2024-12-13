package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day13(
    dataFile: DataFile,
) {
    private val games =
        fileToString(2024, 13, dataFile)
            .split("\n\n")
            .map { ClawMachine.from(it) }
            .toList()

    private data class ClawMachine(
        val ax: Long,
        val ay: Long,
        val bx: Long,
        val by: Long,
        val px: Long,
        val py: Long,
    ) {
        private fun determinate(
            a: Long,
            b: Long,
            c: Long,
            d: Long,
        ) = (a * d) - (b * c)

        fun tryGetPrize(): Pair<Long, Long> {
            val (x, y) = tryGetPrizeNoLimit()
            if (x > 100 || y > 100) return 0L to 0L
            return x to y
        }

        fun tryGetPrizeNoLimit(): Pair<Long, Long> {
            val div = determinate(ax, ay, bx, by)
            val x = determinate(px, bx, py, by)
            val y = determinate(ax, px, ay, py)
            if (x % div != 0L || y % div != 0L) return 0L to 0L
            return (x / div) to (y / div)
        }

        companion object {
            private val NUMBER_REGEX = "[0-9]+".toRegex()

            fun from(str: String): ClawMachine =
                NUMBER_REGEX
                    .findAll(str)
                    .map {
                        it.value.toLong()
                    }.toList()
                    .let { ClawMachine(it[0], it[1], it[2], it[3], it[4], it[5]) }
        }
    }

    private fun Pair<Long, Long>.score() = this.first * 3 + this.second

    fun part1() = games.sumOf { it.tryGetPrize().score() }

    fun part2() =
        games
            .map { it.copy(px = it.px + 10000000000000L, py = it.py + 10000000000000L) }
            .sumOf { it.tryGetPrizeNoLimit().score() }
}

fun day13() {
    val day = Day13(DataFile.Part1)
    report(2024, 13, day.part1(), day.part2())
}
