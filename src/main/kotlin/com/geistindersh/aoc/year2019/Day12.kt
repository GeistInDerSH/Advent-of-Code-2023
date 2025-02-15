package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.enums.Point3D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.extractIntegers
import kotlin.math.absoluteValue

class Day12(
    dataFile: DataFile,
    private val steps: Int = 1000,
) : AoC<Int, Int> {
    private val moons =
        fileToStream(2019, 12, dataFile)
            .map { it.extractIntegers() }
            .map { (x, y, z) -> Point3D(x, y, z) }
            .map { Moon(it) }
            .toList()

    private data class Moon(
        val position: Point3D,
        val velocity: Point3D = Point3D(0, 0, 0),
    ) {
        private fun Point3D.toList() = listOf(this.x, this.y, this.z)

        fun energy() = position.toList().sumOf { it.absoluteValue } * velocity.toList().sumOf { it.absoluteValue }

        private fun diff(
            current: Int,
            other: Int,
        ) = when {
            current < other -> 1
            other < current -> -1
            else -> 0
        }

        fun next(moons: List<Moon>): Moon {
            val velocity =
                moons
                    .filter { it != this }
                    .fold(this.velocity) { acc, point ->
                        val x = diff(this.position.x, point.position.x)
                        val y = diff(this.position.y, point.position.y)
                        val z = diff(this.position.z, point.position.z)
                        Point3D(x + acc.x, y + acc.y, z + acc.z)
                    }
            return this.copy(position + velocity, velocity = velocity)
        }
    }

    override fun part1() =
        generateSequence(moons) { bodies -> bodies.map { it.next(bodies) } }
            .drop(steps)
            .take(1)
            .flatten()
            .sumOf { it.energy() }

    override fun part2() = 0
}

fun day12() {
    val day = Day12(DataFile.Part1)
    report(2019, 12, day.part1(), day.part2())
}
