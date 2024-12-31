package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.ranges.hasOverlap
import com.geistindersh.aoc.helper.ranges.isFullyContained
import com.geistindersh.aoc.helper.report

class Day4(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val pairs =
        fileToStream(2022, 4, dataFile)
            .map { line ->
                val pairs =
                    line
                        .split(',')
                        .map {
                            val range = it.split("-", limit = 2).map(String::toInt)
                            range[0]..range[1]
                        }.toList()

                if (pairs[0].count() > pairs[1].count()) {
                    Pair(pairs[0], pairs[1])
                } else {
                    Pair(pairs[1], pairs[0])
                }
            }.toList()

    override fun part1(): Int = pairs.count { (r1, r2) -> r1.isFullyContained(r2) }

    override fun part2(): Int = pairs.count { (r1, r2) -> r1.hasOverlap(r2) }
}

fun day4() {
    val day = Day4(DataFile.Part1)
    report(2022, 4, day.part1(), day.part2())
}
