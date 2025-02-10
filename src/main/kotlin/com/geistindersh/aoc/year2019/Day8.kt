package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day8(
    dataFile: DataFile,
    private val width: Int = 25,
    private val height: Int = 6,
) : AoC<Int, Int> {
    private val data = fileToString(2019, 8, dataFile).map { it.digitToInt() }

    private data class Layer(
        val numbers: List<Int>,
    ) {
        val distribution = numbers.groupingBy { it }.eachCount()
        val zeros: Int = distribution.getOrDefault(0, 0)
        val ones: Int = distribution.getOrDefault(1, 0)
        val twos: Int = distribution.getOrDefault(2, 0)
    }

    private fun List<Int>.toLayers() = this.windowed(width * height, step = width * height).map { Layer(it) }

    override fun part1() = data.toLayers().minBy { it.zeros }.let { it.ones * it.twos }

    override fun part2() = 0
}

fun day8() {
    val day = Day8(DataFile.Part1)
    report(2019, 8, day.part1(), day.part2())
}
