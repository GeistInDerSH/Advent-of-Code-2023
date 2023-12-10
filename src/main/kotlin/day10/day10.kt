package day10

import helper.fileToStream
import helper.report
import kotlin.math.roundToInt

class Grid(private val tiles: List<List<Tile>>) {
    val start = run {
        for (row in tiles.indices) {
            for (col in tiles[row].indices) {
                if (tiles[row][col].symbol == 'S') {
                    return@run row to col
                }
            }
        }
        -1 to -1
    }

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

    fun generateLoopFrom(position: Pair<Int, Int>): List<Pair<Int, Int>> {
        var pos = next(position)
        val loop: MutableList<Pair<Int, Int>> = mutableListOf()
        while (pos != null) {
            loop.add(pos)
            pos = next(pos)
        }
        return loop
    }
}

data class Tile(val symbol: Char, val row: Int, val col: Int) {
    val left: Pair<Int, Int> by lazy { row to col - 1 }
    val right: Pair<Int, Int> by lazy { row to col + 1 }
    val up: Pair<Int, Int> by lazy { row - 1 to col }
    val down: Pair<Int, Int> by lazy { row + 1 to col }
    private var visited = false

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

fun part1(tiles: Grid): Int {
    return (0..<4).maxOf {
        val loop = tiles.generateLoopFrom(tiles.start)
        (loop.size.toFloat() / 2f).roundToInt()
    }
}

fun day10() {
    val input = parseInput("src/main/resources/day_10/part_1.txt")
    report(
        dayNumber = 10,
        part1 = part1(input),
        part2 = "",
    )
}
