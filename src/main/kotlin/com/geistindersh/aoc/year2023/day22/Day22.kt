package com.geistindersh.aoc.year2023.day22

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.ranges.hasOverlap
import com.geistindersh.aoc.helper.report

typealias Volume = Set<Triple<Int, Int, Int>>

data class Brick(
    val lx: Int,
    val ly: Int,
    val lz: Int,
    val rx: Int,
    val ry: Int,
    val rz: Int,
) {
    val volume: Volume =
        (lx..rx)
            .flatMap { x ->
                (ly..ry).flatMap { y ->
                    (lz..rz).map { z -> Triple(x, y, z) }
                }
            }.toSet()
    val dropVolume: Volume = volume.map { Triple(it.first, it.second, it.third - 1) }.toSet()

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
    fun isSupporting(other: Brick): Boolean =
        when {
            !other.isBelow(this) -> false
            hasOverlap(ly, ry, other.ly, other.ry) -> true
            hasOverlap(lx, rx, other.lx, other.rx) -> true
            else -> false
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

class Tower(
    private val bricks: List<Brick>,
) {
    private val cascade = cascade()
    private val droppedBricks = cascade.first
    private val volume = cascade.second
    private val brickToDropped = droppedBricks.mapIndexed { index, brick -> brick to droppedBricks.drop(index + 1) }

    /**
     * Move all [bricks] down until they cannot be moved lower
     * @return The bricks moved as far down as they can go, and the volume of those bricks
     */
    private fun cascade(): Pair<List<Brick>, Volume> {
        val volume = mutableSetOf<Triple<Int, Int, Int>>()
        val updatedBricks =
            bricks
                .sortedBy { it.lz }
                .map { b ->
                    var brick = b
                    while (brick.lz - 1 > 0 && brick.dropVolume.none { it in volume }) {
                        brick = brick.drop()
                    }
                    volume.addAll(brick.volume)
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
    private fun canRemove(
        brick: Brick,
        bricks: List<Brick>,
    ): Boolean {
        val volumeWithoutBrick = volume.filterNot { it in brick.volume }
        return bricks.none { other ->
            if (!brick.isBelow(other) || other.lz == 1) {
                false
            } else {
                volumeWithoutBrick.none { it !in other.volume && it in other.dropVolume }
            }
        }
    }

    fun part1(): Int =
        brickToDropped
            .map { canRemove(it.first, it.second) }
            .count { it }

    fun part2(): Int =
        brickToDropped
            .parallelStream()
            .map { (baseBrick, remainingBricks) ->
                val removed = mutableSetOf(baseBrick)
                var removedVol = volume.filterNot { it in baseBrick.volume }

                var count = 0
                for (brick in remainingBricks) {
                    if (removed.none { brick.isSupporting(it) }) {
                        continue
                    }

                    // Do this on demand as it's quite computationally expensive
                    val volWithoutBrick = removedVol.filterNot { it in brick.volume }
                    if (volWithoutBrick.none { it in brick.dropVolume }) {
                        removedVol = volWithoutBrick
                        removed.add(brick)
                        count += 1
                    }
                }

                // The number of removed bricks, minus the default one
                count
            }.reduce(0, Int::plus)

    companion object {
        fun parseInput(dataFile: DataFile): Tower {
            val bricks =
                fileToStream(2023, 22, dataFile)
                    .map { Brick.fromString(it) }
                    .toList()
            return Tower(bricks)
        }
    }
}

fun day22() {
    val input = Tower.parseInput(DataFile.Part1)
    report(
        year = 2023,
        dayNumber = 22,
        part1 = input.part1(),
        part2 = input.part2(),
    )
}
