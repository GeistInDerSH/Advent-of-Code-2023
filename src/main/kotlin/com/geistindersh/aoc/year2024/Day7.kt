package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.binary.concat
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day7(
    dataFile: DataFile,
) : AoC<Long, Long> {
    private val equations = fileToStream(2024, 7, dataFile).map { Equation.from(it) }.toList()

    private data class Equation(
        val answer: Long,
        val values: List<Long>,
    ) {
        fun applyAndCheckMatchesAnswer(operations: List<(Long, Long) -> Long>) =
            operations.any { operations.applyAndCheckMatchesAnswer(1, values[0], it) }

        private fun List<(Long, Long) -> Long>.applyAndCheckMatchesAnswer(
            index: Int,
            value: Long,
            op: (Long, Long) -> Long,
        ): Boolean =
            if (index > values.lastIndex) {
                value == answer
            } else {
                val newValue = op(value, values[index])
                this.any { this.applyAndCheckMatchesAnswer(index + 1, newValue, it) }
            }

        companion object {
            fun from(line: String): Equation {
                val parts = line.replace(":", "").split(" ").map(String::toLong)
                val lhs = parts.first()
                val rhs = parts.drop(1)
                return Equation(lhs, rhs)
            }

            val PART_1_OPERATIONS =
                listOf(
                    { a: Long, b: Long -> a + b },
                    { a: Long, b: Long -> a * b },
                )
            val PART_2_OPERATIONS =
                listOf(
                    { a: Long, b: Long -> a + b },
                    { a: Long, b: Long -> a * b },
                    { a: Long, b: Long -> a.concat(b) },
                )
        }
    }

    private fun solve(ops: List<(Long, Long) -> Long>) = equations.filter { it.applyAndCheckMatchesAnswer(ops) }.sumOf { it.answer }

    override fun part1() = solve(Equation.PART_1_OPERATIONS)

    override fun part2() = solve(Equation.PART_2_OPERATIONS)
}

fun day7() {
    val day = Day7(DataFile.Part1)
    report(2024, 7, day.part1(), day.part2())
}
