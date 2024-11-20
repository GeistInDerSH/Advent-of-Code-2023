package com.geistindersh.aoc.helper.algorithms

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GraphTest {
    private val directedGraph =
        graphBuilder {
            isDirected = true
            add("a", "b", 10)
            add("a", "c", 5)
            add("c", "d", 1)
            add("b", "d", 100)
        }
    private val undirectedGraph =
        graphBuilder {
            isDirected = false
            add("a", "b", 10)
            add("a", "c", 5)
            add("c", "d", 1)
            add("b", "d", 100)
        }

    @Test
    fun neighbours() {
        assertEquals(listOf("b", "c"), directedGraph.neighbours("a"))
        assertEquals(listOf("d"), directedGraph.neighbours("c"))
        assertEquals(listOf("d"), directedGraph.neighbours("b"))

        assertEquals(listOf("b", "c"), undirectedGraph.neighbours("a"))
        assertEquals(listOf("a", "d"), undirectedGraph.neighbours("c"))
        assertEquals(listOf("a", "d"), undirectedGraph.neighbours("b"))
    }

    @Test
    fun dfs() {
        assertEquals(10 + 100, directedGraph.dfs("a", "d"))
    }

    @Test
    fun dfsOrNull() {
        assertEquals(10 + 100, directedGraph.dfsOrNull("a", "d"))
    }

    @Test
    fun bfs() {
        assertEquals(10 + 100, directedGraph.bfs("a", "d"))
    }

    @Test
    fun bfsOrNull() {
        assertEquals(10 + 100, directedGraph.bfsOrNull("a", "d"))
    }

    @Test
    fun dijkstras() {
        assertEquals(6, directedGraph.dijkstras("a", "d"))
    }

    @Test
    fun shortestPathOrNull() {
        assertEquals(6, directedGraph.dijkstras("a", "d"))
    }

    @Test
    fun shortestPath() {
        assertEquals(6, directedGraph.dijkstras("a", "d"))
    }

    @Test
    fun travelingSalesman() {
        assertEquals(16, directedGraph.travelingSalesman())
    }

    @Test
    fun testTravelingSalesman() {
        assertEquals(115, directedGraph.travelingSalesman { a, b -> a.coerceAtLeast(b) })
    }
}
