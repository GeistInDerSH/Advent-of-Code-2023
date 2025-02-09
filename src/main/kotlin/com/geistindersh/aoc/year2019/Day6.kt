package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.algorithms.Graph
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day6(
    val neighbors: Map<String, List<String>>,
    val graph: Graph<String>,
) : AoC<Int, Int> {
    constructor(dataFile: DataFile) : this(fileToStream(2019, 6, dataFile).map { it.split(")") }.map { (a, b) -> a to b }.toList())
    constructor(lines: List<Pair<String, String>>) : this(
        lines.groupingBy { it.first }.aggregate { _, children, pair, _ -> children.orEmpty() + pair.second },
        lines.flatMap { (a, b) -> listOf(a to b, b to a) }.map { it to 1 }.let { Graph(it) },
    )

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

    private fun Map<String, List<String>>.findParentOf(child: String) = this.filterValues { child in it }.keys.first()

    override fun part1() = neighbors.generateCostMap().values.sum()

    override fun part2() = listOf("YOU", "SAN").map { neighbors.findParentOf(it) }.let { (start, end) -> graph.dfs(start, end) }
}

fun day6() {
    val day = Day6(DataFile.Part1)
    report(2019, 6, day.part1(), day.part2())
}
