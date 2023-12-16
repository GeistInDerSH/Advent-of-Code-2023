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

    companion object {
        fun parseInput(fileType: DataFile): MirrorGrid {
            val grid = fileToStream(16, fileType).map { it.toList() }.toList()
            return MirrorGrid(grid)
        }
    }

    private fun solution(row: Int, col: Int, direction: Direction): Int {
        val visited = mutableSetOf<Energized>()
        val eastWest = setOf(Direction.East, Direction.West)
        val northSouth = setOf(Direction.North, Direction.South)

        fun solutionInternal(row: Int, col: Int, direction: Direction) {
            var currentRow = row
            var currentCol = col
            var direct = direction

            val energized = Energized(row, col, direction)
            if (!visited.add(energized)) {
                return
            }

            while (currentRow + direct.rowInc in 0..<maxRow && currentCol + direct.colInc in 0..<maxCol) {
                currentRow += direct.rowInc
                currentCol += direct.colInc
                visited.add(Energized(currentRow, currentCol, direct))
                val char = grid[currentRow][currentCol]

                when {
                    char == '|' && direct in eastWest -> {
                        solutionInternal(currentRow, currentCol, Direction.North)
                        solutionInternal(currentRow, currentCol, Direction.South)
                        break
                    }

                    char == '-' && direct in northSouth -> {
                        solutionInternal(currentRow, currentCol, Direction.East)
                        solutionInternal(currentRow, currentCol, Direction.West)
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
        solutionInternal(row, col, direction)
        return visited.map { it.row to it.col }.toSet().count()
    }

    fun part1() = solution(0, 0, Direction.East)

    fun part2(): Int {
        val toRun = grid.indices.map { Energized(it, 0, Direction.East) } +
                grid.indices.map { Energized(it, grid[0].size, Direction.West) } +
                grid[0].indices.map { Energized(0, it, Direction.South) } +
                grid[0].indices.map { Energized(grid.size, it, Direction.North) }

        return toRun.parallelStream().map { solution(it.row, it.col, it.direction) }
            .reduce(0) { max, energized -> max.coerceAtLeast(energized) }
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