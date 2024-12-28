package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.iterators.generatePairs
import com.geistindersh.aoc.helper.report
import java.util.PriorityQueue
import kotlin.collections.count

class Day21(
    dataFile: DataFile,
) {
    private val inputs = fileToStream(2024, 21, dataFile).toList()

    private val cache = mutableMapOf<Pair<String, Int>, Long>()

    private fun getCost(
        str: String,
        depth: Int,
        table: Map<Pair<Char, Char>, List<String>>,
    ): Long =
        cache.getOrPut(str to depth) {
            "A$str".zipWithNext().sumOf { move ->
                val paths = table.getValue(move)
                if (depth == 0) {
                    paths.minOf { it.length }.toLong()
                } else {
                    paths.minOf { getCost(it, depth - 1, DIRECTION_PAD_PATHS) }
                }
            }
        }

    fun part1() = inputs.sumOf { getCost(it, 2, NUMPAD_PATHS) * it.dropLast(1).toLong() }

    fun part2() = inputs.sumOf { getCost(it, 25, NUMPAD_PATHS) * it.dropLast(1).toLong() }

    companion object {
        private val NUMPAD =
            mapOf(
                Point2D(0, 0) to '7',
                Point2D(0, 1) to '8',
                Point2D(0, 2) to '9',
                Point2D(1, 0) to '4',
                Point2D(1, 1) to '5',
                Point2D(1, 2) to '6',
                Point2D(2, 0) to '1',
                Point2D(2, 1) to '2',
                Point2D(2, 2) to '3',
                Point2D(3, 1) to '0',
                Point2D(3, 2) to 'A',
            )
        private val DIRECTION_PAD =
            mapOf(
                Point2D(0, 1) to '^',
                Point2D(0, 2) to 'A',
                Point2D(1, 0) to '<',
                Point2D(1, 1) to 'v',
                Point2D(1, 2) to '>',
            )

        private val NUMPAD_PATHS = NUMPAD.generatePaths()
        private val DIRECTION_PAD_PATHS = DIRECTION_PAD.generatePaths()

        private fun Map<Point2D, Char>.generatePaths() =
            this.keys
                .generatePairs()
                .associate { (start, end) ->
                    (this.getValue(start) to this.getValue(end)) to this.shortestPath(start, end)
                }

        private fun Map<Point2D, Char>.shortestPath(
            start: Point2D,
            end: Point2D,
        ): List<String> {
            val queue =
                PriorityQueue<Pair<Point2D, List<Direction>>>(compareBy { it.second.size })
                    .apply { add(start to emptyList<Direction>()) }
            val seen = mutableMapOf<Point2D, Int>()
            val paths = mutableListOf<String>()
            var cost: Int? = null
            while (queue.isNotEmpty()) {
                val (point, path) = queue.poll()
                val count = path.count()
                if (cost != null && count > cost) return paths
                if (point == end) {
                    cost = count
                    paths.add(path.map { it.toChar() }.joinToString("") + "A")
                    continue
                }
                seen[point] = count
                for (dir in Direction.entries) {
                    val next = point + dir
                    if (next !in this) continue
                    queue.add(next to (path + dir))
                }
            }
            return paths
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
