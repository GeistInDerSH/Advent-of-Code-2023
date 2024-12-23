package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day22(
    dataFile: DataFile,
) {
    private val numbers = fileToStream(2024, 22, dataFile).map(String::toLong).toList()

    private fun Long.mixAndPrune(other: Long) = (this xor other) % 16777216

    private fun Long.nextSecret(): Long {
        val a = this.mixAndPrune(this shl 6)
        val b = a.mixAndPrune(a shr 5)
        return b.mixAndPrune(b shl 11)
    }

    private fun Long.generateBuyerNumber() = (0..<2000).fold(this) { acc, _ -> acc.nextSecret() }

    fun part1() = numbers.sumOf { it.generateBuyerNumber() }

    fun part2() = 0
}

fun day22() {
    val day = Day22(DataFile.Part1)
    report(2024, 22, day.part1(), day.part2())
}
