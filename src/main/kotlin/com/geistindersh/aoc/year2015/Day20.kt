package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.iterators.takeWhileInclusive
import com.geistindersh.aoc.helper.report
import kotlin.math.sqrt

class Day20(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val presentsCount = fileToString(2015, 20, dataFile).toInt()

    private fun deliverPresentsInfinite() =
        sequence {
            for (house in 1..<Int.MAX_VALUE) {
                val upperBound = sqrt(house.toDouble()).toInt()
                val presents =
                    (2..upperBound)
                        .fold(house + 1) { presents, elf ->
                            presents + if (house % elf == 0) elf + house / elf else 0
                        }.let {
                            10 * if (upperBound * upperBound == house) it - upperBound else it
                        }

                yield(Pair(house, presents))
            }
        }

    private fun deliverPresentsLimit50() =
        sequence {
            for (house in 1..<Int.MAX_VALUE) {
                val presents = (0..50).reduce { presents, elf -> presents + if (house % elf == 0) house / elf else 0 }
                yield(Pair(house, presents * 11))
            }
        }

    override fun part1() =
        deliverPresentsInfinite()
            .takeWhileInclusive { it.second < presentsCount }
            .last()
            .first

    override fun part2() =
        deliverPresentsLimit50()
            .takeWhileInclusive { it.second < presentsCount }
            .last()
            .first
}

fun day20() {
    val day = Day20(DataFile.Part1)
    report(2015, 20, day.part1(), day.part2())
}
