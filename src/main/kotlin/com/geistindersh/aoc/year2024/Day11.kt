package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day11(
    dataFile: DataFile,
) {
    private val stones = fileToString(2024, 11, dataFile).split(" ").associateWith { 1L }
    private var memory = mutableMapOf("0" to listOf("1"))

    private fun Map<String, Long>.update() =
        this
            .flatMap { (k, v) ->
                when {
                    k in memory -> {}
                    k.length % 2 == 0 -> {
                        val mid = k.length / 2
                        memory[k] = listOf(k.substring(0, mid).toLong().toString(), k.substring(mid).toLong().toString())
                    }
                    else -> memory[k] = listOf((k.toLong() * 2024).toString())
                }
                memory[k]!!.map { it to v }
            }.groupingBy { it.first }
            .aggregate { _, accumulator: Long?, element, _ -> (accumulator ?: 0) + element.second }

    private fun Map<String, Long>.blink(times: Int) =
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
