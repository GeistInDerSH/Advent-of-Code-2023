package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.iterators.pairCombinations
import com.geistindersh.aoc.helper.report
import kotlin.math.absoluteValue

class Day19(
    dataFile: DataFile,
) {
    private val scanners =
        fileToString(2021, 19, dataFile)
            .split("\n\n")
            .map { line ->
                val beacons =
                    line
                        .split("\n")
                        .drop(1)
                        .map { it.split(",").map(String::toInt) }
                        .map { Beacon.from(it) }
                        .toSet()
                Scanner(beacons)
            }.toList()

    private data class Scanner(
        val beacons: Set<Beacon>,
    ) {
        fun transformationIntersection(other: Scanner): Pair<Scanner, Beacon>? {
            for (facing in 0..<6) {
                for (rotation in 0..<4) {
                    val rotated = other.beacons.map { it.face(facing).rotate(rotation) }.toSet()
                    for (b1 in beacons) {
                        for (b2 in rotated) {
                            val diff = b1 - b2
                            val adjusted = rotated.map { it + diff }.toSet()
                            if (adjusted.intersect(beacons).size < 12) {
                                continue
                            }
                            return Scanner(adjusted) to diff
                        }
                    }
                }
            }
            return null
        }
    }

    private data class Beacon(
        val x: Int,
        val y: Int,
        val z: Int,
    ) {
        operator fun plus(other: Beacon) = Beacon(x + other.x, y + other.y, z + other.z)

        operator fun minus(other: Beacon) = Beacon(x - other.x, y - other.y, z - other.z)

        fun distanceTo(other: Beacon) = (x - other.x).absoluteValue + (y - other.y).absoluteValue + (z - other.z).absoluteValue

        fun face(dir: Int): Beacon =
            when (dir) {
                0 -> this
                1 -> Beacon(x, -y, -z)
                2 -> Beacon(x, -z, y)
                3 -> Beacon(-y, -z, x)
                4 -> Beacon(y, -z, -x)
                5 -> Beacon(-x, -z, -y)
                else -> throw IllegalArgumentException("Unknown beacon: $dir")
            }

        fun rotate(dir: Int): Beacon =
            when (dir) {
                0 -> this
                1 -> Beacon(-y, x, z)
                2 -> Beacon(-x, -y, z)
                3 -> Beacon(y, -x, z)
                else -> throw IllegalArgumentException("Unknown direction: $dir")
            }

        companion object {
            fun from(list: List<Int>) = Beacon(list[0], list[1], list[2])
        }
    }

    private fun getScannerPositions(): Pair<Scanner, Set<Beacon>> {
        var start = scanners.first()
        val foundScannerPoints = mutableSetOf<Beacon>()
        val unmappedScanners = ArrayDeque<Scanner>().apply { addAll(scanners.drop(1)) }
        while (unmappedScanners.isNotEmpty()) {
            val toMap = unmappedScanners.removeFirst()
            val pair = start.transformationIntersection(toMap)
            if (pair == null) {
                unmappedScanners.add(toMap)
            } else {
                val (scanner, node) = pair
                start = Scanner(start.beacons + scanner.beacons)
                foundScannerPoints.add(node)
            }
        }
        return start to foundScannerPoints
    }

    fun part1() = getScannerPositions().first.beacons.size

    fun part2() =
        getScannerPositions()
            .second
            .toList()
            .pairCombinations()
            .maxOf { (a, b) -> a.distanceTo(b) }
}

fun day19() {
    val day = Day19(DataFile.Part1)
    report(2021, 19, day.part1(), day.part2())
}
