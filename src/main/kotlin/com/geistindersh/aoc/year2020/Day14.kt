package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.binary.setBit
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day14(dataFile: DataFile) {
    private val instructions = fileToStream(2020, 14, dataFile)
        .map { Program.from(it) }
        .toList()

    private sealed class Program {

        data class Mask(val mask: List<Pair<Int, Long>>) : Program() {
            override fun toString(): String = StringBuilder().let {
                it.append("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX")
                for ((idx, value) in mask) {
                    it[idx] = value.toInt().digitToChar()
                }
                it.toString().reversed()
            }

            fun applyTo(value: Long): Long {
                var number = value
                for ((idx, bit) in mask) {
                    number = number.setBit(idx, bit)
                }
                return number
            }
        }

        data class Memory(val position: Long, val value: Long) : Program()

        companion object {
            private val NUMBER_REGEX = "[0-9]+".toRegex()

            fun from(line: String): Program {
                return if (line.startsWith("mask = ")) {
                    val bitSetLocation = line
                        .substringAfter("mask = ")
                        .reversed() // Ensure the index sets the LSB first
                        .withIndex()
                        .filter { it.value.isDigit() }
                        .map { it.index to it.value.digitToInt().toLong() }
                    Mask(bitSetLocation)
                } else {
                    val (position, value) = NUMBER_REGEX
                        .findAll(line)
                        .map { it.value.toLong() }
                        .toList()
                    Memory(position, value)
                }
            }
        }
    }

    private fun List<Program>.run(): Long {
        var mask: Program.Mask = this.first { it is Program.Mask } as Program.Mask
        val memory = mutableMapOf<Long, Long>()
        for (instruction in this) {
            when (instruction) {
                is Program.Memory -> memory[instruction.position] = mask.applyTo(instruction.value)

                is Program.Mask -> mask = instruction
            }
        }
        return memory.values.sum()
    }

    fun part1() = instructions.run()
    fun part2() = 0
}

fun day14() {
    val day = Day14(DataFile.Part1)
    report(2020, 14, day.part1(), day.part2())
}
