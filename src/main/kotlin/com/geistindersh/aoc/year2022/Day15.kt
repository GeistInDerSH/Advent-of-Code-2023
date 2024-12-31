package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.iterators.pairCombinations
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.extractIntegers
import kotlin.math.absoluteValue

class Day15(
    dataFile: DataFile,
    private val part1RowNumber: Int,
) : AoC<Int, Long> {
    private val sensors: List<Pair<Pair<Int, Int>, Int>>
    private val beacons: Set<Pair<Int, Int>>

    init {
        val data =
            fileToStream(2022, 15, dataFile)
                .map { line ->
                    val (start, end) = line.split(":", limit = 2)
                    val sensor = start.extractIntegers().let { Pair(it[0], it[1]) }
                    val beacon = end.extractIntegers().let { Pair(it[0], it[1]) }
                    val dist =
                        (sensor.first - beacon.first).absoluteValue +
                            (sensor.second - beacon.second).absoluteValue

                    Pair(Pair(sensor, dist), beacon)
                }.toList()
        sensors = data.map { it.first }
        beacons = data.map { it.second }.toSet()
    }

    private fun rowCoverageCount(
        rowNumber: Int,
        sensors: Pair<Pair<Int, Int>, Int>,
    ) = sequence {
        val (x, y) = sensors.first
        val dist = sensors.second
        val verticalDist = (rowNumber - y).absoluteValue
        val horizontalDist = dist - verticalDist
        val range = (x - horizontalDist..x + horizontalDist)
        for (it in range) yield(Pair(it, rowNumber))
    }

    private fun boundsAdd(
        x: Int,
        y: Int,
    ): Pair<Int, Int>? =
        if (x in 0..4000000 && y in 0..4000000) {
            Pair(x, y)
        } else {
            null
        }

    private fun generateCoverageMap(): Map<Pair<Int, Int>, Set<Pair<Int, Int>>> =
        sensors
            .parallelStream()
            .map { (sensor, dist) ->
                val (x, y) = sensor
                val values =
                    (0..<dist + 2)
                        .flatMap {
                            val px = x - dist - 1 + it
                            val py = y + it
                            val nx = x + dist + 1 - it
                            val ny = y - it
                            setOf(boundsAdd(px, ny), boundsAdd(nx, ny), boundsAdd(px, py), boundsAdd(nx, py))
                        }.mapNotNull { it }
                        .toSet()
                sensor to values
            }.toList()
            .toMap()

    private fun getIntersections(): Sequence<Set<Pair<Int, Int>>> {
        val radius = generateCoverageMap()
        return sensors
            .pairCombinations()
            .map { (fst, snd) ->
                val s1 = fst.first
                val s2 = snd.first
                val p1 = radius.getOrDefault(s1, emptySet())
                val p2 = radius.getOrDefault(s2, emptySet())
                p1.intersect(p2)
            }.filter { it.isNotEmpty() }
    }

    private fun getIntersectionCount(): Map<Pair<Int, Int>, Int> {
        val values = mutableMapOf<Pair<Int, Int>, Int>()
        for (intersection in getIntersections()) {
            intersection.forEach {
                values[it] = values.getOrDefault(it, 0) + 1
            }
        }

        return values
    }

    private fun isInside(point: Pair<Int, Int>): Boolean =
        sensors
            .any { (sensor, dist) ->
                val pointDistance =
                    (sensor.first - point.first).absoluteValue +
                        (sensor.second - point.second).absoluteValue
                pointDistance < dist
            }

    override fun part1(): Int =
        sensors
            .asSequence()
            .flatMap { rowCoverageCount(part1RowNumber, it) }
            .toSet()
            .count { it !in beacons }

    override fun part2(): Long =
        getIntersectionCount()
            .filterValues { it >= 4 }
            .filterKeys { !isInside(it) }
            .map { it.key }
            .reversed()
            .first()
            .let { (x, y) -> x * 4000000L + y }
}

fun day15() {
    val day = Day15(DataFile.Part1, 2000000)
    report(2022, 15, day.part1(), "skipped")
}
