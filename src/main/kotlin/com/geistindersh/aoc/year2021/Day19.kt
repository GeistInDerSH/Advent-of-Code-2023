package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day19(dataFile: DataFile) {
	private val scanners = fileToString(2021, 19, dataFile)
		.split("\n\n")
		.map { line ->
			val beacons = line
				.split("\n")
				.drop(1)
				.map { it.split(",").map(String::toInt) }
				.map { Beacon.from(it) }
				.toSet()
			Scanner(beacons)
		}
		.toList()

	private data class Scanner(val beacons: Set<Beacon>) {
		fun transformationIntersection(other: Scanner): Scanner? {
			for (facing in 0..<6) {
				for (rotation in 0..<4) {
					val rotated = other.beacons.map { it.face(facing).rotate(rotation) }.toSet()
					for (b1 in beacons) {
						for (b2 in rotated) {
							val diff = b1 - b2
							val adjusted = rotated.map { it + diff }.toSet()
							if (adjusted.intersect(beacons).size < 12) {
								continue
							}
							return Scanner(adjusted)
						}
					}
				}
			}
			return null
		}
	}

	private data class Beacon(val x: Int, val y: Int, val z: Int) {
		operator fun plus(other: Beacon) = Beacon(x + other.x, y + other.y, z + other.z)
		operator fun minus(other: Beacon) = Beacon(x - other.x, y - other.y, z - other.z)

		fun face(dir: Int): Beacon {
			return when (dir) {
				0 -> this
				1 -> Beacon(x, -y, -z)
				2 -> Beacon(x, -z, y)
				3 -> Beacon(-y, -z, x)
				4 -> Beacon(y, -z, -x)
				5 -> Beacon(-x, -z, -y)
				else -> throw IllegalArgumentException("Unknown beacon: $dir")
			}
		}

		fun rotate(dir: Int): Beacon {
			return when (dir) {
				0 -> this
				1 -> Beacon(-y, x, z)
				2 -> Beacon(-x, -y, z)
				3 -> Beacon(y, -x, z)
				else -> throw IllegalArgumentException("Unknown direction: $dir")
			}
		}

		companion object {
			fun from(list: List<Int>) = Beacon(list[0], list[1], list[2])
		}
	}

	fun part1(): Int {
		var start = scanners.first()
		val unmappedScanners = ArrayDeque<Scanner>().apply { addAll(scanners.drop(1)) }
		while (unmappedScanners.isNotEmpty()) {
			val toMap = unmappedScanners.removeFirst()
			val scanner = start.transformationIntersection(toMap)
			if (scanner == null) {
				unmappedScanners.add(toMap)
			} else {
				start = Scanner(start.beacons + scanner.beacons)
			}
		}
		return start.beacons.size
	}

	fun part2() = 0
}

fun day19() {
	val day = Day19(DataFile.Part1)
	report(2021, 19, day.part1(), day.part2())
}
