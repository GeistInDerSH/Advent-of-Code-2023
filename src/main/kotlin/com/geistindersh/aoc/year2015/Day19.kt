package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day19(dataFile: DataFile) {
	private val molecule = fileToStream(2015, 19, dataFile).last()
	private val lookupTable = fileToStream(2015, 19, dataFile)
		.takeWhile { it.isNotBlank() }
		.map { it.substringBefore(" ") to it.substringAfterLast(" ") }
		.let {
			val table = mutableMapOf<String, MutableList<String>>()
			it.forEach { (k, v) ->
				val lst = table.getOrPut(k) { mutableListOf() }
				lst.add(v)
			}
			table.entries.associate { (k, v) -> k to v.toList() }
		}

	fun part1() = lookupTable
		.entries
		.flatMap { (k, v) ->
			val words = mutableListOf<String>()
			var idx = molecule.indexOf(k)
			while (idx >= 0) {
				val toAdd = v.map {
					StringBuilder().let { sb ->
						sb.append(molecule.subSequence(0, idx))
						sb.append(it)
						sb.append(molecule.subSequence(idx + k.length, molecule.length))
						sb.toString()
					}
				}
				words.addAll(toAdd)
				idx = molecule.indexOf(k, idx + 1)
			}
			words
		}
		.toSet()
		.count()

	fun part2() = 0
}

fun day19() {
	val day = Day19(DataFile.Part1)
	report(2015, 19, day.part1(), day.part2())
}
