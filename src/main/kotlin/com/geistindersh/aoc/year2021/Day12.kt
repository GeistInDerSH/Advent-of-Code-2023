package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day12(dataFile: DataFile) {
	private val graph = fileToStream(2021, 12, dataFile)
		.flatMap { line ->
			val (start, end) = line.split("-")
			when {
				// Ensure you can only traverse from start to a node, or to the end node,
				// but not from a node to start or from end to a node
				start == "start" -> listOf(Node(start, end))
				start == "end" -> listOf(Node(end, start))
				end == "end" -> listOf(Node(start, end))
				end == "start" -> listOf(Node(end, start))
				else -> listOf(Node(start, end), Node(end, start))
			}
		}
		.toSet()

	data class Node(val current: String, val next: String) {
		val nextIsLower = next == next.lowercase()
	}

	private data class GraphState(
		val node: Node,
		val connections: Set<Node>,
		val path: Set<String>,
		val isPart2: Boolean,
		val seenAnyTwice: Boolean = false,
	) {
		fun nextStates() = connections
			.filter { node.next == it.current }
			.mapNotNull { node ->
				val seenBefore = node.next in path
				when {
					!seenBefore -> {
						val newPath = if (node.nextIsLower) path + listOf(node.next) else path
						Triple(node, newPath, seenAnyTwice)
					}

					isPart2 && !seenAnyTwice -> Triple(node, path, true)
					else -> null
				}
			}
			.map { (node, newPath, anyTwice) -> this.copy(node = node, path = newPath, seenAnyTwice = anyTwice) }
	}

	private fun randomWalk(isPart2: Boolean): Int {
		val queue = ArrayDeque<GraphState>()
			.apply {
				addAll(graph
					.filter { it.current == "start" }
					.map {
						val path = if (it.nextIsLower) setOf("start", it.next) else setOf("start")
						GraphState(it, graph, path, isPart2)
					})
			}

		var count = 0
		while (queue.isNotEmpty()) {
			val state = queue.removeFirst()
			if (state.node.next == "end") {
				count += 1
				continue
			}
			queue.addAll(state.nextStates())
		}

		return count
	}

	fun part1() = randomWalk(false)
	fun part2() = randomWalk(true)
}

fun day12() {
	val day = Day12(DataFile.Part1)
	report(2021, 12, day.part1(), day.part2())
}
