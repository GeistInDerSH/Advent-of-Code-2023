package day14

import helper.DataFile
import helper.fileToStream
import helper.report

enum class Direction(val rowInc: Int, val colInc: Int) {
    NORTH(1, 0),
    EAST(0, 1),
    SOUTH(-1, 0),
    WEST(0, -1),
}

data class LavaRock(var row: Int, var col: Int, val char: Char) : Cloneable {
    public override fun clone() = LavaRock(row, col, char)
}

data class Grid(val rocks: List<LavaRock>) {
    private val colMax = rocks.maxOf { it.col }
    private val rowMax = rocks.maxOf { it.row }
    private val rockCoordinates = rocks.map { it.row to it.col }.toMutableSet()

    private fun tilt(rocks: List<LavaRock>, direction: Direction): List<LavaRock> {
        val newRocks = when (direction) {
            Direction.NORTH -> rocks.sortedBy { it.row }.reversed().toMutableList()
            Direction.SOUTH -> rocks.sortedBy { it.row }.toMutableList()
            Direction.EAST -> rocks.sortedBy { it.col }.reversed().toMutableList()
            Direction.WEST -> rocks.sortedBy { it.col }.toMutableList()
        }

        for (rock in newRocks) {
            if (rock.char == '#') {
                continue
            }

            while (true) {
                val rowUpdate = rock.row + direction.rowInc
                val colUpdate = rock.col + direction.colInc
                val updatedCoord = rowUpdate to colUpdate
                if (rowUpdate !in 1..rowMax || colUpdate !in 1..colMax) {
                    break
                } else if (updatedCoord in rockCoordinates) {
                    break
                }
                rockCoordinates.remove(rock.row to rock.col)
                rockCoordinates.add(updatedCoord)
                rock.row = rowUpdate
                rock.col = colUpdate
            }
        }

        return newRocks
    }

    fun tilt(direction: Direction) = Grid(tilt(rocks.map { it.clone() }, direction))

    fun rotate(count: Int = 1): Grid {
        var newRocks = rocks.map { it.clone() }

        repeat(count) {
            for (direction in listOf(Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.EAST)) {
                newRocks = tilt(newRocks, direction)
            }
        }
        return Grid(newRocks)
    }

    fun resetPositionMap() {
        rockCoordinates.clear()
        rocks.forEach { rockCoordinates.add(Pair(it.row, it.col)) }
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            null -> false
            !is Grid -> false
            else -> rocks.all { rock ->
                other.rocks.any { it.col == rock.col && it.row == rock.row && it.char == rock.char }
            }
        }
    }

    override fun hashCode(): Int {
        var result = rocks.hashCode()
        result = 31 * result + colMax
        result = 31 * result + rowMax
        return result
    }
}

fun parseInput(fileType: DataFile): Grid {
    val rocks = fileToStream(14, fileType).toList().reversed().flatMapIndexed { row, string ->
        string.mapIndexed { col, c ->
            if (c != '.') {
                LavaRock(row + 1, col + 1, c)
            } else {
                null
            }
        }.filterNotNull()
    }.reversed()
    return Grid(rocks)
}

fun part1(lavaRocks: Grid) = lavaRocks.tilt(Direction.NORTH).rocks.filter { it.char == 'O' }.sumOf { it.row }
fun part2(lavaRocks: Grid): Int {
    val maxCycles = 1_000_000_000
    var updatedRocks = lavaRocks
    updatedRocks.resetPositionMap()

    val (loopStart, loopSize) = run {
        val previous = mutableMapOf(updatedRocks to 0)

        var start = 0
        for (j in 0..<maxCycles) {
            updatedRocks = updatedRocks.rotate()
            if (updatedRocks in previous) {
                break
            }
            start += 1
            previous[updatedRocks] = start
        }

        val size = previous[updatedRocks]!!
        updatedRocks = updatedRocks.rotate()
        start += 1

        start to size
    }

    val remainder = (maxCycles - loopStart - 1) % (loopStart - loopSize)
    updatedRocks = updatedRocks.rotate(remainder)

    return updatedRocks.rocks.filter { it.char == 'O' }.sumOf { it.row }
}

fun day14() {
    val input = parseInput(DataFile.Part1)

    report(
        dayNumber = 14,
        part1 = part1(input),
        part2 = part2(input)
    )
}