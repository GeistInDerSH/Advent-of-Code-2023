package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day1(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val masses =
        fileToStream(2019, 1, dataFile)
            .map(String::toInt)
            .toList()

    private fun cost(mass: Int) = (mass / 3) - 2

    private tailrec fun fuelCost(
        mass: Int,
        totalMass: Int = 0,
    ): Int {
        val fuel = cost(mass)
        return if (fuel > 0) {
            fuelCost(fuel, totalMass + fuel)
        } else {
            totalMass
        }
    }

    override fun part1() = masses.sumOf(::cost)

    override fun part2() = masses.sumOf(::fuelCost)
}

fun day1() {
    val day = Day1(DataFile.Part1)
    report(2019, 1, day.part1(), day.part2())
}
