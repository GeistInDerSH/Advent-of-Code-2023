package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.iterators.subsetSum
import com.geistindersh.aoc.helper.report

class Day1(
    dataFile: DataFile,
) : AoC<Long, Long> {
    private val numbers = fileToStream(2020, 1, dataFile).map(String::toInt).toList()

    private fun solution(size: Int) =
        numbers
            .subsetSum(2020)
            .filter { it.size == size }
            .maxOf { it.fold(1, Long::times) }

    override fun part1() = solution(2)

    override fun part2() = solution(3)
}

fun day1() {
    val day = Day1(DataFile.Part1)
    report(2020, 1, day.part1(), day.part2())
}
