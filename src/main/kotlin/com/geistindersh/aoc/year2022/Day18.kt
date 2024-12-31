package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import java.util.ArrayDeque
import kotlin.math.max
import kotlin.math.min

class Day18(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val allCubes =
        fileToStream(2022, 18, dataFile)
            .map {
                val (x, y, z) = it.split(",")
                Triple(x.toInt(), y.toInt(), z.toInt())
            }.toSet()

    private fun neighbors(triple: Triple<Int, Int, Int>): List<Triple<Int, Int, Int>> =
        listOf(
            Triple(triple.first - 1, triple.second, triple.third),
            Triple(triple.first + 1, triple.second, triple.third),
            Triple(triple.first, triple.second - 1, triple.third),
            Triple(triple.first, triple.second + 1, triple.third),
            Triple(triple.first, triple.second, triple.third - 1),
            Triple(triple.first, triple.second, triple.third + 1),
        )

    private fun calculateSurfaceAreaOfCubes(cubes: Collection<Triple<Int, Int, Int>>): Int {
        var surfaceArea = 0
        for (cube in cubes) {
            surfaceArea +=
                neighbors(cube)
                    .filterNot { it in cubes }
                    .count()
        }
        return surfaceArea
    }

    private fun getRanges(): Triple<IntRange, IntRange, IntRange> {
        var minX = 0
        var maxX = 0
        var minY = 0
        var maxY = 0
        var minZ = 0
        var maxZ = 0
        for ((x, y, z) in allCubes) {
            minX = min(minX, x)
            maxX = max(maxX, x)
            minY = min(minY, y)
            maxY = max(maxY, y)
            minZ = min(minZ, z)
            maxZ = max(maxZ, z)
        }
        return Triple(minX..maxX, minY..maxY, minZ..maxZ)
    }

    private fun getWaterPockets(ranges: Triple<IntRange, IntRange, IntRange>): Set<Triple<Int, Int, Int>> {
        val (xRange, yRange, zRange) = ranges
        val queue =
            ArrayDeque<Triple<Int, Int, Int>>().apply {
                add(Triple(xRange.first, yRange.first, zRange.first))
            }
        val water = mutableSetOf<Triple<Int, Int, Int>>()

        while (queue.isNotEmpty()) {
            val head = queue.removeFirst()
            if (head in water) continue
            water.add(head)

            val toAdd =
                neighbors(head)
                    .filter {
                        val (x, y, z) = it
                        x in xRange && y in yRange && z in zRange && it !in allCubes
                    }.toList()
            queue.addAll(toAdd)
        }
        return water
    }

    private fun getLavaPockets(
        waterPockets: Set<Triple<Int, Int, Int>>,
        ranges: Triple<IntRange, IntRange, IntRange>,
    ): Set<Triple<Int, Int, Int>> {
        val pockets = mutableSetOf<Triple<Int, Int, Int>>()
        for (x in ranges.first) {
            for (y in ranges.second) {
                for (z in ranges.third) {
                    val t = Triple(x, y, z)
                    if (t !in waterPockets) pockets.add(t)
                }
            }
        }
        return pockets
    }

    override fun part1() = calculateSurfaceAreaOfCubes(allCubes)

    override fun part2(): Int {
        val ranges = getRanges()
        val waterPockets = getWaterPockets(ranges)
        val lavaPockets = getLavaPockets(waterPockets, ranges)
        return calculateSurfaceAreaOfCubes(lavaPockets)
    }
}

fun day18() {
    val day = Day18(DataFile.Part1)
    report(2022, 18, day.part1(), day.part2())
}
