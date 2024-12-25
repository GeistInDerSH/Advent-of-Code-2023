package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day24(
    rawRegisters: List<String>,
    rawInstructions: List<String>,
) {
    constructor(rawInput: String) : this(
        rawInput.substringBefore("\n\n").split("\n"),
        rawInput.substringAfter("\n\n").split("\n"),
    )

    constructor(dataFile: DataFile) : this(fileToString(2024, 24, dataFile))

    private data class Instruction(
        val r1: String,
        val r2: String,
        val cmd: String,
        val dest: String,
    ) {
        fun call(registers: Map<String, Int>) = call(registers[r1]!!, registers[r2]!!)

        fun call(
            v1: Int,
            v2: Int,
        ) = when (cmd) {
            "AND" -> v1 and v2
            "OR" -> v1 or v2
            "XOR" -> v1 xor v2
            else -> throw IllegalArgumentException("Unknown cmd: $cmd")
        }

        companion object {
            fun from(parts: List<String>) = Instruction(parts[0], parts[2], parts[1], parts[4])

            fun from(string: String) = from(string.split(" "))
        }
    }

    private val registers = rawRegisters.associate { it.substringBefore(": ") to it.substringAfter(": ").toInt() }
    private val instructions = rawInstructions.map { Instruction.from(it) }

    private fun run(
        registers: MutableMap<String, Int>,
        instructions: MutableList<Instruction>,
    ): Map<String, Int> {
        while (instructions.isNotEmpty()) {
            instructions
                .filter { it.r1 in registers && it.r2 in registers }
                .also { instructions.removeAll(it) }
                .forEach { registers[it.dest] = it.call(registers) }
        }
        return registers
    }

    fun part1(): Long {
        val builder = StringBuilder()
        val updatedRegisters = run(registers.toMutableMap(), instructions.toMutableList())
        updatedRegisters.keys
            .filter { it.startsWith('z') }
            .sorted()
            .reversed()
            .forEach { builder.append(updatedRegisters[it]!!) }

        return builder.toString().toLong(2)
    }

    fun part2() = 0
}

fun day24() {
    val day = Day24(DataFile.Part1)
    report(2024, 24, day.part1(), day.part2())
}
