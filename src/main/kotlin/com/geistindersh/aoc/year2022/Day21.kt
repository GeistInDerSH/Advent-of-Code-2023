package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day21(
    dataFile: DataFile,
) : AoC<Long, Long> {
    sealed class Fn {
        data class Const(
            val value: Long,
        ) : Fn()

        data class Func(
            val op: String,
            val left: String,
            val right: String,
        ) : Fn() {
            val fn: (Long, Long) -> Long =
                when (op) {
                    "+" -> Long::plus
                    "-" -> Long::minus
                    "*" -> Long::times
                    "/" -> Long::div
                    else -> throw IllegalArgumentException("Illegal op: $op")
                }

            val invertFn: (Long, Long) -> Long =
                op.let {
                    if (left == "") {
                        when (op) {
                            "-" -> Long::plus
                            "+" -> Long::minus
                            "/" -> Long::times
                            "*" -> Long::div
                            else -> throw IllegalArgumentException("Illegal op: $op")
                        }
                    } else {
                        when (op) {
                            "+" -> Long::minus
                            "-" -> { a, b -> b - a }
                            "*" -> Long::div
                            "/" -> { a, b -> b / a }
                            else -> throw IllegalArgumentException("Illegal op: $op")
                        }
                    }
                }

            fun invertValue() =
                if (left == "") {
                    right.toLong()
                } else {
                    left.toLong()
                }
        }
    }

    private val table =
        fileToStream(2022, 21, dataFile)
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
            }.toMap()

    private fun run(name: String): Long =
        when (val value = table[name]!!) {
            is Fn.Const -> value.value
            is Fn.Func -> value.fn(run(value.left), run(value.right))
        }

    private fun findOps(name: String): Pair<List<Fn.Func>, Long?> =
        if (name == "humn") {
            emptyList<Fn.Func>() to null
        } else {
            findOps(table[name]!!)
        }

    private fun findOps(fn: Fn): Pair<List<Fn.Func>, Long?> {
        val empty = emptyList<Fn.Func>()
        return when (fn) {
            is Fn.Const -> empty to fn.value
            is Fn.Func -> {
                val (lhsOps, lhs) = findOps(fn.left)
                val (rhsOps, rhs) = findOps(fn.right)
                when {
                    lhs != null && rhs != null -> empty to fn.fn(lhs, rhs)
                    lhs == null && rhs != null -> (lhsOps + listOf(Fn.Func(fn.op, "", "$rhs"))) to null
                    lhs != null && rhs == null -> (rhsOps + listOf(Fn.Func(fn.op, "$lhs", ""))) to null
                    else -> throw IllegalArgumentException("$fn")
                }
            }
        }
    }

    override fun part1() = run("root")

    override fun part2(): Long {
        val root = table["root"]!! as Fn.Func
        val (lhsOps, lhs) = findOps(root.left)
        val (rhsOps, rhs) = findOps(root.right)
        val ops = lhsOps.ifEmpty { rhsOps }.reversed()
        val base = lhs ?: rhs!!

        return ops
            .fold(base) { acc, func -> func.invertFn(acc, func.invertValue()) }
    }
}

fun day21() {
    val day = Day21(DataFile.Part1)
    report(2022, 21, day.part1(), day.part2())
}
