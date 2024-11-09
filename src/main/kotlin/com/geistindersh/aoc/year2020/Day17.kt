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
        .let { ConwaysGameOfLife3D(it) }

    private data class ConwaysGameOfLife3D(val enabledCubes: Set<Point3D>) {
        fun next(): ConwaysGameOfLife3D {
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
            return ConwaysGameOfLife3D(points)
        }
    }

    private data class ConwaysGameOfLife4D(val enabledCubes: Set<List<Int>>) {
        private fun List<Int>.neighbors(): List<List<Int>> {
            val neighbors = mutableListOf<List<Int>>()
            for (x in -1..1) {
                for (y in -1..1) {
                    for (z in -1..1) {
                        for (w in -1..1) {
                            if (x == 0 && y == 0 && z == 0 && w == 0) continue
                            neighbors.add(listOf(this[0] + x, this[1] + y, this[2] + z, this[3] + w))
                        }
                    }
                }
            }
            return neighbors
        }

        fun next(): ConwaysGameOfLife4D {
            val points = mutableSetOf<List<Int>>()
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
            return ConwaysGameOfLife4D(points)
        }

        companion object {
            fun from(gol: ConwaysGameOfLife3D): ConwaysGameOfLife4D {
                val cubes = gol.enabledCubes.map { listOf(it.x, it.y, it.z, 0) }.toSet()
                return ConwaysGameOfLife4D(cubes)
            }
        }
    }

    fun part1() = generateSequence(gameOfLife) { it.next() }
        .drop(1) // Skip initial state
        .take(6)
        .last()
        .enabledCubes
        .count()

    fun part2() = generateSequence(ConwaysGameOfLife4D.from(gameOfLife)) { it.next() }
        .drop(1)
        .take(6)
        .last()
        .enabledCubes
        .count()
}

fun day17() {
    val day = Day17(DataFile.Part1)
    report(2020, 17, day.part1(), day.part2())
}
