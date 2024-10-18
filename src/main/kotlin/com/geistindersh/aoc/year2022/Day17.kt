package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day17(dataFile: DataFile) {
    private val pattern = fileToString(2022, 17, dataFile)

    private data class Checkpoint(val peak: List<Int>, val patternIndex: Int, val rockIndex: Long)

    private class Pile(private val pattern: String) {
        private val points = (0..6).map { Pair(it, -1) }.toMutableSet()
        private var patternIndex = 0
            set(idx) {
                field = idx % pattern.length
            }
        private var rockIndex = 0L
            set(idx) {
                field = idx % 5
            }

        fun height() = points.maxOf { it.second + 1 }

        private fun next(): Rock {
            val rock = Rock.create(rockIndex)
            rockIndex += 1
            val base = 4 + (points.maxOfOrNull { it.second } ?: -1)
            val newPoints = rock.points.map { Pair(it.first, it.second + base) }
            return Rock(newPoints)
        }

        fun addRock() {
            var rock = next()

            while (true) {
                val newRock = rock.push(pattern[patternIndex])
                patternIndex += 1
                if (!hasIntersection(newRock)) rock = newRock

                val drop = rock.drop()
                if (hasIntersection(drop)) break
                rock = drop
            }

            points.addAll(rock.points)
        }

        fun getCheckpoint(): Checkpoint {
            val height = height()
            val peaks = points
                .groupBy { it.first }
                .entries
                .sortedBy { it.key }
                .map { it.value.maxBy { p -> p.second } }
                .map { it.second - height }
            return Checkpoint(peaks, patternIndex, rockIndex)
        }

        private fun hasIntersection(rock: Rock): Boolean {
            return rock.points.intersect(points).isNotEmpty() || rock.points.any { it.first !in (0..6) }
        }
    }

    private data class Rock(val points: Collection<Pair<Int, Int>>) {
        fun push(direction: Char): Rock {
            val newPoints = when (direction) {
                '>' -> points.map { Pair(it.first + 1, it.second) }
                '<' -> points.map { Pair(it.first - 1, it.second) }
                else -> throw IllegalArgumentException("Unknown direction: $direction")
            }
            return Rock(newPoints)
        }

        fun drop(): Rock {
            val newPoints = points.map { Pair(it.first, it.second - 1) }
            return Rock(newPoints)
        }

        companion object {
            fun create(id: Long): Rock {
                val points = when (id) {
                    0L -> listOf(2 to 0, 3 to 0, 4 to 0, 5 to 0)
                    1L -> listOf(3 to 0, 2 to 1, 3 to 1, 4 to 1, 3 to 2)
                    2L -> listOf(2 to 0, 3 to 0, 4 to 0, 4 to 1, 4 to 2)
                    3L -> listOf(2 to 0, 2 to 1, 2 to 2, 2 to 3)
                    4L -> listOf(2 to 0, 2 to 1, 3 to 0, 3 to 1)
                    else -> throw IllegalArgumentException("Unknown direction: $id")
                }
                return Rock(points)
            }
        }
    }

    private fun getTowerHeight(rockCount: Long): Long {
        val cache = mutableMapOf<Checkpoint, Pair<Long, Int>>()
        val pile = Pile(pattern)

        for (idx in 0..<rockCount) {
            pile.addRock()

            val checkpoint = pile.getCheckpoint()
            if (checkpoint !in cache) {
                cache[checkpoint] = idx to pile.height()
                continue
            }

            val pileHeight = pile.height()
            val (rockLoopStart, heightLoopStart) = cache.getValue(checkpoint)
            val rocksPerLoop = idx - rockLoopStart
            val loopHeight = pileHeight - heightLoopStart
            val loopsRemaining = (rockCount - idx) / rocksPerLoop
            val rocksRemaining = (rockCount - idx) % rocksPerLoop
            val cond = rockLoopStart + rocksRemaining - 1
            val heightRemaining = cache
                .values
                .first { it.first == cond }
                .second - heightLoopStart
            return pileHeight + heightRemaining + loopsRemaining * loopHeight
        }

        return pile.height().toLong()
    }

    fun part1() = getTowerHeight(2022)
    fun part2() = getTowerHeight(1000000000000)
}

fun day17() {
    val day = Day17(DataFile.Part1)
    report(2022, 17, day.part1(), day.part2())
}