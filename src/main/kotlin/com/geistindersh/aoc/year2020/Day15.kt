package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.iterators.takeWhileInclusive
import com.geistindersh.aoc.helper.report

class Day15(dataFile: DataFile) {
    private val numbers = "[0-9]+"
        .toRegex()
        .findAll(fileToString(2020, 15, dataFile))
        .map { it.value.toInt() }
        .toList()
        .let { Game(it.size, it) }

    private data class Game(val turn: Int, val history: List<Int>) {
        val latest = history.last()
        fun next(): Game {
            val nextValue = if (history.count { it == latest } == 1) {
                0
            } else {
                val lastIndex = history.size - 1
                val priorToLast = history.dropLast(1).lastIndexOf(latest)
                lastIndex - priorToLast
            }

            return Game(turn + 1, history + nextValue)
        }
    }

    fun part1() = generateSequence(numbers) { it.next() }
        .takeWhileInclusive { it.turn != 2020 }
        .last()
        .latest

    fun part2() = 0
}

fun day15() {
    val day = Day15(DataFile.Part1)
    report(2020, 15, day.part1(), day.part2())
}
