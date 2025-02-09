package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day6(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val data: Map<String, List<String>> =
        fileToStream(2019, 6, dataFile)
            .map { line -> line.split(')').let { it[0] to it[1] } }
            .groupingBy { it.first }
            .aggregate { _, children, pair, _ ->
                children.orEmpty() + pair.second
            }

    private fun Map<String, List<String>>.generateCostMap(): Map<Pair<String, String>, Int> {
        val com = "COM"
        val startingPoints = this.getOrDefault(com, emptyList()).map { it to 1 }
        val queue = ArrayDeque<Pair<String, Int>>().apply { addAll(startingPoints) }
        val costMap = mutableMapOf<Pair<String, String>, Int>()
        while (queue.isNotEmpty()) {
            val (name, cost) = queue.removeFirst()
            costMap[com to name] = cost
            queue.addAll(this.getOrDefault(name, emptyList()).map { it to cost + 1 })
        }
        return costMap
    }

    override fun part1() = data.generateCostMap().values.sum()

    override fun part2() = 0
}

fun day6() {
    val day = Day6(DataFile.Part1)
    report(2019, 6, day.part1(), day.part2())
}
