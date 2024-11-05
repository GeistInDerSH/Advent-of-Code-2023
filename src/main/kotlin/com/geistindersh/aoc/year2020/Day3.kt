package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.enums.Point
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.iterators.takeWhileInclusive
import com.geistindersh.aoc.helper.report

class Day3(dataFile: DataFile) {
    private val data = fileToStream(2020, 3, dataFile)
        .flatMapIndexed { row, line -> line.mapIndexed { col, value -> Point(row, col) to value } }
        .toMap()
    private val maxRow = data.keys.maxOf { it.row }
    private val maxCol = data.keys.maxOf { it.col }

    private fun descend() = generateSequence(Point(0, 0)) { it + Point(1, 3) }
        .map {
            if (it in data) {
                it
            } else {
                it.copy(col = it.col % (maxCol + 1))
            }
        }
        .takeWhileInclusive { it.row != maxRow }
        .map { println("$it -- ${data[it]!!}"); it }
        .count { data[it]!! == '#' }

    fun part1() = descend()
    fun part2() = 0
}

fun day3() {
    val day = Day3(DataFile.Part1)
    report(2020, 3, day.part1(), day.part2())
}
