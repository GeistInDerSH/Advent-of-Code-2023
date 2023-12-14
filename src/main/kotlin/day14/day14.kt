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

    // Lookup table for current positions of LavaRocks for speed
    private val rockCoordinates = rocks.map { it.row to it.col }.toMutableSet()

    /**
     * Walk through each of the [LavaRock] in [rocks] and update their position, so they have been moved as far as
     * possible in the given [direction]
     *
     * @param rocks The list of [LavaRock] that will be updated
     * @param direction The direction to move the [rocks]
     * @return The updated [LavaRock] positions
     */
    private fun tilt(rocks: List<LavaRock>, direction: Direction): List<LavaRock> {
        // We want to make sure that the flat list of LavaRocks is sorted by the direction we are going
        // to be moving them. Otherwise, we may move some rocks to early and find they collide with a rock that should
        // have been moved
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

                // Stop when the new position would be outside the grid
                // or if there is already a rock at the position
                if (rowUpdate !in 1..rowMax || colUpdate !in 1..colMax) {
                    break
                } else if (updatedCoord in rockCoordinates) {
                    break
                }

                // Remove the existing rock from the memory, and add the updated one
                rockCoordinates.remove(rock.row to rock.col)
                rockCoordinates.add(updatedCoord)
                rock.row = rowUpdate
                rock.col = colUpdate
            }
        }

        return newRocks
    }

    /**
     * Rotate the Grid a set number of times, and return the updated grid
     *
     * @param count The number of times to rotate the [Grid]
     * @return A new Grid with the updated positions
     */
    private fun rotate(count: Int = 1): Grid {
        // Internally, we can keep this as a mutable value, so we aren't generating
        // a ton of new object for no reason
        var newRocks = rocks.map { it.clone() }

        // Having repeat when count == 1 is silly, but we can avoid extra garbage when count > 1 (e.g. when we find
        // the remaining number of times to rotate the grid
        repeat(count) {
            for (direction in listOf(Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.EAST)) {
                newRocks = tilt(newRocks, direction)
            }
        }
        return Grid(newRocks)
    }

    private fun resetPositionMap() {
        rockCoordinates.clear()
        rocks.forEach { rockCoordinates.add(Pair(it.row, it.col)) }
    }

    fun part1(): Int {
        val rocks = rocks.map { it.clone() }
        return tilt(rocks, Direction.NORTH).filter { it.char == 'O' }.sumOf { it.row }
    }

    fun part2(): Int {
        val maxCycles = 1_000_000_000
        var updatedRocks = this
        resetPositionMap()

        val previous = mutableMapOf(updatedRocks to 0)
        var loopStart = 0
        for (j in 0..<maxCycles) {
            updatedRocks = updatedRocks.rotate()
            if (updatedRocks in previous) {
                break
            }
            loopStart += 1
            previous[updatedRocks] = loopStart
        }

        val loopSize = previous[updatedRocks]!!
        updatedRocks = updatedRocks.rotate()
        loopStart += 1

        val mainLoopRemainder = maxCycles - loopStart - 1
        val period = loopStart - loopSize
        val remainder = mainLoopRemainder - period
        return updatedRocks.rotate(remainder).rocks.filter { it.char == 'O' }.sumOf { it.row }
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

fun day14() {
    val input = parseInput(DataFile.Part1)

    report(
        dayNumber = 14,
        part1 = input.part1(),
        part2 = input.part2()
    )
}