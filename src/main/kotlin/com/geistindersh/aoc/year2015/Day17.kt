package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.iterators.subsetSum
import com.geistindersh.aoc.helper.report

class Day17(
    dataFile: DataFile,
    private val target: Int,
) : AoC<Int, Int> {
    private val numbers = fileToStream(2015, 17, dataFile).map(String::toInt).toList()

    override fun part1() = numbers.subsetSum(target).count()

    override fun part2(): Int {
        val sums = numbers.subsetSum(target).sortedBy { it.size }
        val min = sums.minOf { it.size }
        return sums.filter { it.size == min }.count()
    }
}

fun day17() {
    val day = Day17(DataFile.Part1, 150)
    report(2015, 17, day.part1(), day.part2())
}
