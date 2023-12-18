package day17

import helper.DataFile
import helper.fileToStream
import helper.report
import java.util.*

enum class Plane {
    Vertical,
    Horizontal,
    Start,
}

data class Vertex(val position: Pair<Int, Int>, var direction: Plane, val heatLoss: Int) :
    Comparable<Vertex> {
    var calculatedHeatLoss = 0
    var total = 1 shl 30
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

    private fun getVertexAt(x: Int, y: Int, plane: Plane): Vertex? {
        return if (x < 0 || y < 0 || y >= nodes.size || x >= nodes[0].size) {
            null
        } else {
            vertices[y * 2 * nodes.size + x * 2 + plane.ordinal]
        }
    }

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

    private fun edges(v: Vertex, minDistance: Int, maxDistance: Int): List<Vertex> {
        return when (v.direction) {
            Plane.Vertical -> verticalEdges(v, minDistance, maxDistance)
            Plane.Horizontal -> horizontalEdges(v, minDistance, maxDistance)
            Plane.Start -> {
                horizontalEdges(v, minDistance, maxDistance) + verticalEdges(v, minDistance, maxDistance)
            }
        }
    }

    fun solution(minDistance: Int, maxDistance: Int): Int {
        val queue = PriorityQueue(vertices)
        var current: Vertex
        val endPosition = end().position
        while (true) {
            current = queue.remove()
            if (current.position == endPosition) {
                break
            }

            for (edge in edges(current, minDistance, maxDistance)) {
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
