package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day8(dataFile: DataFile) {
    private val instructions =
        fileToStream(2020, 8, dataFile)
            .map { Instruction.from(it) }
            .toList()

    private sealed class Instruction(open val count: Int) {
        data class Nop(override val count: Int) : Instruction(count)

        data class Jmp(override val count: Int) : Instruction(count)

        data class Acc(override val count: Int) : Instruction(count)

        companion object {
            fun from(line: String): Instruction {
                val (name, value) = line.split(" ")
                return when (name) {
                    "nop" -> Nop(value.toInt())
                    "jmp" -> Jmp(value.toInt())
                    "acc" -> Acc(value.toInt())
                    else -> throw IllegalArgumentException("Invalid line $line")
                }
            }
        }
    }

    private fun List<Instruction>.execute(): Pair<Int, Boolean> {
        var accumulator = 0
        var rip = 0
        val indicesSeen = mutableSetOf<Int>()
        while (rip !in indicesSeen && rip < this.size) {
            indicesSeen.add(rip)
            rip +=
                when (val instruction = this[rip]) {
                    is Instruction.Acc -> {
                        accumulator += instruction.count
                        1
                    }

                    is Instruction.Jmp -> instruction.count
                    is Instruction.Nop -> 1
                }
        }

        return accumulator to (rip >= this.size)
    }

    private fun List<Instruction>.executeWithInvert() =
        sequence {
            for (i in this@executeWithInvert.indices) {
                if (this@executeWithInvert[i] is Instruction.Acc) continue

                val lst = this@executeWithInvert.toMutableList()
                lst[i] =
                    when (val instruction = lst[i]) {
                        is Instruction.Nop -> Instruction.Jmp(instruction.count)
                        is Instruction.Jmp -> Instruction.Nop(instruction.count)
                        else -> throw IllegalArgumentException("Invalid instruction $instruction")
                    }
                yield(lst.execute())
            }
        }

    fun part1() = instructions.execute().first

    fun part2() =
        instructions
            .executeWithInvert()
            .first { it.second }
            .first
}

fun day8() {
    val day = Day8(DataFile.Part1)
    report(2020, 8, day.part1(), day.part2())
}
