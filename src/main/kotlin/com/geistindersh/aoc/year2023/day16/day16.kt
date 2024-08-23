package com.geistindersh.aoc.year2023.day16

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

data class Energized(val row: Int, val col: Int, val direction: Direction)

data class MirrorGrid(val grid: List<List<Char>>) {
    private val maxRow = grid.size
    private val maxCol = grid[0].size
    private val eastWest = setOf(Direction.East, Direction.West)
    private val northSouth = setOf(Direction.North, Direction.South)

    companion object {
        fun parseInput(fileType: DataFile): MirrorGrid {
            val grid = fileToStream(2023, 16, fileType).map { it.toList() }.toList()
            return MirrorGrid(grid)
        }
    }

    private fun solution(start: Energized): Int {
        val visited = mutableSetOf<Energized>()

        fun solutionInternal(start: Energized) {
            if (!visited.add(start)) {
                return
            }

            var (currentRow, currentCol, direct) = start

            while (currentRow + direct.rowInc in 0..<maxRow && currentCol + direct.colInc in 0..<maxCol) {
                currentRow += direct.rowInc
                currentCol += direct.colInc
                visited.add(Energized(currentRow, currentCol, direct))
                val char = grid[currentRow][currentCol]

                when {
                    char == '|' && direct in eastWest -> {
                        solutionInternal(Energized(currentRow, currentCol, Direction.North))
                        solutionInternal(Energized(currentRow, currentCol, Direction.South))
                        break
                    }

                    char == '-' && direct in northSouth -> {
                        solutionInternal(Energized(currentRow, currentCol, Direction.East))
                        solutionInternal(Energized(currentRow, currentCol, Direction.West))
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
        solutionInternal(start)

        return visited
            .map { it.row to it.col }
            .toSet()
            .count()
    }

    fun part1() = solution(Energized(0, 0, Direction.East))

    fun part2(): Int {
        val toRun = grid.indices.map { Energized(it, 0, Direction.East) } +
                grid.indices.map { Energized(it, grid[0].size, Direction.West) } +
                grid[0].indices.map { Energized(0, it, Direction.South) } +
                grid[0].indices.map { Energized(grid.size, it, Direction.North) }

        return toRun
            .parallelStream()
            .map { solution(it) }
            .reduce(0) { max, energized -> max.coerceAtLeast(energized) }
    }
}

fun day16() {
    val input = MirrorGrid.parseInput(DataFile.Part1)
    report(
        year = 2023,
        dayNumber = 16,
        part1 = input.part1(),
        part2 = input.part2(),
    )
}