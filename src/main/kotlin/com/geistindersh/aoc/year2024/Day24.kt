package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day24(
    rawRegisters: List<String>,
    rawInstructions: List<String>,
) : AoC<Long, String> {
    constructor(rawInput: String) : this(
        rawInput.substringBefore("\n\n").split("\n"),
        rawInput.substringAfter("\n\n").split("\n"),
    )

    constructor(dataFile: DataFile) : this(fileToString(2024, 24, dataFile))

    private data class Instruction(
        val r1: String,
        val r2: String,
        val cmd: String,
        var dest: String,
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

    private fun Map<String, Int>.output() = this.registerValue('z')

    private fun Map<String, Int>.registerValue(startsWith: Char) =
        StringBuilder().let { builder ->
            this.keys
                .filter { it.startsWith(startsWith) }
                .sorted()
                .reversed()
                .forEach { builder.append(this[it]!!) }
            builder.toString().toLong(2)
        }

    private fun findFirstOutput(name: String): String? {
        val parentWires = instructions.filter { it.r1 == name || it.r2 == name }
        val validOutWire = parentWires.find { it.dest.startsWith('z') }
        if (validOutWire == null) return parentWires.firstNotNullOfOrNull { findFirstOutput(it.dest) }
        return "z" + (validOutWire.dest.drop(1).toInt() - 1).toString().padStart(2, '0')
    }

    override fun part1() = run(registers.toMutableMap(), instructions.toMutableList()).output()

    override fun part2(): String {
        val instructions = instructions.toMutableList()
        val badEndWires = instructions.filter { it.dest.startsWith('z') && it.cmd != "XOR" && it.dest != "z45" }
        val xy = setOf('x', 'y')
        val badMidWires =
            instructions
                .filter { it.cmd == "XOR" && !it.dest.startsWith('z') && it.r1.first() !in xy && it.r2.first() !in xy }
        for (i in badMidWires.indices) {
            val wire = badMidWires[i]
            val switchWith = badEndWires.first { it.dest == findFirstOutput(wire.dest) }
            val tmp = wire.dest
            wire.dest = switchWith.dest
            switchWith.dest = tmp
        }

        val xIn = registers.registerValue('x')
        val yIn = registers.registerValue('y')
        val updatedRegisters = run(registers.toMutableMap(), instructions.toMutableList())
        val difference = xIn + yIn xor updatedRegisters.output()
        val zBits = difference.countTrailingZeroBits().toString().padStart(2, '0')
        val badCarryWires = instructions.filter { it.r1.endsWith(zBits) && it.r2.endsWith(zBits) }

        return (badCarryWires + badMidWires + badEndWires).map { it.dest }.sorted().joinToString(",")
    }
}

fun day24() {
    val day = Day24(DataFile.Part1)
    report(2024, 24, day.part1(), day.part2())
}
