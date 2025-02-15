package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.enums.Point3D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.math.lcm
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.extractIntegers
import kotlin.math.absoluteValue

class Day12(
    dataFile: DataFile,
    private val steps: Int = 1000,
) : AoC<Int, Long> {
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

        private fun diff(
            current: Point3D,
            other: Point3D,
        ): Point3D {
            val x = diff(current.x, other.x)
            val y = diff(current.y, other.y)
            val z = diff(current.z, other.z)
            return Point3D(x, y, z)
        }

        fun next(moons: List<Moon>): Moon =
            moons
                .fold(this.velocity) { acc, point ->
                    val updated = diff(this.position, point.position)
                    acc + updated
                }.let { this.copy(position + it, it) }
    }

    private fun List<Moon>.getPeriod(fn: (Point3D) -> Int): Long {
        val expectedVals = moons.map { fn(it.position) }

        var step = 0L
        var localMoons = this
        while (true) {
            localMoons = localMoons.map { it.next(localMoons) }
            if (localMoons.zip(expectedVals).all { (l, r) -> fn(l.position) == r }) {
                return step + 2
            }
            step += 1
        }
    }

    override fun part1() =
        generateSequence(moons) { bodies -> bodies.map { it.next(bodies) } }
            .drop(steps)
            .take(1)
            .flatten()
            .sumOf { it.energy() }

    override fun part2(): Long {
        val p1 = moons.getPeriod { it.x }
        val p2 = moons.getPeriod { it.y }
        val p3 = moons.getPeriod { it.z }
        val ans = lcm(p1, p2)
        return lcm(ans, p3)
    }
}

fun day12() {
    val day = Day12(DataFile.Part1)
    report(2019, 12, day.part1(), day.part2())
}
