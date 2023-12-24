package day24

import helper.files.DataFile
import helper.files.fileToStream
import helper.report

data class Vector(val px: Double, val py: Double, val pz: Double, val vx: Double, val vy: Double, val vz: Double) {
    private val a = vy
    private val b = -vx
    private val c = vy * px - vx * py

    private fun isParallel(other: Vector) = a * other.b == b * other.a

    private fun positive(x: Double, y: Double): Boolean {
        return (x - px < 0.0) == (vx < 0.0) &&
                (y - py < 0.0) == (vy < 0.0)
    }

    fun intersection(other: Vector): Pair<Double, Double>? {
        if (isParallel(other)) {
            return null
        }

        val div = a * other.b - b * other.a
        val x = (c * other.b - b * other.c) / div
        val y = (a * other.c - c * other.a) / div

        return if (positive(x, y) && other.positive(x, y)) {
            Pair(x, y)
        } else {
            null
        }
    }
}

data class Hailstones(val vectors: Set<Vector>) {

    fun part1(start: Long, end: Long): Long {
        val range = start.toDouble()..end.toDouble()
        return vectors
            .mapIndexed { index, vector ->
                vectors
                    .drop(index + 1)
                    .mapNotNull { vector.intersection(it) }
                    .count { it.first in range && it.second in range }
                    .toLong()
            }
            .sumOf { it }
    }
}

fun parseInput(dataFile: DataFile): Hailstones {
    val hail = fileToStream(24, dataFile).map { line ->
        val (pos, vel) = line.split(" @ ")
        val (px, py, pz) = pos.split(',').map { it.trim().toDouble() }
        val (vx, vy, vz) = vel.split(',').map { it.trim().toDouble() }
        Vector(px, py, pz, vx, vy, vz)
    }.toSet()
    return Hailstones(hail)
}

fun day24() {
    val input = parseInput(DataFile.Part1)
    report(
        dayNumber = 24,
        part1 = input.part1(200000000000000, 400000000000000),
        part2 = "",
    )
    // val input = parseInput(DataFile.Example)
    // report(
    //     dayNumber = 24,
    //     part1 = input.part1(7, 27),
    //     part2 = "",
    // )
}
