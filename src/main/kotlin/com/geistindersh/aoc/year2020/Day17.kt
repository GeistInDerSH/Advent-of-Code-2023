package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.enums.Point3D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day17(dataFile: DataFile) {
    private val gameOfLife = fileToStream(2020, 17, dataFile)
        .flatMapIndexed { x, line ->
            line.mapIndexedNotNull { y, c -> if (c == '#') Point3D(x, y, 0) else null }
        }
        .toSet()
        .let { ConwaysGameOfLife(it) }

    private data class ConwaysGameOfLife(val enabledCubes: Set<Point3D>) {
        fun next(): ConwaysGameOfLife {
            val points = mutableSetOf<Point3D>()
            val toCheck = enabledCubes.flatMap { it.neighbors() }.toMutableSet().apply { addAll(enabledCubes) }
            for (point in toCheck) {
                val activeNeighbors = point
                    .neighbors()
                    .count { it in enabledCubes }
                when {
                    activeNeighbors == 3 -> points.add(point)
                    point in enabledCubes && activeNeighbors == 2 -> points.add(point)
                }
            }
            return ConwaysGameOfLife(points)
        }
    }

    fun part1() = generateSequence(gameOfLife) { it.next() }
        .drop(1) // Skip initial state
        .take(6)
        .last()
        .enabledCubes
        .count()

    fun part2() = 0
}

fun day17() {
    val day = Day17(DataFile.Part1)
    report(2020, 17, day.part1(), day.part2())
}
