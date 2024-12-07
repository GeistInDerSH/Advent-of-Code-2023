package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day7(
    dataFile: DataFile,
) {
    private val data =
        fileToStream(2024, 7, dataFile)
            .map { Equation.from(it) }
            .toList()

    private data class Equation(
        val answer: Long,
        val values: List<Long>,
    ) {
        fun evalWith(operations: List<(Long, Long) -> Long>) =
            values.drop(1).foldIndexed(values[0]) { idx, acc, v -> operations[idx](acc, v) }

        fun anyMatch() = anyMatch(1, values[0], Long::plus) || anyMatch(1, values[0], Long::times)

        private fun anyMatch(
            index: Int,
            value: Long,
            op: (Long, Long) -> Long,
        ): Boolean {
            if (index > values.lastIndex) return value == answer
            val newValue = op(value, values[index])
            return anyMatch(index + 1, newValue, Long::plus) || anyMatch(index + 1, newValue, Long::times)
        }

        companion object {
            fun from(line: String): Equation {
                val parts = line.replace(":", "").split(" ").map(String::toLong)
                val lhs = parts.first()
                val rhs = parts.drop(1)
                return Equation(lhs, rhs)
            }
        }
    }

    fun part1() = data.filter { it.anyMatch() }.sumOf { it.answer }

    fun part2() = 0
}

fun day7() {
    val day = Day7(DataFile.Part1)
    report(2024, 7, day.part1(), day.part2())
}
