package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day8(dataFile: DataFile) {
	private val signals = fileToStream(2021, 8, dataFile)
		.map { line ->
			val (signal, out) = line.split('|').map { it.trim().split(" ") }
			Noise(signal, out)
		}
		.toList()

	private data class Noise(val signal: List<String>, val digitalOut: List<String>) {
		val denoise = signal.let { signal ->
			val known = signal
				.mapNotNull { sig ->
					when (sig.length) {
						2 -> sig to 1
						3 -> sig to 7
						4 -> sig to 4
						7 -> sig to 8
						else -> null
					}
				}
				.toMap()
				.toMutableMap()
			val knownRev = known.entries.associate { (k, v) -> v to k.toSet() }.toMutableMap()

			// We can solve the unknowns for length 6 if we remove known segments
			// and check the length of the segments. Use set difference to make it easier
			for (it in signal.filter { it.length == 6 }) {
				val set = it.toSet()
				val digit = when {
					(set - knownRev[4]!!).size == 2 -> 9 // top and bottom
					(set - knownRev[1]!!).size == 4 -> 0 // top, bottom, and all left
					else -> 6 // All that can be left
				}
				known[it] = digit
				knownRev[digit] = it.toSet()
			}
			for (it in signal.filter { it.length == 5 }) {
				val set = it.toSet()
				val digit = when {
					(set - knownRev[1]!!).size == 3 -> 3 // top, middle, bottom
					(set - (knownRev[4]!! + knownRev[7]!!).toSet()).size == 1 -> 5 // lower left
					else -> 2 // All that can be left
				}
				known[it] = digit
				knownRev[digit] = it.toSet()
			}

			known.entries.associate { (k, v) -> k.toSet() to v }
		}
	}

	fun part1() = signals
		.flatMap { signal -> signal.digitalOut }
		.count {
			when (it.length) {
				2 -> true // Signal 1
				3 -> true // Signal 7
				4 -> true // Signal 4
				7 -> true // Signal 8
				else -> false
			}
		}

	fun part2() = signals
		.map { signal ->
			val v = signal
				.digitalOut
				.map { signal.denoise[it.toSet()]!! }
				.fold(0) { acc, i -> acc * 10 + i }
			v
		}
		.reduce(Int::plus)
}

fun day8() {
	val day = Day8(DataFile.Part1)
	report(2021, 8, day.part1(), day.part2())
}
