package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.iterators.cycle
import com.geistindersh.aoc.helper.report

class Day23(dataFile: DataFile) {
    private val elves = fileToStream(2022, 23, dataFile)
        .flatMapIndexed { row, line ->
            line.mapIndexedNotNull { col, value ->
                if (value == '#') Point2D(row, col)
                else null
            }
        }
        .toSet()

    private fun getAdjacency(direction: Direction) = when (direction) {
        Direction.North, Direction.South -> listOf(
            direction.pair(),
            direction + Direction.East,
            direction + Direction.West,
        )

        Direction.East, Direction.West -> listOf(
            direction.pair(),
            direction + Direction.North,
            direction + Direction.South,
        )
    }

    private fun getAllNeighbours() = listOf(
        Direction.North.pair(),
        Direction.North + Direction.East,
        Direction.North + Direction.West,
        Direction.South.pair(),
        Direction.South + Direction.East,
        Direction.South + Direction.West,
        Direction.East.pair(),
        Direction.West.pair(),
    )

    private fun rounds() = sequence {
        val searchOrder = listOf(Direction.North, Direction.South, Direction.West, Direction.East).cycle()
        var elves = this@Day23.elves
        yield(elves)

        var round = 0
        while (true) {
            val proposedMoves = mutableMapOf<Point2D, Point2D>()
            val order = searchOrder.drop(round).take(4).toList()

            for (elf in elves) {
                if (getAllNeighbours().map { elf + it }.all { it !in elves }) {
                    proposedMoves[elf] = elf
                    continue
                }

                proposedMoves[elf] = order
                    .firstOrNull { dir ->
                        getAdjacency(dir).map { elf + it }.all { it !in elves }
                    }
                    ?.let { elf + it }
                    ?: elf
            }

            for ((elf, move) in proposedMoves) {
                val movedToSame = proposedMoves.filter { (k, v) -> k != elf && v == move }.keys
                if (movedToSame.isEmpty()) continue

                proposedMoves[elf] = elf
                movedToSame.forEach { proposedMoves[it] = it }
            }

            elves = proposedMoves.values.toSet()
            yield(elves)
            round += 1
        }
    }

    fun part1(): Int {
        val elves = rounds().elementAt(10)
        val height = elves.maxOf { it.row } - elves.minOf { it.row } + 1
        val width = elves.maxOf { it.col } - elves.minOf { it.col } + 1
        return width * height - elves.size
    }

    // Efficient? No! Functional? Yes!
    fun part2() = rounds()
        .zipWithNext()
        .takeWhile { (prev, curr) -> prev != curr }
        .count() + 1
}

fun day23() {
    val day = Day23(DataFile.Part1)
    report(2022, 23, day.part1(), day.part2())
}
