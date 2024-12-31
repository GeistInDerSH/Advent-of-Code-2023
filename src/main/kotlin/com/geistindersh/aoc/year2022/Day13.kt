package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import java.util.Stack

class Day13(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val packets =
        fileToStream(2022, 13, dataFile)
            .filter { it.isNotEmpty() }
            .map { line ->
                val stack = Stack<List<Any>>()
                stack.push(mutableListOf())

                var packet = line
                while (packet.isNotEmpty()) {
                    when (packet[0]) {
                        ',' -> packet = packet.drop(1)
                        '[' -> {
                            stack.push(mutableListOf())
                            packet = packet.drop(1)
                        }

                        ']' -> {
                            val item = stack.pop()
                            (stack.peek() as MutableList<Any>).add(item)
                            packet = packet.drop(1)
                        }

                        else -> {
                            val digits =
                                packet
                                    .takeWhile { it.isDigit() }
                                    .toList()
                                    .joinToString("")
                            (stack.peek() as MutableList<Any>).add(digits.toInt())
                            packet = packet.drop(digits.count())
                        }
                    }
                }

                stack.pop().first()
            }.toList()

    @Suppress("UNCHECKED_CAST")
    private fun areInOrder(
        left: List<Any>,
        right: List<Any>,
    ): Pair<Boolean, Boolean> {
        var lIndex = 0
        var rIndex = 0
        while (lIndex < left.size && rIndex < right.size) {
            val lVal = left[lIndex]
            val rVal = right[rIndex]
            lIndex += 1
            rIndex += 1
            when {
                lVal is Int && rVal is Int -> {
                    if (lVal < rVal) return Pair(true, true)
                    if (lVal > rVal) return Pair(false, true)
                    continue
                }

                lVal is Int && rVal is List<*> -> {
                    val result = areInOrder(listOf(lVal), rVal as List<Any>)
                    if (result.second) return result
                }

                lVal is List<*> && rVal is Int -> {
                    val result = areInOrder(lVal as List<Any>, listOf(rVal))
                    if (result.second) return result
                }

                lVal is List<*> && rVal is List<*> -> {
                    val result = areInOrder(lVal as List<Any>, rVal as List<Any>)
                    if (result.second) return result
                }
            }
        }

        return when {
            lIndex == left.size && rIndex != right.size -> Pair(true, true)
            lIndex != left.size && rIndex == right.size -> Pair(false, true)
            else -> Pair(false, false)
        }
    }

    override fun part1(): Int =
        packets
            .windowed(2, 2)
            .mapIndexed { i, (left, right) ->
                @Suppress("UNCHECKED_CAST")
                if (areInOrder(left as List<Any>, right as List<Any>).first) {
                    i + 1
                } else {
                    0
                }
            }.sum()

    override fun part2(): Int {
        val d1 = listOf(listOf(2))
        val d2 = listOf(listOf(6))
        val withDecoders =
            packets
                .toMutableList()
                .apply {
                    add(d1)
                    add(d2)
                }
        withDecoders.sortWith { p1, p2 ->
            @Suppress("UNCHECKED_CAST")
            val result = areInOrder(p1 as List<Any>, p2 as List<Any>)
            if (result.first) -1 else 1
        }
        return withDecoders
            .mapIndexed { i, d ->
                if (d1 == d || d2 == d) i + 1 else 1
            }.fold(1, Int::times)
    }
}

fun day13() {
    val day = Day13(DataFile.Part1)
    report(2022, 13, day.part1(), day.part2())
}
