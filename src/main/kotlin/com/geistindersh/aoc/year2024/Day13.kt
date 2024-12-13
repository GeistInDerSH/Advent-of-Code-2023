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
        val x: Int,
        val y: Int,
    )

    private data class Prize(
        val x: Int,
        val y: Int,
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

        companion object {
            private val NUMBER_REGEX = "[0-9]+".toRegex()

            fun from(str: String): ClawMachine {
                val lines = str.split("\n")
                val a =
                    NUMBER_REGEX
                        .findAll(lines[0])
                        .map { it.value.toInt() }
                        .toList()
                        .let { Button(it[0], it[1]) }
                val b =
                    NUMBER_REGEX
                        .findAll(lines[1])
                        .map { it.value.toInt() }
                        .toList()
                        .let { Button(it[0], it[1]) }
                val prize =
                    NUMBER_REGEX
                        .findAll(lines[2])
                        .map { it.value.toInt() }
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

    fun part2() = 0
}

fun day13() {
    val day = Day13(DataFile.Part1)
    report(2024, 13, day.part1(), day.part2())
}
