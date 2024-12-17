package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import kotlin.math.pow

class Day17(
    rawRegisters: List<String>,
    rawProgram: String,
) {
    constructor(lines: List<String>) : this(lines.first().split("\n"), lines.last())
    constructor(dataFile: DataFile) : this(fileToString(2024, 17, dataFile).split("\n\n"))

    private val registers =
        rawRegisters.associate {
            val parts = it.replace(":", "").split(" ")
            parts[1] to parts.last().toLong()
        }
    private val program = rawProgram.substringAfter(": ").split(",").map { it.toLong() }
    private val opCodes = OpCodes.from(program)

    private sealed class OpCodes(
        open val argument: Long,
    ) {
        data class Adv(
            override val argument: Long,
        ) : OpCodes(argument) {
            fun value(registers: Map<String, Long>) = 2.0.pow(comboValue(registers).toDouble()).toLong()
        }

        data class Bxl(
            override val argument: Long,
        ) : OpCodes(argument)

        data class Bst(
            override val argument: Long,
        ) : OpCodes(argument)

        data class Jnz(
            override val argument: Long,
        ) : OpCodes(argument)

        data class Bxc(
            override val argument: Long,
        ) : OpCodes(argument)

        data class Out(
            override val argument: Long,
        ) : OpCodes(argument)

        data class Bdv(
            override val argument: Long,
        ) : OpCodes(argument) {
            fun value(registers: Map<String, Long>) = 2.0.pow(comboValue(registers).toDouble()).toLong()
        }

        data class Cdv(
            override val argument: Long,
        ) : OpCodes(argument) {
            fun value(registers: Map<String, Long>) = 2.0.pow(comboValue(registers).toDouble()).toLong()
        }

        fun comboValue(registers: Map<String, Long>) =
            when (argument) {
                0L, 1L, 2L, 3L -> argument
                4L -> registers["A"]!!
                5L -> registers["B"]!!
                6L -> registers["C"]!!
                else -> throw IllegalArgumentException("Unknown register $argument")
            }

        companion object {
            fun from(nums: List<Long>) =
                nums
                    .windowed(2, 1)
                    .map { (l, r) ->
                        when (l) {
                            0L -> Adv(r)
                            1L -> Bxl(r)
                            2L -> Bst(r)
                            3L -> Jnz(r)
                            4L -> Bxc(r)
                            5L -> Out(r)
                            6L -> Bdv(r)
                            7L -> Cdv(r)
                            else -> throw IllegalArgumentException("Unknown opcode: $l")
                        }
                    }
        }
    }

    private fun List<OpCodes>.run(registers: Map<String, Long>): List<Long> {
        val registers = registers.toMutableMap()
        var rip = 0
        val output = mutableListOf<Long>()

        while (rip < this.size) {
            when (val op = this[rip]) {
                is OpCodes.Adv -> {
                    val a = registers["A"]!!
                    registers["A"] = a / op.value(registers)
                    rip += 2
                }
                is OpCodes.Bxl -> {
                    val b = registers["B"]!!
                    registers["B"] = b xor op.argument
                    rip += 2
                }
                is OpCodes.Bst -> {
                    registers["B"] = op.comboValue(registers) % 8L
                    rip += 2
                }
                is OpCodes.Jnz -> {
                    rip = if (registers["A"]!! == 0L) rip + 2 else op.argument.toInt()
                }
                is OpCodes.Bxc -> {
                    val b = registers["B"]!!
                    val c = registers["C"]!!
                    registers["B"] = b xor c
                    rip += 2
                }
                is OpCodes.Out -> {
                    output.add(op.comboValue(registers) % 8)
                    rip += 2
                }
                is OpCodes.Bdv -> {
                    val a = registers["A"]!!
                    registers["B"] = a / op.value(registers)
                    rip += 2
                }
                is OpCodes.Cdv -> {
                    val a = registers["A"]!!
                    registers["C"] = a / op.value(registers)
                    rip += 2
                }
            }
        }
        return output
    }

    private fun List<OpCodes>.run(aRegister: Long) = this.run(registers.toMutableMap().apply { put("A", aRegister) })

    fun part1() = opCodes.run(registers).joinToString(",") { it.toString() }

    fun part2(): Long {
        var candidates = sortedSetOf(0L)
        for (instr in program.reversed()) {
            val newCandidates = sortedSetOf<Long>()
            for (candidate in candidates) {
                val value = candidate shl 3
                for (i in 0..<8) {
                    val output = opCodes.run(value + i)
                    if (output.first() == instr) newCandidates.add(value + i)
                }
            }
            candidates = newCandidates
        }
        return candidates.first()
    }
}

fun day17() {
    val day = Day17(DataFile.Part1)
    report(2024, 17, day.part1(), day.part2())
}
