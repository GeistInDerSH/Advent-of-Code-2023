package day10

import helper.fileToStream
import helper.report

class Grid(private val tiles: List<List<Tile>>) {
    private val start = run {
        for (row in tiles.indices) {
            for (col in tiles[row].indices) {
                if (tiles[row][col].symbol == 'S') {
                    return@run row to col
                }
            }
        }
        -1 to -1
    }
    private val loops: MutableList<List<Pair<Int, Int>>> = mutableListOf()

    fun getTileAt(pair: Pair<Int, Int>) = tiles[pair.first][pair.second]
    fun toFlatSet() = tiles.flatMap { tile -> tile.map { it.row to it.col } }.toSet()

    private fun getFirstUnvisitedOrNull(positions: List<Pair<Int, Int>>): Pair<Int, Int>? {
        for (pos in positions) {
            val (row, col) = pos
            if (row in tiles.indices && col in tiles[0].indices) {
                try {
                    val tile = tiles[row][col]
                    if (tile.hasBeenVisited() || tile.symbol == '.') {
                        continue
                    } else {
                        return pos
                    }
                } catch (_: Exception) {
                    continue
                }
            } else {
                continue
            }
        }
        return null
    }

    fun next(position: Pair<Int, Int>?): Pair<Int, Int>? {
        if (position == null) {
            return null
        }
        val (row, col) = position
        val tile = tiles[row][col]
        tile.visit()
        return when (tile.symbol) {
            '|' -> getFirstUnvisitedOrNull(listOf(tile.up, tile.down))
            '-' -> getFirstUnvisitedOrNull(listOf(tile.left, tile.right))
            'L' -> getFirstUnvisitedOrNull(listOf(tile.up, tile.right))
            'J' -> getFirstUnvisitedOrNull(listOf(tile.up, tile.left))
            '7' -> getFirstUnvisitedOrNull(listOf(tile.left, tile.down))
            'F' -> getFirstUnvisitedOrNull(listOf(tile.down, tile.right))
            'S' -> getFirstUnvisitedOrNull(listOf(tile.up, tile.right, tile.down, tile.left))
            else -> null
        }
    }

    private fun generateLoopFrom(position: Pair<Int, Int>): List<Pair<Int, Int>> {
        var pos = next(position)
        val loop: MutableList<Pair<Int, Int>> = mutableListOf()
        while (pos != null) {
            loop.add(pos)
            pos = next(pos)
        }
        loops.add(loop.toList() + start)
        return loop
    }

    private fun getLoops(): List<List<Pair<Int, Int>>> {
        if (loops.isNotEmpty()) {
            return loops.toList()
        }
        repeat(4) { generateLoopFrom(start) }
        return loops.toList()
    }

    fun mainLoop() = getLoops().first { it.isNotEmpty() }
}

data class Tile(val symbol: Char, val row: Int, val col: Int) {
    val left: Pair<Int, Int> by lazy { row to col - 1 }
    val right: Pair<Int, Int> by lazy { row to col + 1 }
    val up: Pair<Int, Int> by lazy { row - 1 to col }
    val down: Pair<Int, Int> by lazy { row + 1 to col }
    private var visited = false
    val range = 0..col

    fun hasBeenVisited() = visited
    fun visit() {
        visited = true
    }
}

fun parseInput(fileName: String): Grid {
    val tiles = fileToStream(fileName).mapIndexed { row, line ->
        line.mapIndexed { col, it -> Tile(it, row, col) }
    }.toList()
    return Grid(tiles)
}

fun part1(tiles: Grid) = tiles.mainLoop().size / 2

fun part2(tiles: Grid): Int {
    val loop = tiles.mainLoop().toSet()
    val crossings = "|7F".toSet()
    return (tiles.toFlatSet() - loop).parallelStream().map { tile ->
        loop.count { it.first == tile.first && it.second in 0..<tile.second && tiles.getTileAt(it).symbol in crossings }
    }.toList().count { it % 2 == 1 }
}

fun day10() {
    val input = parseInput("src/main/resources/day_10/part_1.txt")
    report(
        dayNumber = 10,
        part1 = part1(input),
        part2 = part2(input),
    )
}
