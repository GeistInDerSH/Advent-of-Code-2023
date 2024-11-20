package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.iterators.rotateLeft
import com.geistindersh.aoc.helper.report

class Day6(dataFile: DataFile) {
    private val lanternFish =
        fileToString(2021, 6, dataFile)
            .split(",")
            .map(String::toInt)
            .let {
                val fish = LongArray(9) { 0L }
                it.forEach { i ->
                    fish[i] += 1L
                }
                fish
            }

    private fun solution(days: Int): Long =
        generateSequence(lanternFish) {
            val fish = it.rotateLeft(1)
            fish[6] += fish[8]
            fish
        }
            .drop(days)
            .first()
            .sum()

    fun part1() = solution(80)

    fun part2() = solution(256)
}

fun day6() {
    val day = Day6(DataFile.Part1)
    report(2021, 6, day.part1(), day.part2())
}
