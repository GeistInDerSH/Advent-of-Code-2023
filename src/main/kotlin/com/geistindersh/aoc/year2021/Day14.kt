package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import kotlin.math.ceil

class Day14(dataFile: DataFile) {
	private val template = fileToStream(2021, 14, dataFile).first()
	private val mapping = fileToStream(2021, 14, dataFile)
		.drop(2)
		.map { line ->
			val (key, value) = line.split(" -> ")
			key to value
		}
		.toMap()

	private data class Polymer(
		private val pairCount: Map<String, Long>,
		private val pairMap: Map<String, Pair<String, String>>,
	) {
		fun next(): Polymer {
			// The possible pairs is already known, and won't grow. However, the number of those
			// pairs will. Because of that, loop over the existing pair counts and get the two next
			// pairs that would be generated, and increment the pair count by the parent pair count
			val newPairCount = pairMap.keys.associateWith { 0L }.toMutableMap()
			for ((pair, count) in pairCount) {
				val (left, right) = pairMap[pair]!!
				newPairCount[left] = newPairCount[left]!! + count
				newPairCount[right] = newPairCount[right]!! + count
			}

			return this.copy(pairCount = newPairCount)
		}

		fun getLetterCounts(): Map<Char, Long> {
			val letterCount = mutableMapOf<Char, Long>()
			for ((pair, count) in pairCount) {
				for (char in pair) {
					letterCount[char] = letterCount.getOrDefault(char, 0L) + count
				}
			}

			// Account for there being two of each char because of the pairs
			for ((char, count) in letterCount) {
				letterCount[char] = ceil(count.toDouble() / 2).toLong()
			}
			return letterCount
		}

		companion object {
			fun from(template: String, mapping: Map<String, String>): Polymer {
				val pairMap = mapping
					.entries
					.associate { (key, value) ->
						val left = key.first() + value
						val right = value + key.last()
						key to Pair(left, right)
					}
				val pairCount = pairMap.keys.associateWith { 0L }.toMutableMap()
				for (pair in template.windowed(2)) {
					val (left, right) = pairMap[pair]!!
					pairCount[left] = pairCount[left]!! + 1L
					pairCount[right] = pairCount[right]!! + 1L
				}

				return Polymer(pairCount, pairMap)
			}
		}
	}

	private fun generatePolymer(steps: Int) = generateSequence(Polymer.from(template, mapping)) { it.next() }
		.drop(steps - 1) // account for the first polymer already being generated
		.first()
		.let { map ->
			val letterCount = map.getLetterCounts()
			val min = letterCount.values.minOrNull()!!
			val max = letterCount.values.maxOrNull()!!
			max - min
		}

	fun part1() = generatePolymer(10)
	fun part2() = generatePolymer(40)
}

fun day14() {
	val day = Day14(DataFile.Part1)
	report(2021, 14, day.part1(), day.part2())
}
