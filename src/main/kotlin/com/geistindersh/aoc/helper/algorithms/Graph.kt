package com.geistindersh.aoc.helper.algorithms

import java.util.PriorityQueue

class Graph<T : Comparable<T>>(
    nodes: Collection<Pair<Pair<T, T>, Int>>,
) {
    private val weights = nodes.associate { it.first to it.second }
    private val nodes = nodes.flatMap { it.first.toList() }.toSet()

    fun neighbours(node: T) = weights.keys.filter { it.first == node }.map { it.second }

    private fun getCost(
        start: T,
        end: T,
    ) = weights[start to end]!!

    private fun assertContains(node: T) {
        if (node !in nodes) {
            throw GraphExceptions.InvalidNodeException("Node $node not found")
        }
    }

    fun dfsOrNull(
        start: T,
        end: T,
    ) = try {
        dfs(start, end)
    } catch (_: GraphExceptions.UnreachableNodeException) {
        null
    } catch (_: GraphExceptions.InvalidNodeException) {
        null
    }

    fun dfs(
        start: T,
        end: T,
    ): Int {
        assertContains(start)
        assertContains(end)

        val queue = ArrayDeque<Pair<T, Int>>().apply { add(Pair(start, 0)) }
        val visited = mutableSetOf<T>()
        while (queue.isNotEmpty()) {
            val (name, cost) = queue.removeFirst()
            if (name == end) return cost
            if (name in visited) continue

            visited.add(name)
            queue.addAll(neighbours(name).map { it to cost + getCost(name, it) })
        }
        throw GraphExceptions.UnreachableNodeException("Cannot reach $end from $start")
    }

    fun bfsOrNull(
        start: T,
        end: T,
    ) = try {
        bfs(start, end)
    } catch (_: GraphExceptions.UnreachableNodeException) {
        null
    } catch (_: GraphExceptions.InvalidNodeException) {
        null
    }

    fun bfs(
        start: T,
        end: T,
    ): Int {
        assertContains(start)
        assertContains(end)

        val queue = ArrayDeque<Pair<T, Int>>().apply { add(start to 0) }
        val visited = mutableSetOf<T>()

        while (queue.isNotEmpty()) {
            val (name, cost) = queue.removeFirst()
            if (name == end) return cost
            val unvisited =
                neighbours(name)
                    .filter { it !in visited }
                    .map { it to cost + getCost(name, it) }
            queue.addAll(unvisited)
        }
        throw GraphExceptions.UnreachableNodeException("Cannot reach $end from $start")
    }

    fun dijkstras(
        start: T,
        end: T,
    ): Int {
        assertContains(start)
        assertContains(end)

        val distances = mutableMapOf(start to 0).withDefault { Int.MAX_VALUE }
        val queue =
            PriorityQueue<Pair<T, Int>>(compareBy { it.second })
                .apply { add(start to 0) }

        while (queue.isNotEmpty()) {
            val (node, cost) = queue.poll()
            neighbours(node).forEach { neighbor ->
                val neighborCost = getCost(node, neighbor) + cost
                if (neighborCost < distances.getValue(neighbor)) {
                    distances[neighbor] = neighborCost
                    queue.add(neighbor to neighborCost)
                }
            }
        }
        return distances[end]!!
    }

    @Suppress("unused")
    fun shortestPathOrNull(
        start: T,
        end: T,
    ) = try {
        shortestPath(start, end)
    } catch (_: GraphExceptions.UnreachableNodeException) {
        null
    } catch (_: GraphExceptions.InvalidNodeException) {
        null
    }

    fun shortestPath(
        start: T,
        end: T,
    ) = dijkstras(start, end)

    fun travelingSalesman() = travelingSalesman { a, b -> a.coerceAtMost(b) }

    fun travelingSalesman(fn: (Int, Int) -> Int): Int {
        val sortedNodes = nodes.sorted()

        fun adjacency(): Array<IntArray> {
            val arr = Array(sortedNodes.size) { IntArray(sortedNodes.size) }
            for (entry in weights.entries) {
                val i = sortedNodes.indexOf(entry.key.first)
                val j = sortedNodes.indexOf(entry.key.second)
                arr[i][j] = entry.value
                arr[j][i] = entry.value
            }
            return arr
        }

        val distances = IntArray(sortedNodes.size) { 0 }

        for (start in sortedNodes.indices) {
            val adjacencyMatrix = adjacency()
            var dest = start

            (0..<nodes.size - 1).forEach {
                val value =
                    adjacencyMatrix[dest]
                        .filter { it > 0 }
                        .reduce { acc, v -> fn(acc, v) }

                val pos = adjacencyMatrix[dest].indexOf(value)
                adjacencyMatrix[dest][pos] = 0

                for (j in sortedNodes.indices) {
                    adjacencyMatrix[j][dest] = 0
                }

                distances[start] += value
                dest = pos
            }
        }

        return distances.reduce(fn)
    }
}

fun <T : Comparable<T>> Collection<Pair<Pair<T, T>, Int>>.toGraph(): Graph<T> = Graph(this)

sealed class GraphExceptions(
    message: String,
) : Exception(message) {
    class UnreachableNodeException(
        message: String,
    ) : GraphExceptions(message)

    class InvalidNodeException(
        message: String,
    ) : GraphExceptions(message)
}

class GraphBuilder<T : Comparable<T>> {
    private val nodes = mutableSetOf<Pair<Pair<T, T>, Int>>()
    var isDirected = false

    fun add(
        start: T,
        end: T,
        weight: Int,
    ) {
        nodes.add(Pair(start, end) to weight)
        if (!isDirected) {
            nodes.add(Pair(end, start) to weight)
        }
    }

    fun add(
        start: T,
        end: T,
    ) = add(start, end, 0)

    fun build() = Graph(nodes)
}

fun <T : Comparable<T>> graphBuilder(init: GraphBuilder<T>.() -> Unit): Graph<T> {
    val builder = GraphBuilder<T>()
    builder.init()
    return builder.build()
}
