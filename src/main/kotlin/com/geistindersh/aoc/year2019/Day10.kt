package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.math.gcd
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2D
import kotlin.math.absoluteValue
import kotlin.math.atan2

class Day10(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val asteroidField = fileToString(2019, 10, dataFile).toGrid2D()
    private val asteroids = asteroidField.filterValues { it == '#' }.keys

    private fun Point2D.rayCast() = this.rayCast(this@Day10.asteroids)

    private fun Point2D.rayCast(asteroids: Set<Point2D>): Set<Point2D> {
        val visible = mutableSetOf<Point2D>()
        val tried = mutableSetOf(this)
        for (asteroid in asteroids) {
            if (!tried.add(asteroid)) continue
            visible.add(asteroid)

            // Find the 'slope' of the line, and reduce it to the smallest ratio
            val slope = asteroid - this
            val div = gcd(slope.row, slope.col).absoluteValue
            val minSlope = Point2D(slope.row / div, slope.col / div)

            // Remove any points along the slope after the asteroid in question
            var possiblePoint = asteroid + minSlope
            while (possiblePoint in asteroidField) {
                if (possiblePoint in asteroids) {
                    visible.remove(possiblePoint)
                    tried.add(possiblePoint)
                }
                possiblePoint += minSlope
            }
        }

        return visible
    }

    private fun Point2D.angleBetween(other: Point2D): Double {
        val c = 180.0 / Math.PI
        val r = atan2((other.col - this.col).toDouble(), (this.row - other.row).toDouble()) * c
        return if (r < 0) r + 360 else r
    }

    override fun part1() = asteroids.map { it.rayCast() }.maxOf { it.count() }

    override fun part2(): Int {
        val laserOrigin = asteroids.maxBy { it.rayCast().count() }
        val asteroids = asteroids.toMutableSet().also { it.remove(laserOrigin) }
        val angles =
            asteroids
                .map { laserOrigin.angleBetween(it) to it }
                .sortedWith(
                    compareBy { it: Pair<Double, Point2D> -> it.first }
                        .thenComparing {
                            (laserOrigin.col - it.second.col).absoluteValue +
                                (laserOrigin.row - it.second.row).absoluteValue
                        },
                ).toMutableList()
        var (lastAngle, last) = angles.removeFirst()
        var removedCount = 1
        var idx = 0
        while (removedCount < 200 && angles.isNotEmpty()) {
            if (idx >= angles.size) {
                idx = 0
            }
            if (lastAngle == angles[idx].first) {
                idx += 1
                continue
            }
            val (la, p) = angles.removeAt(idx)
            lastAngle = la
            last = p
            removedCount += 1
        }
        return last.col * 100 + last.row
    }
}

fun day10() {
    val day = Day10(DataFile.Part1)
    report(2019, 10, day.part1(), day.part2())
}
