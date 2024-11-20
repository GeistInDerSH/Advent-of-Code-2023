package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day24(dataFile: DataFile) {
    private val rawInput = fileToStream(2021, 24, dataFile).toList()
    private val instructions =
        rawInput
            .map { Instruction.from(it) }
            .toList()
    private val decidingInstructions =
        rawInput.let {
            val relevant = mutableListOf<Triple<Int, Int, Int>>()
            var i = 5
            var j = 15
            var index = 0
            while (i < it.size && j < it.size) {
                val x = it[i].split(" ")[2].toInt()
                val y = it[j].split(" ")[2].toInt()
                relevant.add(Triple(index, x, y))
                index += 1
                i += 18
                j += 18
            }
            relevant
        }

    private sealed class Instruction {
        data class Inp(val a: String) : Instruction()

        data class Add(val a: String, val b: String) : Instruction()

        data class Mul(val a: String, val b: String) : Instruction()

        data class Div(val a: String, val b: String) : Instruction()

        data class Mod(val a: String, val b: String) : Instruction()

        data class Eql(val a: String, val b: String) : Instruction()

        companion object {
            fun from(line: String): Instruction {
                val parts = line.split(" ")
                return when (parts[0]) {
                    "inp" -> Inp(parts[1])
                    "add" -> Add(parts[1], parts[2])
                    "mul" -> Mul(parts[1], parts[2])
                    "div" -> Div(parts[1], parts[2])
                    "mod" -> Mod(parts[1], parts[2])
                    "eql" -> Eql(parts[1], parts[2])
                    else -> throw IllegalArgumentException("Unexpected line $line")
                }
            }
        }
    }

    private fun getModelNumber(maximize: Boolean): Long {
        val defaultValue = if (maximize) 9 else 1

        val digits = IntArray(14) { 0 }
        val queue = ArrayDeque<Triple<Int, Int, Int>>()
        for (entry in decidingInstructions) {
            if (entry.second >= 10) {
                queue.addFirst(entry)
                continue
            }

            val (cIndex, cX, _) = entry
            val (pIndex, _, pY) = queue.removeFirst()
            val value = pY + cX

            if (maximize) {
                if (value >= 0) {
                    digits[cIndex] = defaultValue
                    digits[pIndex] = defaultValue - value
                } else {
                    digits[pIndex] = defaultValue
                    digits[cIndex] = defaultValue + value
                }
            } else {
                if (value >= 0) {
                    digits[pIndex] = defaultValue
                    digits[cIndex] = defaultValue + value
                } else {
                    digits[cIndex] = defaultValue
                    digits[pIndex] = defaultValue - value
                }
            }
        }

        if (!isValid(digits)) {
            throw Exception("Invalid Model Number: ${digits.joinToString { it.toString() }}")
        }
        return digits.fold(0L) { acc, d -> acc * 10 + d }
    }

    private fun isValid(modelNumber: IntArray): Boolean {
        val registers = mutableMapOf("w" to 0, "x" to 0, "y" to 0, "z" to 0)
        var i = 0
        for (instruction in instructions) {
            when (instruction) {
                is Instruction.Inp -> {
                    registers[instruction.a] = modelNumber[i]
                    i += 1
                }

                is Instruction.Add -> {
                    val value = instruction.b.toIntOrNull() ?: registers[instruction.b]!!
                    registers[instruction.a] = registers[instruction.a]!! + value
                }

                is Instruction.Div -> {
                    val value = instruction.b.toIntOrNull() ?: registers[instruction.b]!!
                    registers[instruction.a] = registers[instruction.a]!! / value
                }

                is Instruction.Eql -> {
                    val value = instruction.b.toIntOrNull() ?: registers[instruction.b]!!
                    registers[instruction.a] = if (registers[instruction.a]!! == value) 1 else 0
                }

                is Instruction.Mod -> {
                    val value = instruction.b.toIntOrNull() ?: registers[instruction.b]!!
                    registers[instruction.a] = registers[instruction.a]!! % value
                }

                is Instruction.Mul -> {
                    val value = instruction.b.toIntOrNull() ?: registers[instruction.b]!!
                    registers[instruction.a] = registers[instruction.a]!! * value
                }
            }
        }

        return registers["z"]!! == 0
    }

    fun part1() = getModelNumber(true)

    fun part2() = getModelNumber(false)
}

fun day24() {
    val day = Day24(DataFile.Part1)
    report(2021, 24, day.part1(), day.part2())
}
