package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import java.math.BigInteger

class Day11(
    dataFile: DataFile,
) : AoC<BigInteger, BigInteger> {
    private val monkeys =
        fileToString(2022, 11, dataFile)
            .split("\n\n")
            .associate { line ->
                val lines = line.split("\n")
                val monkey =
                    lines[0]
                        .trim(':')
                        .drop(7)
                        .toInt()
                        .toBigInteger()
                val items =
                    lines[1]
                        .replace(",", "")
                        .split(" ")
                        .drop(4)
                        .map { it.toInt().toBigInteger() }
                        .toMutableList()
                val operation =
                    lines[2].split(" ").drop(5).let {
                        val lhs = it[0]
                        val rhs = it[2]
                        val operator =
                            when (it[1]) {
                                "*" -> BigInteger::multiply
                                "/" -> BigInteger::divide
                                "-" -> BigInteger::subtract
                                "+" -> BigInteger::add
                                else -> throw IllegalArgumentException(it[1])
                            }

                        { x: BigInteger ->
                            val a = if (lhs == "old") x else lhs.toInt().toBigInteger()
                            val b = if (rhs == "old") x else rhs.toInt().toBigInteger()
                            operator(a, b)
                        }
                    }
                val test: (BigInteger) -> BigInteger =
                    lines[3].split(" ").drop(3).last().toInt().toBigInteger().let {
                        val trueCond =
                            lines[4]
                                .split(" ")
                                .last()
                                .toInt()
                                .toBigInteger()
                        val falseCond =
                            lines[5]
                                .split(" ")
                                .last()
                                .toInt()
                                .toBigInteger()

                        val fn = { x: BigInteger -> if (x % it == BigInteger.ZERO) trueCond else falseCond }
                        fn
                    }

                monkey to MonkeyBox(items, operation, test)
            }
    private val lcm =
        fileToString(2022, 11, dataFile)
            .split("\n\n")
            .map { block ->
                val relevant = block.split("\n")[3]
                relevant
                    .split(" ")
                    .last()
                    .toInt()
                    .toBigInteger()
            }.fold(BigInteger.ONE, BigInteger::multiply)

    private data class MonkeyBox(
        val items: MutableList<BigInteger>,
        val operation: (BigInteger) -> BigInteger,
        val test: (BigInteger) -> BigInteger,
        var inspected: BigInteger = BigInteger.ZERO,
    )

    private fun applyMove(
        monkeys: Map<BigInteger, MonkeyBox>,
        worryFunc: (BigInteger) -> BigInteger,
    ): Map<BigInteger, MonkeyBox> {
        val mutableMonkeys = monkeys.toMutableMap()

        var turn = BigInteger.ZERO
        while (mutableMonkeys.count().toBigInteger() != turn) {
            val monkey = mutableMonkeys[turn]!!
            val sendTo =
                monkey
                    .items
                    .map(monkey.operation)
                    .map(worryFunc)
                    .let { items ->
                        val mm = mutableMapOf<BigInteger, MutableList<BigInteger>>()
                        items.forEach { item ->
                            val idx = monkey.test(item)
                            mm.putIfAbsent(idx, mutableListOf())
                            mm[idx]!!.add(item)
                        }
                        mm.toMap()
                    }
            val inspectedCount = monkey.inspected + monkey.items.count().toBigInteger()

            for (mky in mutableMonkeys) {
                val index = mky.key
                if (index == turn) {
                    mky.value.items.clear()
                    if (turn in sendTo) {
                        mky.value.items.addAll(sendTo[turn]!!)
                    }
                    mky.value.inspected = inspectedCount
                } else if (index in sendTo) {
                    mky.value.items.addAll(sendTo[index]!!)
                }
            }

            turn = turn.inc()
        }

        return mutableMonkeys
    }

    private fun trackMonkeys(
        rounds: Int,
        worryFunc: (BigInteger) -> BigInteger,
    ): List<BigInteger> {
        var monkeyMap = monkeys
        for (i in 0..<rounds) {
            monkeyMap = applyMove(monkeyMap, worryFunc)
        }
        return monkeyMap.values.map { it.inspected }.sorted()
    }

    override fun part1(): BigInteger {
        val worryFunc = { x: BigInteger -> x / BigInteger.valueOf(3) }
        return trackMonkeys(20, worryFunc)
            .takeLast(2)
            .fold(BigInteger.ONE) { acc, it -> acc * it }
    }

    override fun part2(): BigInteger {
        val worryFunc = { x: BigInteger -> x.mod(lcm) }
        return trackMonkeys(10000, worryFunc)
            .takeLast(2)
            .fold(BigInteger.ONE) { acc, it -> acc * it }
    }
}

fun day11() {
    val day = Day11(DataFile.Part1)
    report(2022, 11, day.part1(), day.part2())
}
