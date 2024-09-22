package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import kotlin.math.max

class Day16(dataFile: DataFile) {
    private val lines = fileToStream(2022, 16, dataFile)
        .map { "[\\s=;,]+".toRegex().split(it) }
        .toList()
    private val graph = lines.associate { it[1] to it.drop(10).toSet() }
    private val pipeFlowMap = lines
        .filter { it[5].toInt() != 0 }
        .associate { it[1] to it[5].toInt() }
    private val pipeOffsetMap = pipeFlowMap
        .keys
        .mapIndexed { index, entry -> entry to (1 shl index).toLong() }
        .toMap()
    private val distances = graph
        .keys
        .associateWith { x ->
            graph
                .keys
                .associateWith { if (graph[x]?.contains(it) == true) 1 else 9999999999 }
                .toMutableMap()
        }
        .toMutableMap()

    init {
        for (k in distances.keys) {
            for (i in distances.keys) {
                for (j in distances.keys) {
                    val current = distances[i]!![j]!!
                    val other = distances[i]!![k]!! + distances[k]!![j]!!
                    distances[i]!![j] = if (current < other) current else other
                }
            }
        }
    }

    private fun visit(
        node: String,
        budget: Long,
        state: Long,
        flow: Long,
        visited: Map<Long, Long>
    ): Map<Long, Long> {
        var localVisited = visited.toMutableMap()
        localVisited[state] = max(visited.getOrDefault(state, 0), flow)

        for (pipe in pipeFlowMap.keys) {
            val newBudget = budget - distances[node]!![pipe]!! - 1
            if (pipeOffsetMap[pipe]!! and state != 0L || newBudget <= 0) {
                continue
            }

            val newState = state or pipeOffsetMap[pipe]!!
            val newFlow = flow + newBudget * pipeFlowMap[pipe]!!
            localVisited = visit(pipe, newBudget, newState, newFlow, localVisited).toMutableMap()
        }

        return localVisited
    }

    fun part1() = visit("AA", 30, 0, 0, mutableMapOf()).values.max()
    fun part2(): Long {
        val visited = visit("AA", 26, 0, 0, mutableMapOf())
        return visited.maxOf { (k1, v1) ->
            visited
                .map { (k2, v2) -> if (k1 and k2 == 0L) v1 + v2 else 0 }
                .max()
        }
    }
}

fun day16() {
    val day = Day16(DataFile.Part1)
    report(2022, 16, day.part1(), day.part2())
}