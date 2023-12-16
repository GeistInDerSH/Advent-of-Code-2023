package day16

import helper.DataFile
import helper.fileToStream
import helper.report

enum class Direction(val rowInc: Int, val colInc: Int) {
    North(-1, 0),
    South(1, 0),
    East(0, 1),
    West(0, -1),
}

data class Energized(val row: Int, val col: Int, val direction: Direction)

data class MirrorGrid(val grid: List<List<Char>>) {
    private val maxRow = grid.size
    private val maxCol = grid[0].size
    private val energized = mutableSetOf<Energized>()

    companion object {
        fun parseInput(fileType: DataFile): MirrorGrid {
            val grid = fileToStream(16, fileType).map { it.toList() }.toList()
            return MirrorGrid(grid)
        }
    }

    private fun solution(row: Int, col: Int, direction: Direction) {
        var currentRow = row
        var currentCol = col
        var direct = direction

        energized.add(Energized(row, col, direction))

        while (currentRow + direct.rowInc in 0..<maxRow && currentCol + direct.colInc in 0..<maxCol) {
            currentRow += direct.rowInc
            currentCol += direct.colInc
            val char = grid[currentRow][currentCol]
            energized.add(Energized(currentRow, currentCol, direct))

            when {
                char == '|' && direct in setOf(Direction.East, Direction.West) -> {
                    if (Energized(currentRow, currentCol, Direction.North) !in energized) {
                        solution(currentRow, currentCol, Direction.North)
                    }
                    if (Energized(currentRow, currentCol, Direction.South) !in energized) {
                        solution(currentRow, currentCol, Direction.South)
                    }
                    break
                }

                char == '-' && direct in setOf(Direction.North, Direction.South) -> {
                    if (Energized(currentRow, currentCol, Direction.East) !in energized) {
                        solution(currentRow, currentCol, Direction.East)
                    }
                    if (Energized(currentRow, currentCol, Direction.West) !in energized) {
                        solution(currentRow, currentCol, Direction.West)
                    }
                    break
                }

                char == '\\' -> {
                    direct = when (direct) {
                        Direction.East -> Direction.South
                        Direction.South -> Direction.East
                        Direction.West -> Direction.North
                        Direction.North -> Direction.West
                    }
                }

                char == '/' -> {
                    direct = when (direct) {
                        Direction.East -> Direction.North
                        Direction.North -> Direction.East
                        Direction.West -> Direction.South
                        Direction.South -> Direction.West
                    }
                }

                else -> continue
            }
        }
    }

    private fun solutionCountAndClear(row: Int, col: Int, direction: Direction): Int {
        solution(row, col, direction)
        val count = energizedCount()
        energized.clear()
        return count
    }

    private fun energizedCount() = energized.map { it.row to it.col }.toSet().count()

    fun part1() = solutionCountAndClear(0, 0, Direction.East)

    fun part2(): Int {
        val rowMax = grid.indices.maxOf { row ->
            val eastCount = solutionCountAndClear(row, 0, Direction.East)
            val westCount = solutionCountAndClear(row, grid[0].size, Direction.West)
            eastCount.coerceAtLeast(westCount)
        }
        val colMax = grid[0].indices.maxOf { col ->
            val southCount = solutionCountAndClear(0, col, Direction.South)
            val northCount = solutionCountAndClear(grid.size, col, Direction.North)
            southCount.coerceAtLeast(northCount)
        }

        return rowMax.coerceAtLeast(colMax)
    }
}

fun day16() {
    val input = MirrorGrid.parseInput(DataFile.Part1)
    report(
        dayNumber = 16,
        part1 = input.part1(),
        part2 = input.part2(),
    )
}