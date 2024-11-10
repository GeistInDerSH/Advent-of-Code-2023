package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day20(dataFile: DataFile) {
    private val tileSet = fileToString(2020, 20, dataFile)
        .split("\n\n")
        .map { Tile.from(it) }
        .let { TileSet(it) }

    private data class TileSet(val tiles: List<Tile>) {
        private fun edgesToCount() = tiles
            .flatMap { it.allSides() }
            .groupingBy { it }
            .eachCount()

        fun tilesWithUniqueEdges() = edgesToCount()
            .let { edges ->
                tiles.filter { tile -> tile.allSides().count { edges[it]!! == 2 } == 4 }
            }
    }

    private data class Tile(val title: Long, val board: List<List<Char>>) {
        val top = board.first()
        val bottom = board.last()
        val left = board.indices.map { board[it].first() }
        val right = board.indices.map { board[it].last() }

        fun sides() = listOf(top, bottom, left, right)
        fun allSides() = sides() + sides().map { it.reversed() }

        fun reflect() = this.copy(board = board.map { it.reversed() })
        fun rotateLeft(): Tile {
            val tmp = Array(board.size) { CharArray(board.size) }
            for (row in board.indices) {
                for (col in board[row].indices) {
                    tmp[col][row] = board[row][col]
                }
            }
            val newBoard = tmp.map { it.toList() }.toList()
            return this.copy(board = newBoard)
        }


        companion object {
            val NUMBER_REGEX = "[0-9]+".toRegex()

            fun from(line: String): Tile {
                val lines = line.split("\n")
                val title = NUMBER_REGEX.find(lines[0])!!.value.toLong()
                val board = lines.drop(1).map { it.toList() }
                return Tile(title, board)
            }
        }
    }

    fun part1() = tileSet.tilesWithUniqueEdges().map { it.title }.reduce(Long::times)
    fun part2() = 0
}

fun day20() {
    val day = Day20(DataFile.Part1)
    report(2020, 20, day.part1(), day.part2())
}
