package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.binary.digitCount
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import kotlin.math.pow

class Day11(
    dataFile: DataFile,
) {
    private val stones = fileToString(2024, 11, dataFile).split(" ").associate { it.toLong() to 1L }
    private var memory = mutableMapOf(0L to listOf(1L))

    private fun Map<Long, Long>.update() =
        this
            .flatMap { (k, v) ->
                if (k !in memory) {
                    val digits = k.digitCount()
                    memory[k] =
                        if (digits % 2 == 0) {
                            val div = 10.0.pow(digits / 2.0).toLong()
                            listOf(k.floorDiv(div), k % div)
                        } else {
                            listOf(k * 2024)
                        }
                }
                memory[k]!!.map { it to v }
            }.groupingBy { it.first }
            .aggregate { _, accumulator: Long?, element, _ -> (accumulator ?: 0) + element.second }

    private fun Map<Long, Long>.blink(times: Int) =
        generateSequence(this) { it.update() }
            .drop(times)
            .first()
            .values
            .sum()

    fun part1() = stones.blink(25)

    fun part2() = stones.blink(75)
}

fun day11() {
    val day = Day11(DataFile.Part1)
    report(2024, 11, day.part1(), day.part2())
}
