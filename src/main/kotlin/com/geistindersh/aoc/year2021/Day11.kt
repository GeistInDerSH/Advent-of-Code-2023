package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2D

class Day11(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val octopuses =
        fileToString(2021, 11, dataFile)
            .toGrid2D { it.digitToInt() }
            .let {
                Octopuses(0, it)
            }

    private data class Octopuses(
        val round: Int,
        val octopuses: Map<Point2D, Int>,
        val totalFlashes: Int = 0,
        val newFlashes: Int = 0,
    ) {
        fun next(): Octopuses {
            val octopuses = octopuses.entries.associate { (point, value) -> point to value + 1 }.toMutableMap()
            val flashed = mutableSetOf<Point2D>()

            val queue =
                ArrayDeque<Point2D>()
                    .apply { addAll(octopuses.entries.filter { it.value > 9 }.map { it.key }) }
            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                if (current in flashed) continue
                flashed.add(current)

                current.neighborsAll().filter { it in octopuses }.forEach { octopuses[it] = octopuses[it]!! + 1 }
                val newFlash =
                    octopuses.entries
                        .filter { it.value > 9 }
                        .filter { it.key !in flashed }
                        .map { it.key }
                queue.addAll(newFlash)
            }

            flashed.forEach { octopuses[it] = 0 }
            return Octopuses(round + 1, octopuses, totalFlashes + flashed.count(), flashed.count())
        }
    }

    override fun part1() =
        generateSequence(octopuses) { it.next() }
            .drop(100)
            .first()
            .totalFlashes

    override fun part2() =
        generateSequence(octopuses) { it.next() }
            .dropWhile { it.newFlashes != it.octopuses.size }
            .first()
            .round
}

fun day11() {
    val day = Day11(DataFile.Part1)
    report(2021, 11, day.part1(), day.part2())
}
