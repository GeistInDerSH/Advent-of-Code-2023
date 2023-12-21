package day21

import helper.enums.Direction
import helper.files.DataFile
import helper.files.fileToStream
import helper.report

data class Rock(val row: Int, val col: Int, val char: Char)

data class Jump(val row: Int, val col: Int) {
    operator fun plus(direction: Direction): Jump {
        return Jump(row + direction.rowInc, col + direction.colInc)
    }
}

data class Grid(private val start: Jump, private val rocks: Set<Rock>, private val lineLength: Int) {
    private val jumpRocks = rocks.map { Jump(it.row, it.col) }.toSet()

    private tailrec fun part1(locations: Set<Jump>, step: Int, limit: Int): Int {
        return if (step < limit) {
            val newLocations = locations.flatMap { loc ->
                Direction.entries.map { loc + it }.filter { it !in jumpRocks }
            }.toSet()

            part1(newLocations, step + 1, limit)
        } else {
            locations.size
        }
    }

    fun part1(maxSteps: Int): Int {
        return part1(setOf(start), 0, maxSteps)
    }

    private tailrec fun part2(locations: Set<Jump>, step: Int, limit: Int, map: MutableMap<Int, Int>): Map<Int, Int> {
        if (step % lineLength == limit % lineLength) {
            map[step] = locations.size
        }

        return if (step < limit) {
            val newLocations = locations.flatMap { loc ->
                Direction.entries.map { loc + it }.filter { direction ->
                    // Get the actual row / column in the original field
                    val row = ((direction.row % lineLength) + lineLength) % lineLength
                    val col = ((direction.col % lineLength) + lineLength) % lineLength

                    Jump(row, col) !in jumpRocks
                }
            }.toSet()

            part2(newLocations, step + 1, limit, map)
        } else {
            map
        }
    }

    fun part2(maxSteps: Int): Long {
        val m = maxSteps % lineLength

        val results = part2(setOf(start), 0, m + 2 * lineLength, mutableMapOf())
        println(results)

        val b0 = results[m]!!
        val b1 = results[m + lineLength]!!
        val b2 = results[m + 2 * lineLength]!!

        val n = maxSteps / lineLength

        val deltaA0 = -b0 + 2.0 * b1 - b2
        val deltaA1 = 3 * b0 - 4 * b1 + b2
        val deltaA2 = -2.0 * b0

        val x0 = (deltaA0 / -2.0).toLong()
        val x1 = (deltaA1 / -2.0).toLong()
        val x2 = (deltaA2 / -2.0).toLong()
        return x0 * n * n + x1 * n + x2
    }
}

fun parseInput(dataFile: DataFile): Grid {
    var lineLength = 0
    val parsed = fileToStream(21, dataFile).flatMapIndexed { row, line ->
        lineLength = line.length
        line.mapIndexed { col, c ->
            Rock(row, col, c)
        }
    }.toList()

    val start = parsed.first { it.char == 'S' }.let { Jump(it.row, it.col) }
    val rocks = parsed.filter { it.char == '#' }.toSet()
    return Grid(start, rocks, lineLength)
}

fun day21() {
    val input = parseInput(DataFile.Part1)
    report(
        dayNumber = 8,
        part1 = input.part1(64),
        part2 = input.part2(26501365),
    )
}
