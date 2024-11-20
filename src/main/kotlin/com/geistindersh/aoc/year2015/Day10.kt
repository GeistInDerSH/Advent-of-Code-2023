package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day10(
    dataFile: DataFile,
) {
    private val numbers = fileToString(2015, 10, dataFile)

    private fun seeSay(nums: String) =
        nums
            .map { it.digitToInt() }
            .fold(mutableListOf<MutableList<Int>>()) { acc, i ->
                if (acc.isEmpty() || acc.last().last() != i) {
                    acc.add(mutableListOf(i))
                } else {
                    acc.last().add(i)
                }
                acc
            }.joinToString("") { it.count().toString() + it.last().toString() }

    private fun seeSayTimes(times: Int) =
        generateSequence(numbers) { seeSay(it) }
            .drop(times)
            .take(1)
            .first()
            .length

    fun part1() = seeSayTimes(40)

    fun part2() = seeSayTimes(50)
}

fun day10() {
    val day = Day10(DataFile.Part1)
    report(2015, 10, day.part1(), day.part2())
}
