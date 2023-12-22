package day22

import helper.files.DataFile
import helper.files.fileToStream
import helper.report

typealias Volume = Set<Triple<Int, Int, Int>>

data class Brick(val lx: Int, val ly: Int, val lz: Int, val rx: Int, val ry: Int, val rz: Int) {
    val maxZ = rz
    val minZ = lz
    val volume: Volume by lazy {
        (lx..rx).flatMap { x ->
            (ly..ry).flatMap { y ->
                (lz..rz).map { z -> Triple(x, y, z) }
            }
        }.toSet()
    }

    /**
     * @param brick The brick to check against
     * @return If [brick] and the current brick are touching
     */
    fun isBelow(brick: Brick) = maxZ == brick.minZ - 1

    /**
     * @return The current brick dropped one position
     */
    fun drop(): Brick = Brick(lx, ly, lz - 1, rx, ry, rz - 1)

    companion object {
        fun fromString(string: String): Brick {
            val (left, right) = string.split('~')
            val (lx, ly, lz) = left.split(',').map { it.toInt() }
            val (rx, ry, rz) = right.split(',').map { it.toInt() }
            return Brick(lx, ly, lz, rx, ry, rz)
        }
    }
}

data class Tower(val bricks: List<Brick>) {

    /**
     * Move all [bricks] down until they cannot be moved lower
     * @return The bricks moved as far down as they can go, and the volume of those bricks
     */
    private fun cascade(): Pair<List<Brick>, Volume> {
        var volume: Volume = setOf()
        val updatedBricks = bricks.sortedBy { it.minZ }.map { b ->
            var brick = b
            while (true) {
                val drop = brick.drop()
                if (drop.minZ > 0 && volume.intersect(drop.volume).isEmpty()) {
                    brick = drop
                } else {
                    break
                }
            }
            volume = volume.union(brick.volume)
            brick
        }

        return Pair(updatedBricks, volume)
    }

    /**
     * Check to see if the given [brick] can be removed from the tower of bricks
     *
     * @param brick The brick to check if it can be removed
     * @param bricks The list of remaining bricks
     * @param volume The volume of the tower of bricks
     * @return If the [brick] can be removed
     */
    private fun canRemove(brick: Brick, bricks: List<Brick>, volume: Volume): Boolean {
        val volumeWithoutBrick = volume - brick.volume
        return !bricks.any { other ->
            if (!brick.isBelow(other) || other.minZ == 1) {
                false
            } else {
                val drop = other.drop()
                val removedVol = volumeWithoutBrick - other.volume
                removedVol.intersect(drop.volume).isEmpty()
            }
        }
    }

    fun part1(): Int {
        val (bricks, volume) = cascade()
        return bricks.mapIndexed { index, brick -> canRemove(brick, bricks.drop(index + 1), volume) }.count { it }
    }

    companion object {

        fun parseInput(dataFile: DataFile): Tower {
            val bricks = fileToStream(22, dataFile).map { Brick.fromString(it) }.toList()
            return Tower(bricks)
        }
    }
}


fun day22() {
    val input = Tower.parseInput(DataFile.Part1)
    report(
        dayNumber = 22,
        part1 = input.part1(),
        part2 = "",
    )
}