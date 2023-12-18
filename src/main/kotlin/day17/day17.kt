package day17

import helper.DataFile
import helper.fileToStream
import helper.report
import java.util.*

/**
 * What plane the given vertex is on
 */
enum class Plane {
    Vertical,
    Horizontal,
    Start,
}

data class Vertex(val position: Pair<Int, Int>, var direction: Plane, val heatLoss: Int) :
    Comparable<Vertex> {
    var calculatedHeatLoss = 0
    var total = 1 shl 30 // Do not use Int.MAX_VALUE as it may overflow
    override fun compareTo(other: Vertex): Int {
        return total - other.total
    }
}

data class Graph(val nodes: List<List<Int>>) {
    private val vertices = run {
        val vertices = nodes.indices.flatMap { y ->
            nodes[0].indices.flatMap { x ->
                listOf(Plane.Vertical, Plane.Horizontal).map {
                    Vertex(Pair(x, y), it, nodes[y][x])
                }
            }
        }

        vertices[0].total = 0
        vertices[0].direction = Plane.Start

        vertices
    }

    private fun end() = vertices.last()

    /**
     * @param x The x position of the value
     * @param y The y position of the value
     * @param plane Which plane the value should be located on
     * @return A vertex, or null if the value is out of bounds
     */
    private fun getVertexAt(x: Int, y: Int, plane: Plane): Vertex? {
        return if (x < 0 || y < 0 || y >= nodes.size || x >= nodes[0].size) {
            null
        } else {
            vertices[y * 2 * nodes.size + x * 2 + plane.ordinal]
        }
    }

    /**
     * Get vertices along the horizontal axis
     *
     * @param v The starting vertex to get edges of
     * @param minDistance The minimum distance we can step
     * @param maxDistance The maximum distance in a line that can be traveled
     * @return Vertices along the axis of [v]
     */
    private fun horizontalEdges(v: Vertex, minDistance: Int, maxDistance: Int): List<Vertex> {
        var heatLoss = 0
        val (x, y) = v.position
        val up = (1..maxDistance).mapNotNull { distance ->
            val vertex = getVertexAt(x, y + distance, Plane.Vertical) ?: return@mapNotNull null
            heatLoss += vertex.heatLoss
            if (distance >= minDistance) {
                vertex.calculatedHeatLoss = heatLoss
                vertex
            } else {
                null
            }
        }

        heatLoss = 0
        val down = (1..maxDistance).mapNotNull { distance ->
            val vertex = getVertexAt(x, y - distance, Plane.Vertical) ?: return@mapNotNull null
            heatLoss += vertex.heatLoss
            if (distance >= minDistance) {
                vertex.calculatedHeatLoss = heatLoss
                vertex
            } else {
                null
            }
        }

        return up + down
    }

    /**
     * Get vertices along the vertical axis
     *
     * @param v The starting vertex to get edges of
     * @param minDistance The minimum distance we can step
     * @param maxDistance The maximum distance in a line that can be traveled
     * @return Vertices along the vertical axis of [v]
     */
    private fun verticalEdges(v: Vertex, minDistance: Int, maxDistance: Int): List<Vertex> {
        var heatLoss = 0
        val (x, y) = v.position
        val up = (1..maxDistance).mapNotNull { distance ->
            val vertex = getVertexAt(x + distance, y, Plane.Horizontal) ?: return@mapNotNull null
            heatLoss += vertex.heatLoss
            if (distance >= minDistance) {
                vertex.calculatedHeatLoss = heatLoss
                vertex
            } else {
                null
            }
        }

        heatLoss = 0
        val down = (1..maxDistance).mapNotNull { distance ->
            val vertex = getVertexAt(x - distance, y, Plane.Horizontal) ?: return@mapNotNull null
            heatLoss += vertex.heatLoss
            if (distance >= minDistance) {
                vertex.calculatedHeatLoss = heatLoss
                vertex
            } else {
                null
            }
        }

        return up + down
    }

    /**
     * Get the edges extending out from the given [Vertex] [v]
     *
     * @param v The starting vertex to get edges of
     * @param minDistance The minimum distance we can step
     * @param maxDistance The maximum distance in a line that can be traveled
     * @return Vertices along the axis of [v]
     */
    private fun edges(v: Vertex, minDistance: Int, maxDistance: Int): List<Vertex> {
        return when (v.direction) {
            Plane.Vertical -> verticalEdges(v, minDistance, maxDistance)
            Plane.Horizontal -> horizontalEdges(v, minDistance, maxDistance)
            Plane.Start -> {
                horizontalEdges(v, minDistance, maxDistance) + verticalEdges(v, minDistance, maxDistance)
            }
        }
    }

    /**
     * @param minDistance The minimum distance we can step
     * @param maxDistance The maximum distance in a line that can be traveled
     * @return The total heat loss along the shortest path
     */
    fun solution(minDistance: Int, maxDistance: Int): Int {
        // make a priority queue, sorting by the vertex total heat loss with the lowest being the first
        val queue = PriorityQueue(vertices)
        var current: Vertex
        val endPosition = end().position

        // Walk the queue, removing the head and determine any viable paths along the edges from the current node,
        // then we will continue until we've reached the ending node
        while (true) {
            current = queue.remove()
            if (current.position == endPosition) {
                break
            }

            for (edge in edges(current, minDistance, maxDistance)) {
                // Ensure we are loosing as little heat as possible, by only adding the edge back to the queue if
                // the amount of heat we would lose from the current position is less than what would be at the edge
                if (current.total + edge.calculatedHeatLoss < edge.total) {
                    edge.total = current.total + edge.calculatedHeatLoss
                    queue.add(edge)
                }
            }
        }

        return current.total
    }

    companion object {
        fun parseInput(fileType: DataFile): Graph {
            return Graph(fileToStream(17, fileType).map { line -> line.map { it.digitToInt() } }.toList())
        }

        fun part1(fileType: DataFile) = parseInput(fileType).solution(1, 3)
        fun part2(fileType: DataFile) = parseInput(fileType).solution(4, 10)
    }
}

fun day17() {
    report(
        dayNumber = 17,
        part1 = Graph.part1(DataFile.Part1),
        part2 = Graph.part2(DataFile.Part1),
    )
}
