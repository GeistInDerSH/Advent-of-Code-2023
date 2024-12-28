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
            val pages: Map<Int, Set<Int>> =
                run {
                    val map = mutableMapOf<Int, MutableSet<Int>>()
                    for (line in parts[0].split("\n")) {
                        val key = line.substringBefore("|").toInt()
                        val value = line.substringAfter("|").toInt()
                        if (key in map) {
                            map[key]!!.add(value)
                        } else {
                            map[key] = mutableSetOf(value)
                        }
                    }
                    map
                }
            val updates =
                parts[1]
                    .split("\n")
                    .map { line -> line.split(",").map { it.toInt() } }
                    .toSet()
            pages to updates
        }
    private val pages = data.first
    private val updates = data.second

    private fun List<Int>.hasCorrectOrder() =
        (0..<this.lastIndex)
            .all { i ->
                val l = this[i]
                val r = this[i + 1]
                r in pages.getOrDefault(l, emptySet())
            }

    private fun List<Int>.fixOrder() = this.sortedWith { l, r -> if (r in pages.getOrDefault(l, emptySet())) -1 else 0 }

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
