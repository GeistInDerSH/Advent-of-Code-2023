package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day25(dataFile: DataFile) {
    private val decodeSum = fileToStream(2022, 25, dataFile).map(::decode).sum()

    private fun decode(line: String) =
        line
            .map { decodeLookupTable[it]!! }
            .reduce { acc, i -> acc * 5 + i }

    private fun encode(n: Long) =
        generateSequence(n) { (it + 2) / 5 } // 0 to 5
            .takeWhile { it != 0L } // Take until we get a min value
            .joinToString("") {
                val idx = ((it + 2) % 5) - 2
                encodeLookupTable[idx]!!
            } // -2 to 2
            .reversed()

    fun part1() = encode(decodeSum)

    fun part2() = "Push the button!"

    companion object {
        val decodeLookupTable =
            mapOf(
                '=' to -2,
                '-' to -1,
                '0' to 0,
                '1' to 1,
                '2' to 2L,
            )
        val encodeLookupTable = decodeLookupTable.map { (k, v) -> v to k.toString() }.toMap()
    }
}

fun day25() {
    val day = Day25(DataFile.Part1)
    report(2022, 25, day.part1(), day.part2())
}
