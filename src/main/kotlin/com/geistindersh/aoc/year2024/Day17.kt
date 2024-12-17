package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day17(
    registers: Map<String, Long>,
    private val program: List<Long>,
) {
    constructor(rawRegisters: List<String>, rawProgram: String) : this(
        rawRegisters.associate { line -> line.replace(":", "").split(" ").let { it[1] to it.last().toLong() } },
        rawProgram.substringAfter(": ").split(",").map { it.toLong() },
    )
    constructor(lines: List<String>) : this(lines.first().split("\n"), lines.last())
    constructor(dataFile: DataFile) : this(fileToString(2024, 17, dataFile).split("\n\n"))

    val a = registers.getOrDefault("A", 0L)
    val b = registers.getOrDefault("B", 0L)
    val c = registers.getOrDefault("C", 0L)
    private val opCodes = OpCodes.from(program)

    private sealed class OpCodes(
        open val argument: Long,
    ) {
        data class Adv(
            override val argument: Long,
        ) : OpCodes(argument)

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
        ) : OpCodes(argument)

        data class Cdv(
            override val argument: Long,
        ) : OpCodes(argument)

        fun comboValue(
            a: Long,
            b: Long,
            c: Long,
        ) = when (argument) {
            0L, 1L, 2L, 3L -> argument
            4L -> a
            5L -> b
            6L -> c
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

    private fun List<OpCodes>.run(
        registerA: Long,
        registerB: Long,
        registerC: Long,
    ): List<Long> {
        val output = mutableListOf<Long>()
        var rip = 0
        var ax = registerA
        var bx = registerB
        var cx = registerC

        while (rip < this.size) {
            when (val op = this[rip]) {
                is OpCodes.Adv -> {
                    ax = ax shr op.comboValue(ax, bx, cx).toInt()
                    rip += 2
                }
                is OpCodes.Bxl -> {
                    bx = bx xor op.argument
                    rip += 2
                }
                is OpCodes.Bst -> {
                    bx = op.comboValue(ax, bx, cx) and 7L
                    rip += 2
                }
                is OpCodes.Jnz -> {
                    rip = if (ax == 0L) rip + 2 else op.argument.toInt()
                }
                is OpCodes.Bxc -> {
                    bx = bx xor cx
                    rip += 2
                }
                is OpCodes.Out -> {
                    output.add(op.comboValue(ax, bx, cx) and 7L)
                    rip += 2
                }
                is OpCodes.Bdv -> {
                    bx = ax shr op.comboValue(ax, bx, cx).toInt()
                    rip += 2
                }
                is OpCodes.Cdv -> {
                    cx = ax shr op.comboValue(ax, bx, cx).toInt()
                    rip += 2
                }
            }
        }
        return output
    }

    fun part1() = opCodes.run(a, b, c).joinToString(",") { it.toString() }

    fun part2(): Long {
        var candidates = sortedSetOf(0L)
        for (instr in program.reversed()) {
            val newCandidates = sortedSetOf<Long>()
            for (candidate in candidates) {
                val value = candidate shl 3
                for (i in 0..<8) {
                    val output = opCodes.run(value + i, b, c)
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
