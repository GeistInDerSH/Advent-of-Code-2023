package day21

import helper.enums.Direction
import helper.files.DataFile
import helper.files.fileToStream
import helper.report

data class Obstacle(val row: Int, val col: Int, val char: Char)

data class Location(val row: Int, val col: Int) {
    operator fun plus(direction: Direction) = Location(row + direction.rowInc, col + direction.colInc)
}

data class Grid(private val start: Location, private val rocks: Set<Location>, private val lineLength: Int) {

    /**
     * Recursively loop through the steps, determining the locations that have been jumped to at each step.
     *
     * @param locations Visited locations
     * @param step The current step number
     * @param limit The maximum step count
     */
    private tailrec fun part1(locations: Set<Location>, step: Int, limit: Int): Int {
        return if (step < limit) {
            val newLocations = locations.flatMap { loc ->
                Direction.entries.map { loc + it }.filter { it !in rocks }
            }.toSet()

            part1(newLocations, step + 1, limit)
        } else {
            locations.size
        }
    }

    fun part1(maxSteps: Int) = part1(setOf(start), 0, maxSteps)

    /**
     * Recursively loop through the steps, determining the locations that have been jumped to at each step.
     * When the [step] mod [lineLength] and [limit] % [lineLength] are the same, add them to the [list]
     *
     * @param locations Visited locations
     * @param step The current step number
     * @param limit The maximum step count
     * @param list Values where the [step] mod [lineLength] and [limit] % [lineLength] are the same
     */
    private tailrec fun part2(locations: Set<Location>, step: Int, limit: Int, list: MutableList<Int>): List<Int> {
        if (step % lineLength == limit % lineLength) {
            list.add(locations.size)
        }

        return if (step < limit) {
            val newLocations = locations.flatMap { loc ->
                Direction.entries.map { loc + it }.filter { direction ->
                    // Get the actual row / column in the original field
                    val row = ((direction.row % lineLength) + lineLength) % lineLength
                    val col = ((direction.col % lineLength) + lineLength) % lineLength

                    Location(row, col) !in rocks
                }
            }.toSet()

            part2(newLocations, step + 1, limit, list)
        } else {
            list
        }
    }

    /**
     * @see <a href="https://github.com/Oupsman/AOC2023/blob/main/d21/advent21.go">Solution Help 1</a>
     * @see <a href="https://github.com/apprenticewiz/adventofcode/blob/main/2023/rust/day21b/src/main.rs">Solution Help 2</a>
     */
    fun part2(maxSteps: Int): Long {
        val limit = (maxSteps % lineLength) + (2 * lineLength)
        val (b0, b1, b2) = part2(setOf(start), 0, limit, mutableListOf())

        val a0 = -b0 + 2.0 * b1 - b2
        val x0 = (a0 / -2.0).toLong()

        val a1 = 3 * b0 - 4 * b1 + b2
        val x1 = (a1 / -2.0).toLong()

        val a2 = -2.0 * b0
        val x2 = (a2 / -2.0).toLong()

        val n = maxSteps / lineLength
        return (x0 * n * n) + (x1 * n) + x2
    }
}

fun parseInput(dataFile: DataFile): Grid {
    var lineLength = 0
    val parsed = fileToStream(21, dataFile).flatMapIndexed { row, line ->
        lineLength = line.length
        line.mapIndexed { col, c -> Obstacle(row, col, c) }
    }.toList()

    val start = parsed.first { it.char == 'S' }.let { Location(it.row, it.col) }
    val rocks = parsed.filter { it.char == '#' }.map { Location(it.row, it.col) }.toSet()
    return Grid(start, rocks, lineLength)
}

fun day21(skip: Boolean = true) {
    if (skip) {
        return
    }

    val input = parseInput(DataFile.Part1)
    report(
        dayNumber = 8,
        part1 = input.part1(64),
        part2 = input.part2(26501365),
    )
}
