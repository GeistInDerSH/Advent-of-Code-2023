package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import kotlin.collections.buildList

class Day22(
    dataFile: DataFile,
) : AoC<Long, Long> {
    private val numbers = fileToStream(2024, 22, dataFile).map(String::toLong).toList()

    private fun Long.mixAndPrune(other: Long) = (this xor other) % 16777216

    private fun Long.nextSecret(): Long {
        val a = this.mixAndPrune(this shl 6)
        val b = a.mixAndPrune(a shr 5)
        return b.mixAndPrune(b shl 11)
    }

    private fun Long.generateBuyerNumber() = (0..<2000).fold(this) { acc, _ -> acc.nextSecret() }

    private fun Long.generateBuyerCosts() =
        buildList {
            var secret = this@generateBuyerCosts
            add(secret % 10)
            repeat(2000) {
                secret = secret.nextSecret()
                add(secret % 10)
            }
        }

    override fun part1() = numbers.sumOf { it.generateBuyerNumber() }

    override fun part2() =
        buildMap<List<Long>, Long> {
            numbers.forEach { buyer ->
                buyer
                    .generateBuyerCosts()
                    .windowed(5, 1)
                    .map { window -> window.zipWithNext { a, b -> b - a } to window.last() }
                    .distinctBy { it.first }
                    .forEach { (key, value) -> this[key] = this.getOrDefault(key, 0L) + value }
            }
        }.maxOf { it.value }
}

fun day22() {
    val day = Day22(DataFile.Part1)
    report(2024, 22, day.part1(), day.part2())
}
