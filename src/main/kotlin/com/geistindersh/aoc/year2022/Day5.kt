package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day5(dataFile: DataFile) {
    private val parts = fileToString(2022, 5, dataFile).split("\n\n")
    private val cargo = run {
        val rows = parts[0].split("\n").dropLast(1)
        val longest = rows.maxOf { it.length } + 1
        val blocks = rows
            .map { line ->
                (0..<longest step 4)
                    .map { line.drop(it).take(3).getOrNull(1) ?: ' ' }
            }
            .reversed()

        (0..<(longest / 4)).map { row ->
            blocks.map { it[row] }
                .filter { it != ' ' }
        }
    }
    private val commands = parts[1]
        .split("\n")
        .map { line -> line.split(" ").mapNotNull { it.toIntOrNull() } }
        .reversed()

    private fun applyMoves(cargo: List<List<Char>>, commands: List<List<Int>>, isPartTwo: Boolean): List<List<Char>> {
        return if (commands[0].size == 3 && commands.size == 1) {
            val command = commands[0]
            val numToMove = command[0]
            val moveFromPos = command[1] - 1
            val moveToPos = command[2] - 1
            val moveFrom = cargo[moveFromPos]
            val moveTo = cargo[moveToPos]
            val toMove = moveFrom.takeLast(numToMove)
            val movedTo = if (!isPartTwo) {
                moveTo + toMove.reversed()
            } else {
                moveTo + toMove
            }
            val movedFrom = moveFrom.dropLast(numToMove)

            cargo.indices.map {
                when (it) {
                    moveFromPos -> movedFrom
                    moveToPos -> movedTo
                    else -> cargo[it]
                }
            }
        } else {
            val command = commands[0]
            val latest = applyMoves(cargo, commands.drop(1), isPartTwo)
            applyMoves(latest, listOf(command), isPartTwo)
        }
    }

    fun part1() = applyMoves(cargo, commands, false)
        .map { it.last() }
        .joinToString("")

    fun part2() = applyMoves(cargo, commands, true)
        .map { it.last() }
        .joinToString("")
}

fun day5() {
    val day = Day5(DataFile.Part1)
    report(2022, 5, day.part1(), day.part2())
}