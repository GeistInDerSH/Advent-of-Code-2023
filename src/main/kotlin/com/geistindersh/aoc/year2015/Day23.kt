package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day23(dataFile: DataFile) {
	private val instructions = fileToStream(2015, 23, dataFile)
		.map { it.replace(",", "") }
		.map { line ->
			val parts = line.split(' ')
			when (parts[0]) {
				"hlf" -> Instruction.Half(parts[1])
				"tpl" -> Instruction.Triple(parts[1])
				"inc" -> Instruction.Increment(parts[1])
				"jmp" -> Instruction.Jump(parts[1].toInt())
				"jie" -> Instruction.JumpIfEven(parts[1], parts[2].toInt())
				"jio" -> Instruction.JumpIfOne(parts[1], parts[2].toInt())
				else -> throw IllegalArgumentException("Unexpected line: $line")
			}
		}
		.toList()

	private sealed class Instruction {
		data class Half(val register: String) : Instruction()
		data class Triple(val register: String) : Instruction()
		data class Increment(val register: String) : Instruction()
		data class Jump(val offset: Int) : Instruction()
		data class JumpIfEven(val register: String, val offset: Int) : Instruction()
		data class JumpIfOne(val register: String, val offset: Int) : Instruction()
	}

	private fun List<Instruction>.run(registers: MutableMap<String, Int>): Map<String, Int> {
		var rip = 0

		while (rip < size) {
			when (val instr = this[rip]) {
				is Instruction.Increment -> registers[instr.register] = registers[instr.register]!! + 1
				is Instruction.Half -> registers[instr.register] = registers[instr.register]!! / 2
				is Instruction.Triple -> registers[instr.register] = registers[instr.register]!! * 3
				is Instruction.Jump -> rip += instr.offset - 1
				is Instruction.JumpIfEven -> if (registers[instr.register]!! % 2 == 0) rip += instr.offset - 1
				is Instruction.JumpIfOne -> if (registers[instr.register]!! == 1) rip += instr.offset - 1
			}

			rip += 1
		}

		return registers
	}

	fun part1(register: String) = instructions.run(mutableMapOf("a" to 0, "b" to 0))[register]
	fun part2(register: String) = instructions.run(mutableMapOf("a" to 1, "b" to 0))[register]
}

fun day23() {
	val day = Day23(DataFile.Part1)
	report(2015, 23, day.part1("b"), day.part2("b"))
}
