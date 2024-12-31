package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.enums.Point2D
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import kotlin.math.sqrt

class Day20(
    dataFile: DataFile,
) : AoC<Long, Int> {
    private val tileSet =
        fileToString(2020, 20, dataFile)
            .split("\n\n")
            .map { Tile.from(it) }
            .let { TileSet(it) }

    private data class TileSet(
        val tiles: List<Tile>,
    ) {
        fun edgesToCount() =
            tiles
                .flatMap { it.allSides() }
                .groupingBy { it }
                .eachCount()

        fun tilesWithUniqueEdges() =
            edgesToCount()
                .let { edges ->
                    tiles.filter { tile -> tile.allSides().count { edges[it]!! == 2 } == 4 }
                }
    }

    private data class Tile(
        val title: Long,
        val board: List<List<Char>>,
    ) {
        val top = board.first()
        val bottom = board.last()
        val left = board.indices.map { board[it].first() }
        val right = board.indices.map { board[it].last() }

        fun getMutableBoard() = board.map { it.toMutableList() }.toMutableList()

        fun sides() = listOf(top, bottom, left, right)

        fun allSides() = sides() + sides().map { it.reversed() }

        fun reflectLeftRight() = this.copy(board = board.map { it.reversed() })

        fun reflectTopBottom() = this.copy(board = board.reversed())

        fun rotateLeft(): Tile {
            val tmp = Array(board.size) { CharArray(board.size) }
            for (row in board.indices) {
                for (col in board[row].indices) {
                    tmp[row][board.size - col - 1] = board[col][row]
                }
            }
            val newBoard = tmp.map { it.toList() }.toList()
            return this.copy(board = newBoard)
        }

        fun allOrientations(): List<Tile> {
            val orientations = mutableListOf<Tile>()
            var tile = this
            for (i in 0..<4) {
                orientations.add(tile)
                orientations.add(tile.reflectLeftRight())
                orientations.add(tile.reflectTopBottom())
                tile = tile.rotateLeft()
            }
            return orientations
        }

        fun orient(edges: Map<List<Char>, Int>) = orient(emptyList(), emptyList(), edges)

        fun orient(
            left: List<Char>,
            top: List<Char>,
            edges: Map<List<Char>, Int>,
        ): Tile? {
            for (orientation in allOrientations()) {
                val leftMatches = (left.isEmpty() && edges[orientation.left] == 1) || left == orientation.left
                val topMatches = (top.isEmpty() && edges[orientation.top] == 1) || top == orientation.top
                if (leftMatches && topMatches) return orientation
            }
            return null
        }

        fun withoutBorder(): Tile {
            val border = Array(board.size - 2) { CharArray(board.size - 2) }
            for (row in board.indices) {
                if (row == 0 || row == board.size - 1) continue
                for (col in board[row].indices) {
                    if (col == 0 || col == board.size - 1) continue
                    border[row - 1][col - 1] = board[row][col]
                }
            }
            val board = border.map { it.toList() }.toList()
            return Tile(title, board)
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

    private data class Image(
        val chart: Map<Point2D, Tile>,
    ) {
        val grid: Tile by lazy {
            val width = sqrt(chart.size.toDouble()).toInt()
            val tileSize =
                chart.values
                    .first()
                    .withoutBorder()
                    .board.size
            val board = Array(tileSize * width) { CharArray(tileSize * width) }

            for ((point, tile) in chart) {
                val borderless = tile.withoutBorder()
                for (row in borderless.board.indices) {
                    val y = (tileSize * point.row) + row

                    for (col in borderless.board[row].indices) {
                        val x = (tileSize * point.col) + col
                        board[y][x] = borderless.board[row][col]
                    }
                }
            }

            Tile(0, board.map { it.toList() }.toList())
        }

        private fun getBoardWithSeaMonster(): Tile {
            for (tile in grid.allOrientations()) {
                var found = false
                val grid = tile.getMutableBoard()
                for (row in grid.indices) {
                    for (col in grid[row].indices) {
                        var hasMatch = true
                        for (point in SEA_MONSTER) {
                            val y = row + point.row
                            if (y >= grid.size) {
                                hasMatch = false
                                break
                            }
                            val x = col + point.col
                            if (x >= grid[y].size) {
                                hasMatch = false
                                break
                            }
                            if (grid[y][x] != '#') {
                                hasMatch = false
                                break
                            }
                        }
                        if (!hasMatch) {
                            continue
                        }

                        found = true
                        for (point in SEA_MONSTER) {
                            val y = row + point.row
                            val x = col + point.col
                            grid[y][x] = 'O'
                        }
                    }
                }
                if (found) {
                    return Tile(0, grid)
                }
            }
            throw IllegalStateException("No tile found for $grid")
        }

        fun roughness() = getBoardWithSeaMonster().board.sumOf { row -> row.count { it == '#' } }

        companion object {
            val SEA_MONSTER =
                "                  # \n#    ##    ##    ###\n #  #  #  #  #  #   "
                    .split("\n")
                    .flatMapIndexed { row, line ->
                        line.mapIndexedNotNull { col, char -> if (char == '#') Point2D(row, col) else null }
                    }

            fun from(tileSet: TileSet): Image {
                val edgeCount = tileSet.edgesToCount()
                val unusedTiles = tileSet.tiles.associateBy { it.title }.toMutableMap()
                var firstInRow =
                    tileSet // Find the first tile, and orient it
                        .tilesWithUniqueEdges()
                        .first()
                        .orient(edgeCount)
                unusedTiles.remove(firstInRow!!.title)

                val tileMap = mutableMapOf(Point2D(0, 0) to firstInRow)

                var point = Point2D(0, 1)
                var left = firstInRow.right
                var top = emptyList<Char>()

                while (unusedTiles.isNotEmpty()) {
                    var toRemove: Long? = null
                    for ((title, tile) in unusedTiles) {
                        val candidate = tile.orient(left, top, edgeCount) ?: continue
                        toRemove = title
                        tileMap[point] = candidate

                        point = point.copy(col = point.col + 1)
                        left = candidate.right
                        tileMap[point + Point2D(-1, 0)]?.let {
                            // Set the top value if there's a board above
                            top = it.bottom
                        }

                        if (firstInRow == null) {
                            firstInRow = candidate
                        }
                        break
                    }
                    if (toRemove == null) {
                        // Go to the next row, and back to the first column
                        point = Point2D(point.row + 1, 0)
                        left = emptyList() // nothing to the left of us (still stuck in the middle with you though...)
                        top = firstInRow!!.bottom // The grid above us provides to the top value we should look for
                        firstInRow = null
                    } else {
                        unusedTiles.remove(toRemove)
                    }
                }

                return Image(tileMap)
            }
        }
    }

    override fun part1() = tileSet.tilesWithUniqueEdges().map { it.title }.reduce(Long::times)

    override fun part2() = Image.from(tileSet).roughness()
}

fun day20() {
    val day = Day20(DataFile.Part1)
    report(2020, 20, day.part1(), day.part2())
}
