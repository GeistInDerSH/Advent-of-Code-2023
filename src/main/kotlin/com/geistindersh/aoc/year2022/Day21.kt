package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day21(dataFile: DataFile) {
    sealed class Fn {
        data class Const(val value: Long) : Fn()
        data class Func(val op: String, val left: String, val right: String) : Fn() {
            val fn: (Long, Long) -> Long = when (op) {
                "+" -> Long::plus
                "-" -> Long::minus
                "*" -> Long::times
                "/" -> Long::div
                else -> throw IllegalArgumentException("Illegal op: $op")
            }
            val invertFn: (Long, Long) -> Long = when (op) {
                "-" -> Long::plus
                "+" -> Long::minus
                "/" -> Long::times
                "*" -> Long::div
                else -> throw IllegalArgumentException("Illegal op: $op")
            }
        }
    }

    private val table = fileToStream(2022, 21, dataFile)
        .map {
            val items = "[ :]+".toRegex().split(it)
            val name = items[0]
            val left = items[1]
            if (left.toLongOrNull() != null) {
                name to Fn.Const(left.toLong())
            } else {
                val op = items[2]
                val right = items[3]
                name to Fn.Func(op, left, right)
            }
        }
        .toMap()

    private fun run(name: String): Long {
        return when (val value = table[name]!!) {
            is Fn.Const -> value.value
            is Fn.Func -> value.fn(run(value.left), run(value.right))
        }
    }

    fun part1() = run("root")

    fun part2() = 0
}

fun day21() {
    val day = Day21(DataFile.Part1)
    report(2022, 21, day.part1(), day.part2())
}
