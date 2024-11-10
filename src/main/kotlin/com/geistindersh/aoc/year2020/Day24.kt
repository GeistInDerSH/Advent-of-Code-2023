package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.enums.Point3D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day24(dataFile: DataFile) {
    private val lines = fileToStream(2020, 24, dataFile).map { Line(it) }.toList()

    private data class Line(val raw: String) {
        val directions = DIRECTION_REGEX
            .findAll(raw)
            .map { LOOKUP_TABLE[it.value]!! }
            .toList()
        val finalPoint = directions.reduce(Point3D::plus)

        companion object {
            private val DIRECTION_REGEX = "[ns]?[ew]".toRegex()
            private val LOOKUP_TABLE = mapOf(
                "e" to Point3D(1, -1, 0),
                "w" to Point3D(-1, 1, 0),
                "ne" to Point3D(1, 0, -1),
                "nw" to Point3D(0, 1, -1),
                "se" to Point3D(0, -1, 1),
                "sw" to Point3D(-1, 0, 1),
            )
        }
    }

    fun part1() = lines
        .map { it.finalPoint }
        .groupingBy { it }
        .eachCount()
        .count { it.value % 2 == 1 }

    fun part2() = 0
}

fun day24() {
    val day = Day24(DataFile.Part1)
    report(2020, 24, day.part1(), day.part2())
}
