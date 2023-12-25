package day25

import helper.files.DataFile
import helper.files.fileToStream
import helper.report
import java.util.*

typealias Edge = Pair<String, String>

/**
 * Get the [Edge] that has been traversed the most the number of times
 *
 * @param connections The current key-value pairs for the walkable edges
 * @return The most frequently traversed edge
 */
private fun getMostTraversedEdge(connections: Map<String, List<String>>): Edge {
    val edges = mutableMapOf<Edge, Int>()
    connections.keys.forEach {
        val visited = mutableMapOf<String, Boolean>()
        val queue = ArrayDeque<String>()
        queue.add(it)

        while (queue.isNotEmpty()) {
            val from = queue.removeFirst()
            for (to in connections[from]!!) {
                if (visited.getOrElse(to) { false }) {
                    continue
                }

                queue.add(to)
                visited[to] = true
                val edge = if (from < to) Edge(from, to) else Edge(to, from)
                edges[edge] = edges.getOrElse(edge) { 0 } + 1
            }
        }
    }

    return edges.maxBy { it.value }.key
}

/**
 * Count the number of connections starting from [start]
 *
 * @param start The start of the loop
 * @param connections The current connection map
 * @return The length of the loop from the [start]
 */
private fun countConnectionsFromStart(start: String, connections: Map<String, List<String>>): Int {
    val visited = mutableMapOf<String, Boolean>()
    val queue = ArrayDeque<String>()
    queue.add(start)

    while (queue.isNotEmpty()) {
        val from = queue.removeFirst()
        for (to in connections[from]!!) {
            if (visited.getOrElse(to) { false }) {
                continue
            }
            queue.add(to)
            visited[to] = true
        }
    }
    return visited.size
}

fun part1(connections: MutableMap<String, MutableList<String>>): Int {
    val m1 = (0..2).map { _ ->
        val edge = getMostTraversedEdge(connections)
        connections[edge.first]!!.remove(edge.second)
        connections[edge.second]!!.remove(edge.first)

        edge
    }.first()

    val firstLoopSize = countConnectionsFromStart(m1.first, connections)
    val secondLoopSize = countConnectionsFromStart(m1.second, connections)

    return firstLoopSize * secondLoopSize
}

fun parseInput(dataFile: DataFile): MutableMap<String, MutableList<String>> {
    val nodes = mutableMapOf<String, MutableList<String>>()

    fileToStream(25, dataFile).forEach {
        val key = it.substringBefore(':')
        val values = it.substringAfter(':').trim().split(' ')

        nodes.putIfAbsent(key, mutableListOf())

        values.forEach { value ->
            nodes.putIfAbsent(value, mutableListOf())
            nodes[key]!!.add(value)
            nodes[value]!!.add(key)
        }
    }

    return nodes
}

fun day25() {
    val input = parseInput(DataFile.Part1)

    report(
        dayNumber = 25,
        part1 = part1(input),
        part2 = "Push the button!",
    )
}