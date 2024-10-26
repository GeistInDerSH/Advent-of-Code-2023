package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.enums.Point
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import java.util.*

class Day15(dataFile: DataFile) {
	private val graph = fileToStream(2021, 15, dataFile)
		.flatMapIndexed { row, line ->
			line.mapIndexed { col, value -> Point(row, col) to value.digitToInt() }
		}
		.toMap()
	private val start = Point(0, 0)
	private val end = graph.let { g ->
		val rowMax = g.maxOf { it.key.row }
		val colMax = g.maxOf { it.key.col }
		Point(rowMax, colMax)
	}

	private data class Path(val current: Point, val risk: Int, val steps: Set<Point>)

	private fun getPathCost(): Int {
		val queue: PriorityQueue<Path> = PriorityQueue<Path>(compareBy { it.risk })
			.apply { add(Path(start, 0, setOf(start))) }
		val seen = mutableSetOf<Point>()

		while (queue.isNotEmpty()) {
			val head = queue.poll()
			if (head.current in seen) continue
			if (head.current == end) return head.risk
			seen.add(head.current)

			val next = head
				.current
				.neighbors()
				.filter { it in graph && it !in head.steps }
				.map { Path(it, head.risk + graph[it]!!, head.steps + listOf(it)) }
			queue.addAll(next)
		}
		return -1
	}

	fun part1() = getPathCost()
	fun part2() = 0
}

fun day15() {
	val day = Day15(DataFile.Part1)
	report(2021, 15, day.part1(), day.part2())
}
