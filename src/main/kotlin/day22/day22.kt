package day22

import helper.files.DataFile
import helper.files.fileToStream
import helper.report

typealias Volume = Set<Triple<Int, Int, Int>>

data class Brick(val lx: Int, val ly: Int, val lz: Int, val rx: Int, val ry: Int, val rz: Int) {
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
    fun isBelow(brick: Brick) = rz == brick.lz - 1

    /**
     * @return The current brick dropped one position
     */
    fun drop(): Brick = Brick(lx, ly, lz - 1, rx, ry, rz - 1)

    /**
     * @param other The brick to check against
     * @return If the current Brick is supporting the [other]
     */
    fun isSupporting(other: Brick): Boolean {
        return when {
            !other.isBelow(this) -> false
            (ly..ry).intersect(other.ly..other.ry).isNotEmpty() -> true
            (lx..rx).intersect(other.lx..other.rx).isNotEmpty() -> true
            else -> false
        }
    }

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
    private val cascade = cascade()
    private val droppedBricks = cascade.first
    private val volume = cascade.second

    /**
     * Move all [bricks] down until they cannot be moved lower
     * @return The bricks moved as far down as they can go, and the volume of those bricks
     */
    private fun cascade(): Pair<List<Brick>, Volume> {
        var volume: Volume = setOf()
        val updatedBricks = bricks.sortedBy { it.lz }.map { b ->
            var brick = b
            while (true) {
                val drop = brick.drop()
                if (drop.lz > 0 && volume.intersect(drop.volume).isEmpty()) {
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
     * @return If the [brick] can be removed
     */
    private fun canRemove(brick: Brick, bricks: List<Brick>): Boolean {
        val volumeWithoutBrick = volume - brick.volume
        return !bricks.any { other ->
            if (!brick.isBelow(other) || other.lz == 1) {
                false
            } else {
                val drop = other.drop()
                val removedVol = volumeWithoutBrick - other.volume
                removedVol.intersect(drop.volume).isEmpty()
            }
        }
    }

    fun part1(): Int {
        return droppedBricks.mapIndexed { index, brick -> canRemove(brick, droppedBricks.drop(index + 1)) }
            .count { it }
    }

    fun part2(): Int {
        return droppedBricks.mapIndexed { index, brick -> brick to droppedBricks.drop(index + 1) }
            .parallelStream()
            .map { (b, remaining) ->
                val removed = mutableSetOf(b)
                var removedVol = volume - b.volume

                for (brick in remaining) {
                    val isSupporting = removed.any { brick.isSupporting(it) }

                    // Do this on demand as it's quite computationally expensive
                    val volWithoutBrick by lazy { removedVol - brick.volume }
                    val canDrop by lazy { volWithoutBrick.intersect(brick.drop().volume).isEmpty() }
                    if (isSupporting && canDrop) {
                        removedVol = volWithoutBrick
                        removed.add(brick)
                    }
                }

                // The number of removed bricks, minus the default one
                removed.count() - 1
            }.reduce(0) { acc, v -> acc + v }
    }

    companion object {

        fun parseInput(dataFile: DataFile): Tower {
            val bricks = fileToStream(22, dataFile).map { Brick.fromString(it) }.toList()
            return Tower(bricks)
        }
    }
}


fun day22(skip: Boolean = true) {
    if (skip) {
        return
    }

    val input = Tower.parseInput(DataFile.Part1)
    report(
        dayNumber = 22,
        part1 = input.part1(),
        part2 = input.part2(),
    )
}