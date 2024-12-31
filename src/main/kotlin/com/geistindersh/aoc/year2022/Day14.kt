package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.iterators.takeWhileInclusive
import com.geistindersh.aoc.helper.report

class Day14(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val data =
        fileToStream(2022, 14, dataFile)
            .map { line ->
                line.split(" -> ").map {
                    val (l, r) = it.split(",", limit = 2)
                    Pair(l.toInt(), r.toInt())
                }
            }.toList()
    private val bounds =
        data
            .flatMap { points ->
                points
                    .windowed(2, 1)
                    .mapNotNull { (p1, p2) ->
                        when {
                            p1.first == p2.first -> {
                                if (p1.second < p2.second) {
                                    (p1.second + 1..<p2.second + 1)
                                        .map { Pair(p1.first, it) }
                                } else {
                                    (p2.second..<p1.second)
                                        .map { Pair(p1.first, it) }
                                        .reversed()
                                }
                            }

                            p1.second == p2.second -> {
                                if (p1.first < p2.first) {
                                    (p1.first + 1..<p2.first + 1)
                                        .map { Pair(it, p1.second) }
                                } else {
                                    (p2.first..<p1.first)
                                        .map { Pair(it, p1.second) }
                                        .reversed()
                                }
                            }

                            else -> null
                        }
                    }.flatten()
            }.toMutableSet()
            .apply { addAll(data.flatten()) }
            .toSet()
    private val bottom = bounds.maxOf { it.second } + 1

    private fun pourSand(isPart1: Boolean) =
        sequence {
            val points = bounds.toMutableSet()
            var source = Pair(500, 0)

            while (true) {
                if (source.second == bottom) {
                    if (isPart1) {
                        yield(emptySet())
                        source = Pair(500, 0)
                    } else {
                        points.add(source)
                        yield(points)
                        source = Pair(500, 0)
                    }
                }

                val newSource =
                    listOf(
                        Pair(source.first, source.second + 1),
                        Pair(source.first - 1, source.second + 1),
                        Pair(source.first + 1, source.second + 1),
                    ).firstOrNull { it !in points }
                if (newSource != null) {
                    source = newSource
                } else {
                    points.add(source)
                    yield(points)
                    source = Pair(500, 0)
                }
            }
        }

    override fun part1(): Int =
        pourSand(true)
            .drop(1)
            .takeWhileInclusive { it.isNotEmpty() }
            .count()

    override fun part2() =
        pourSand(false)
            .takeWhileInclusive { Pair(500, 0) !in it }
            .count()
}

fun day14() {
    val day = Day14(DataFile.Part1)
    report(2022, 14, day.part1(), day.part2())
}
