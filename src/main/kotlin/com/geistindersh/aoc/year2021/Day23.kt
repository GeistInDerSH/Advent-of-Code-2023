package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import java.util.*
import kotlin.math.absoluteValue

class Day23(dataFile: DataFile) {
	private val diagram = fileToStream(2021, 23, dataFile)
		.drop(1)
		.map { it.drop(1).dropLast(1) }
		.toList()
		.dropLast(1)
		.let { lines ->
			val roomLines = lines.drop(1)
			val a = Room.from(Amphipods.Amber, roomLines)
			val b = Room.from(Amphipods.Bronze, roomLines)
			val c = Room.from(Amphipods.Copper, roomLines)
			val d = Room.from(Amphipods.Desert, roomLines)
			Diagram(lines[0].map { Amphipods.from(it) }, listOf(a, b, c, d).associateBy { it.roomFor })
		}

	private enum class Amphipods(val cost: Int, val index: Int) {
		Amber(1, 2),
		Bronze(10, 4),
		Copper(100, 6),
		Desert(1000, 8),
		Empty(-1, -1);

		companion object {
			fun from(char: Char) = when (char) {
				'A' -> Amber
				'B' -> Bronze
				'C' -> Copper
				'D' -> Desert
				else -> Empty
			}
		}
	}

	private data class Room(val roomFor: Amphipods, val contents: List<Amphipods>) {
		fun isFinished() = contents.all { it == roomFor }
		fun isEmptyOrAllCorrect() = contents.all { it == Amphipods.Empty || it == roomFor }
		val index = roomFor.index

		companion object {
			fun from(roomFor: Amphipods, lines: List<String>) =
				Room(roomFor, lines.map { Amphipods.from(it[roomFor.index]) })
		}
	}

	private data class Diagram(val hallway: List<Amphipods>, val rooms: Map<Amphipods, Room>) {
		fun isFinished() = rooms.all { it.value.isFinished() }

		fun next() = sequence {
			for ((hallwayIndex, hallwayValue) in moveableHallwayAmphipods()) {
				val room = rooms[hallwayValue]!!
				if (!hallwayIsClearBetween(hallwayIndex, room.index)) continue

				val moveToIndex = room.contents.lastIndexOf(Amphipods.Empty)
				val cost = ((hallwayIndex - room.index).absoluteValue + moveToIndex + 1) * hallwayValue.cost

				val newRooms = rooms.toMutableMap().let {
					val content = room.contents.toMutableList().apply { set(moveToIndex, hallwayValue) }
					it[room.roomFor] = room.copy(contents = content)
					it
				}
				val hall = hallway.toMutableList().apply { set(hallwayIndex, Amphipods.Empty) }
				yield(Diagram(hall, newRooms) to cost)
			}

			for (room in unsortedRooms()) {
				val toMove = room.contents.withIndex().first { it.value != Amphipods.Empty }
				for (hallwayIndex in listOf(0, 1, 3, 5, 7, 9, 10)) {
					if (hallway[hallwayIndex] != Amphipods.Empty) continue
					if (!hallwayIsClearBetween(hallwayIndex, room.index)) continue

					val cost = ((room.index - hallwayIndex).absoluteValue + toMove.index + 1) * toMove.value.cost
					val newRooms = rooms.toMutableMap().let {
						val content = room.contents.toMutableList().apply { set(toMove.index, Amphipods.Empty) }
						it[room.roomFor] = room.copy(contents = content)
						it
					}
					val hall = hallway.toMutableList().apply { set(hallwayIndex, toMove.value) }
					yield(Diagram(hall, newRooms) to cost)
				}
			}
		}

		private fun moveableHallwayAmphipods() = hallway
			.withIndex()
			.filter { it.value != Amphipods.Empty && rooms[it.value]!!.isEmptyOrAllCorrect() }

		private fun unsortedRooms() = rooms.values.filterNot { it.isEmptyOrAllCorrect() }

		private fun hallwayIsClearBetween(start: Int, end: Int): Boolean {
			val (actStart, actEnd) = if (start > end) end to start - 1 else start + 1 to end
			return (actStart..actEnd).all { hallway[it] == Amphipods.Empty }
		}
	}


	private fun organize(start: Diagram): Int {
		val queue = PriorityQueue<Pair<Diagram, Int>>(compareBy { it.second })
			.apply { add(start to 0) }
		val diagrams = mutableMapOf<Diagram, Int>().withDefault { Int.MAX_VALUE }
		val seen = mutableSetOf<Pair<Diagram, Int>>()

		while (queue.isNotEmpty()) {
			val (diagram, cost) = queue.poll().also { seen.add(it) }
			if (diagram.isFinished()) return cost

			for (next in diagram.next()) {
				if (next in seen) continue
				val (nextDiagram, nextCost) = next
				val newCost = nextCost + cost
				if (newCost > diagrams.getValue(nextDiagram)) continue

				diagrams[nextDiagram] = newCost
				queue.add(nextDiagram to newCost)
			}
		}

		throw Exception("No solution could be found")
	}

	fun part1() = organize(diagram)
	fun part2() = 0
}

fun day23() {
	val day = Day23(DataFile.Part1)
	report(2021, 23, day.part1(), day.part2())
}
