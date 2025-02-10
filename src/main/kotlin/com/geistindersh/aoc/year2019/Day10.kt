package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.math.gcd
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.toGrid2D
import kotlin.math.absoluteValue

class Day10(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val asteroidField = fileToString(2019, 10, dataFile).toGrid2D()
    private val asteroids = asteroidField.filterValues { it == '#' }.keys

    private fun Point2D.rayCast(): Set<Point2D> {
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
                if (asteroidField[possiblePoint]!! == '#') {
                    visible.remove(possiblePoint)
                    tried.add(possiblePoint)
                }
                possiblePoint += minSlope
            }
        }

        return visible
    }

    override fun part1() = asteroids.map { it.rayCast() }.maxOf { it.count() }

    override fun part2() = 0
}

fun day10() {
    val day = Day10(DataFile.Part1)
    report(2019, 10, day.part1(), day.part2())
}
