package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import java.util.PriorityQueue
import kotlin.collections.count

class Day21(
    dataFile: DataFile,
) {
    private val inputs = fileToStream(2024, 21, dataFile).toList()

    private fun String.numpadEntrySequence(): Set<String> {
        var sequences = mutableListOf<String>()
        val positions = listOf(NUMPAD_GRID['A']) + this.map { NUMPAD_GRID[it]!! }
        for (i in 0..<positions.lastIndex) {
            val start = positions[i]
            val end = positions[i + 1]
            if (sequences.isEmpty()) {
                sequences.addAll(NUMPAD_MAP_PATHS[start to end]!!)
            } else {
                val toAdd = mutableListOf<String>()
                for (entry in sequences) {
                    for (path in NUMPAD_MAP_PATHS[start to end]!!) {
                        toAdd.add(entry + path)
                    }
                }
                sequences = toAdd
            }
        }
        return sequences.toSet()
    }

    private fun String.directionalPadEntrySequence(): Set<String> {
        val robotStart = DIRECTION_PAD_GRID['A']
        val numpadStart = DIRECTION_PAD_GRID[this[0]]
        var sequences = DIRECTION_PAD_MAP_PATHS[robotStart to numpadStart]!!.toSet()
        val positions = this.map { DIRECTION_PAD_GRID[it]!! }
        for (i in 0..<positions.lastIndex) {
            val start = positions[i]
            val end = positions[i + 1]
            val route = start to end
            val toAdd = mutableSetOf<String>()
            for (entry in sequences) {
                for (path in DIRECTION_PAD_MAP_PATHS[route]!!) {
                    toAdd.add(entry + path)
                }
            }
            sequences = toAdd
        }
        val shortestLength = sequences.minOf { it.length }
        println(sequences.count())
        return sequences.filter { it.length == shortestLength }.toSet()
    }

    private fun String.getShortestSequence() =
        this
            .numpadEntrySequence()
            .flatMapTo(mutableSetOf()) { it.directionalPadEntrySequence() }
            .flatMapTo(mutableSetOf()) { it.directionalPadEntrySequence() }
            .minBy { it.length }
            .count()
            .toLong()

    private fun String.toNumeric() = this.dropLast(1).toLong()

    fun part1() =
        inputs
            .map { it.toNumeric() to it.getShortestSequence() }
            .sumOf { (num, score) ->
                println("$num * $score")
                num * score
            }

    fun part2() = 0

    companion object {
        private val NUMPAD_GRID =
            mapOf(
                '7' to Point2D(0, 0),
                '8' to Point2D(0, 1),
                '9' to Point2D(0, 2),
                '4' to Point2D(1, 0),
                '5' to Point2D(1, 1),
                '6' to Point2D(1, 2),
                '1' to Point2D(2, 0),
                '2' to Point2D(2, 1),
                '3' to Point2D(2, 2),
                '0' to Point2D(3, 1),
                'A' to Point2D(3, 2),
            )
        private val DIRECTION_PAD_GRID =
            mapOf(
                '^' to Point2D(0, 1),
                'A' to Point2D(0, 2),
                '<' to Point2D(1, 0),
                'v' to Point2D(1, 1),
                '>' to Point2D(1, 2),
            )

        private val NUMPAD_MAP_PATHS = construct(NUMPAD_GRID.values.toSet())
        private val DIRECTION_PAD_MAP_PATHS = construct(DIRECTION_PAD_GRID.values.toSet())

        private fun shortest(
            start: Point2D,
            end: Point2D,
            keyMap: Set<Point2D>,
        ): Set<List<Direction>> {
            val queue =
                PriorityQueue<Triple<Point2D, List<Direction>, Set<Point2D>>>(compareBy { it.second.count() })
                    .apply { add(Triple(start, emptyList(), setOf(start))) }
            val routes = mutableSetOf<List<Direction>>()
            while (queue.isNotEmpty()) {
                val head = queue.poll()
                if (head.first == end) {
                    routes.add(head.second)
                    continue
                }
                if (routes.isNotEmpty() && routes.any { it.size < head.second.size }) continue
                for (dir in Direction.entries) {
                    val next = head.first + dir
                    if (next !in keyMap) continue
                    if (next in head.third) continue
                    queue.add(Triple(next, head.second + dir, head.third + next))
                }
            }
            return routes
        }

        private fun construct(keyMap: Set<Point2D>): Map<Pair<Point2D, Point2D>, List<String>> {
            val values = mutableMapOf<Pair<Point2D, Point2D>, List<String>>()
            for (start in keyMap) {
                values[start to start] = listOf("A")
                for (end in keyMap) {
                    val pair = start to end
                    if (pair in values) continue
                    val path =
                        shortest(start, end, keyMap).map { it.joinToString("") { c -> c.toChar().toString() } + "A" }
                    values[pair] = path
                }
            }
            return values
        }

        private fun Direction.toChar() =
            when (this) {
                Direction.North -> '^'
                Direction.South -> 'v'
                Direction.East -> '>'
                Direction.West -> '<'
            }
    }
}

fun day21() {
    val day = Day21(DataFile.Part1)
    report(2024, 21, day.part1(), day.part2())
}
