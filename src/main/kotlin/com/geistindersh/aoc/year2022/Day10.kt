package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day10(
    dataFile: DataFile,
) {
    private val instructions =
        fileToStream(2022, 10, dataFile)
            .map {
                val items = it.split(' ')
                val key = items[0]
                val value = items.getOrNull(1)

                if (value == null) {
                    Pair(key, 0)
                } else {
                    Pair(key, value.toInt())
                }
            }.toList()

    private fun getSignals(): List<Int> {
        val signals = mutableListOf<Int>()
        var xRegister = 1
        var cycle = 1

        for (instruction in instructions) {
            if (cycle == 20 || cycle % 40 == 20) {
                // A relevant interrupt has happened
                signals.add(cycle * xRegister)
            } else if ((cycle + 1) % 40 == 20) {
                // Covers the case of a signal being sent while the CPU
                // is already blocked for the next cycle
                signals.add((cycle + 1) * xRegister)
            }

            cycle += if (instruction.first == "noop") 1 else 2
            xRegister += instruction.second
        }

        return signals
    }

    private fun getLightStatus(
        cycle: Int,
        positions: Set<Int>,
    ) = if (positions.contains(cycle % 40)) "#" else "."

    private fun rasterizeSignal(): String {
        var positions = (0..<3).toSet()
        var xRegister = 1
        var cycle = 1
        val output = StringBuilder()

        for (instruction in instructions) {
            output.append(getLightStatus(cycle, positions))
            if (instruction.first == "noop") {
                cycle += 1
                xRegister += instruction.second
            } else {
                output.append(getLightStatus(cycle + 1, positions))

                cycle += 2
                xRegister += instruction.second
                positions = (xRegister..<xRegister + 3).toSet()
            }
        }

        return output.toString()
    }

    fun part1() = getSignals().sum()

    fun part2(): List<String> {
        val output = rasterizeSignal()
        return (0..<6).map { output.drop(it * 40).take(40) }
    }
}

fun day10() {
    val day = Day10(DataFile.Part1)
    report(2022, 10, day.part1(), day.part2())
}
