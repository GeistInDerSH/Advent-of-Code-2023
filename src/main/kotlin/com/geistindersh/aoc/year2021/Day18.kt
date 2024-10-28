package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import java.util.*

class Day18(dataFile: DataFile) {
	private val data = fileToStream(2021, 18, dataFile).map(::parseLine).toList()

	private fun parseLine(line: String): List<Any> {
		val stack = Stack<MutableList<Any>>().apply { push(mutableListOf()) }
		for (char in line) {
			when (char) {
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
					stack.peek().add(char.digitToInt())
				}

				'[' -> stack.push(mutableListOf())
				']' -> {
					val top = stack.pop()
					stack.peek().add(top)
				}

				else -> continue
			}
		}
		val listStart = stack.firstElement().first()
		if (listStart !is MutableList<*>) throw IllegalArgumentException("The stack produced an invalid object: $listStart")
		return listStart as List<Any>
	}

	init {
		data.forEach(::println)
	}

	fun part1() = 0
	fun part2() = 0
}

fun day18() {
	val day = Day18(DataFile.Part1)
	report(2021, 18, day.part1(), day.part2())
}
