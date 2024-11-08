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
            private val floatingBitPositions = mask
                .map { it.first }
                .toSet()
                .let { (0..35).filter { pos -> pos !in it } }

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

            fun addresses(address: Long): List<Long> {
                var addr = address
                mask.filter { it.second == 1L } // 0 won't change the value so we can skip it
                    .forEach { (idx, bit) -> addr = addr.setBit(idx, bit) }

                val permutations = mutableListOf(addr)
                floatingBitPositions.forEach { index ->
                    val toAdd = permutations
                        .flatMap { listOf(it.setBit(index, 1), it.setBit(index, 0)) }
                    permutations.addAll(toAdd)
                }

                return permutations
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

    fun part1(): Long {
        var mask: Program.Mask = instructions.first { it is Program.Mask } as Program.Mask
        val memory = mutableMapOf<Long, Long>()
        for (instruction in instructions) {
            when (instruction) {
                is Program.Memory -> memory[instruction.position] = mask.applyTo(instruction.value)
                is Program.Mask -> mask = instruction
            }
        }
        return memory.values.sum()
    }

    fun part2(): Long {
        var mask: Program.Mask = instructions.first { it is Program.Mask } as Program.Mask
        val memory = mutableMapOf<Long, Long>()
        for (instruction in instructions) {
            when (instruction) {
                is Program.Mask -> mask = instruction
                is Program.Memory -> {
                    for (addr in mask.addresses(instruction.position)) {
                        memory[addr] = instruction.value
                    }
                }
            }
        }
        return memory.values.sum()
    }
}

fun day14() {
    val day = Day14(DataFile.Part1)
    report(2020, 14, day.part1(), day.part2())
}
