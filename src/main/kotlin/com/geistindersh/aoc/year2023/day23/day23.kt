package com.geistindersh.aoc.year2023.day23

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.max

data class Trail(val row: Int, val col: Int, val symbol: Char) {
    val pair = Pair(row, col)
    val allNeighbors = Direction.entries.map { it + pair }
    val neighbors = when (symbol) {
        '>' -> listOf(Direction.East + pair)
        '^' -> listOf(Direction.North + pair)
        'v' -> listOf(Direction.South + pair)
        '<' -> listOf(Direction.West + pair)
        else -> allNeighbors
    }
}

data class Hike(val trail: Set<Trail>) {
    private val start = trail.minBy { it.row }
    private val end = trail.maxBy { it.row }
    private val coordinateToTrail = trail.associateBy { it.pair }

    // Trim down the path, by only getting nodes with two or more paths, then create an adjacency list
    // with paths, and the distance of the path. This helps to avoid the Stack overflowing in the JVM
    private val trailDistance = run {
        val junctions = trail.filter { t ->
            t.neighbors
                .mapNotNull { coordinateToTrail[it] }
                .size > 2
        } + start + end

        val trailDistance = trail.associateWith { mutableListOf<Pair<Int, Trail>>() }

        junctions.forEach { junction ->
            val queue = ArrayDeque<Trail>()
            queue.add(junction)
            val seen = mutableSetOf(junction)
            var distance = 0

            while (queue.isNotEmpty()) {
                val newQueue = mutableListOf<Trail>()
                distance += 1
                for (item in queue) {
                    item.allNeighbors
                        .mapNotNull { coordinateToTrail[it] }
                        .filter { it !in seen }
                        .forEach { value ->
                            seen.add(value)
                            if (value in junctions) {
                                trailDistance[junction]!!.add(Pair(distance, value))
                            } else {
                                newQueue.add(value)
                            }
                        }
                }
                queue.clear()
                queue.addAll(newQueue)
            }
        }

        trailDistance
    }

    /**
     * Walk the path from the start to the end, without repeating any steps, & be mindful of the
     * slope characters along the path.
     *
     * @param current The current step in the path
     * @param seen The nodes we have seen before [current]
     * @param longest The longest path we have seen
     * @return The longest path, with no repeated steps
     */
    private fun depthFirstSearch(current: Trail, seen: Stack<Trail>, longest: Int): Int {
        return if (current == end) {
            max(longest, seen.size)
        } else {
            val localMax = current.neighbors
                .mapNotNull { coordinateToTrail[it] }
                .filterNot { it in seen }
                .maxOfOrNull {
                    seen.push(it)
                    val m = depthFirstSearch(it, seen, longest)
                    seen.pop()
                    m
                } ?: longest

            max(localMax, longest)
        }
    }

    /**
     * Walk the path from the start to the end, without repeating any steps, & ignore the
     * slope characters along the path.
     *
     * @param current The current step in the path
     * @param seen The nodes we have seen before [current]
     * @param longest The longest path we have seen
     * @param distance The total distance traveled, as the [seen] size is no longer 1:1
     * @return The [longest] distance traveled
     */
    private fun depthFirstSearchAllNeighbors(current: Trail, seen: Stack<Trail>, longest: Int, distance: Int): Int {
        return if (current == end) {
            max(longest, distance)
        } else {
            val found = trailDistance[current]!!.filterNot { (_, t) -> t in seen }
            val localMax = if (found.isNotEmpty()) {
                found.maxOf { (dist, t) ->
                    seen.push(t)
                    val m = depthFirstSearchAllNeighbors(t, seen, longest, distance + dist)
                    seen.pop()
                    m
                }
            } else {
                0
            }

            max(localMax, longest)
        }
    }

    fun part1() = depthFirstSearch(start, Stack(), 0)
    fun part2() = depthFirstSearchAllNeighbors(start, Stack(), 0, 0)
}

fun parseInput(dataFile: DataFile): Hike {
    val trails = fileToStream(23, dataFile)
        .flatMapIndexed { row, line ->
            line.mapIndexed { col, c -> Trail(row, col, c) }
                .filter { it.symbol != '#' }
        }
        .toSet()
    return Hike(trails)
}

fun day23(skip: Boolean = true) {
    if (skip) {
        return
    }
    val input = parseInput(DataFile.Part1)
    report(
        dayNumber = 23,
        part1 = input.part1(),
        part2 = input.part2(),
    )
}
