package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day5(
    dataFile: DataFile,
) {
    private val data =
        run {
            val parts = fileToString(2024, 5, dataFile).split("\n\n")
            val pages =
                parts[0]
                    .split("\n")
                    .map { line ->
                        val (x, y) = line.split("|").map { it.toInt() }
                        Pages(x, y)
                    }.toSet()
            val updates =
                parts[1]
                    .split("\n")
                    .map { line -> line.split(",").map { it.toInt() } }
            pages to updates
        }
    private val pages = data.first
    private val updates = data.second

    private data class Pages(
        val left: Int,
        val right: Int,
    )

    private fun List<Int>.hasCorrectOrder() =
        this
            .windowed(2)
            .all { (a, b) -> Pages(a, b) in pages }

    private fun List<Int>.fixOrder() =
        this.sortedWith { a, b ->
            val options = pages.filter { it.right == a }.map { it.left }.toSet()
            if (b in options) -1 else 0
        }

    fun part1() =
        updates
            .filter { it.hasCorrectOrder() }
            .map { it[it.lastIndex / 2] }
            .reduce(Int::plus)

    fun part2() =
        updates
            .filterNot { it.hasCorrectOrder() }
            .map { it.fixOrder() }
            .map { it[it.lastIndex / 2] }
            .reduce(Int::plus)
}

fun day5() {
    val day = Day5(DataFile.Part1)
    report(2024, 5, day.part1(), day.part2())
}
