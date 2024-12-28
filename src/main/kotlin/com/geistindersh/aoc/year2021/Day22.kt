package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.ranges.hasOverlap
import com.geistindersh.aoc.helper.ranges.intersection
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.extractIntegers

class Day22(
    dataFile: DataFile,
) {
    private val cubes =
        fileToStream(2021, 22, dataFile)
            .map { line ->
                val isOn = line.substringBefore(' ') == "on"
                val (x, y, z) =
                    line
                        .extractIntegers()
                        .windowed(2, 2) { (start, end) -> start..end }
                        .toList()
                RebootCube(isOn, x, y, z)
            }.toList()

    private data class RebootCube(
        val isOn: Boolean,
        val x: IntRange,
        val y: IntRange,
        val z: IntRange,
    ) {
        fun volume() = (if (isOn) 1 else -1) * x.size() * y.size() * z.size()

        fun hasOverlap(other: RebootCube) = x.hasOverlap(other.x) && y.hasOverlap(other.y) && z.hasOverlap(other.z)

        fun intersection(other: RebootCube) =
            RebootCube(
                !isOn,
                x.intersection(other.x),
                y.intersection(other.y),
                z.intersection(other.z),
            )

        private fun IntRange.size() = (last - first + 1).toLong()
    }

    private fun List<RebootCube>.determineOverlap(): Long {
        val cubes = mutableListOf<RebootCube>()
        for (cube in this) {
            val toAdd = cubes.filter { it.hasOverlap(cube) }.map { it.intersection(cube) }
            cubes.addAll(toAdd)
            if (cube.isOn) cubes.add(cube)
        }
        return cubes.sumOf { it.volume() }
    }

    fun part1() =
        RebootCube(true, -50..50, -50..50, -50..50).let { cube ->
            cubes.filter { cube.hasOverlap(it) }.determineOverlap()
        }

    fun part2() = cubes.determineOverlap()
}

fun day22() {
    val day = Day22(DataFile.Part1)
    report(2021, 22, day.part1(), day.part2())
}
