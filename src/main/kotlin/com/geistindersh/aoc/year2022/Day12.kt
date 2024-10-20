package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import java.util.*

class Day12(dataFile: DataFile) {
    private val grid = fileToStream(2022, 12, dataFile)
        .map { line ->
            line.map {
                when (it) {
                    'S' -> -1
                    'E' -> 26
                    else -> it.code - 97
                }
            }
        }
        .toList()
    private val start = grid
        .mapIndexed { index, ints ->
            val loc = ints.indexOf(-1)
            if (loc != -1) {
                Pair(index, loc)
            } else {
                null
            }
        }
        .firstNotNullOf { it }
    private val end = grid
        .mapIndexed { index, ints ->
            val loc = ints.indexOf(26)
            if (loc != -1) {
                Pair(index, loc)
            } else {
                null
            }
        }
        .firstNotNullOf { it }

    private fun getNeighbors(cord: Pair<Int, Int>): Set<Pair<Int, Int>> {
        val neighbors = listOf(
            Pair(cord.first - 1, cord.second),
            Pair(cord.first + 1, cord.second),
            Pair(cord.first, cord.second - 1),
            Pair(cord.first, cord.second + 1),
        )
        return neighbors
            .filter {
                val current = grid[cord.first][cord.second]
                val neighbor = grid
                    .getOrNull(it.first)
                    ?.getOrNull(it.second)
                    ?: return@filter false

                neighbor - current <= 1
            }
            .toSet()
    }

    private fun bfs(start: List<Pair<Int, Int>>, end: Pair<Int, Int>): Int {
        val visited = mutableSetOf<Pair<Int, Int>>()
        val queue = PriorityQueue<Pair<Pair<Int, Int>, Int>>(compareBy { it.second })
        queue.addAll(start.map { Pair(it, 0) })

        while (queue.isNotEmpty()) {
            val current = queue.poll() ?: break
            val (cords, cost) = current
            if (cords == end) return cost

            val touching = getNeighbors(cords)
                .filter { it !in visited }
                .map { Pair(it, cost + 1) }
                .toSet()
            // Remove all first, so we don't have duplicates
            // (it's also WAY faster for larger grids)
            queue.removeAll(touching)
            queue.addAll(touching)
            visited.add(cords)
        }
        return 0
    }

    private fun bfs(start: Pair<Int, Int>, end: Pair<Int, Int>) = bfs(listOf(start), end)

    fun part1(): Int = bfs(start, end)

    fun part2(): Int {
        val zeroPoints = grid
            .indices
            .flatMap { x ->
                grid[0]
                    .indices
                    .mapNotNull { y ->
                        if (grid[x][y] == 0) Pair(x, y)
                        else null
                    }
            }
        return bfs(listOf(start) + zeroPoints, end)
    }
}

fun day12() {
    val day = Day12(DataFile.Part1)
    report(2022, 12, day.part1(), day.part2())
}