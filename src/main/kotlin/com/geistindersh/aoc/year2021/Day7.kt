package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day7(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val data =
        fileToString(2021, 7, dataFile)
            .split(",")
            .map(String::toInt)

    override fun part1(): Int {
        val costMap = mutableMapOf<Int, Int>()
        for (num in data) {
            if (num in costMap) continue
            costMap[num] =
                data.fold(0) { acc, value ->
                    acc + if (num > value) num - value else value - num
                }
        }
        return costMap.minOf { it.value }
    }

    override fun part2(): Int {
        val costMap = mutableMapOf<Int, Int>()
        for (num in data) {
            if (num in costMap) continue
            costMap[num] =
                data.fold(0) { acc, value ->
                    val end = if (num > value) num - value else value - num
                    acc + (0..end).sum()
                }
        }
        return costMap.minOf { it.value }
    }
}

fun day7() {
    val day = Day7(DataFile.Part1)
    report(2021, 7, day.part1(), day.part2())
}
