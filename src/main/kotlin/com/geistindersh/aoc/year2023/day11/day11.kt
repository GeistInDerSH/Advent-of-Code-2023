package com.geistindersh.aoc.year2023.day11

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

private typealias Galaxy = Pair<Int, Int>

data class StarChart(val galaxies: List<Galaxy>, val emptyRows: Set<Int>, val emptyCols: Set<Int>) {
    /**
     * Determine the sum of the distances between all [galaxies], when accounting for empty space
     * between them
     *
     * @param emptySpaceSize How much space is represented by an empty row/column
     * @return The sum of the distances between the [galaxies]
     */
    fun sumDistanceWithEmptySpace(emptySpaceSize: Int): Long {
        var sum = 0L
        for (i in galaxies.indices) {
            for (j in i..<galaxies.size) {
                val (x1, y1) = galaxies[i]
                val (x2, y2) = galaxies[j]

                val xRange = if (x1 > x2) x2..<x1 else x1..<x2
                sum += xRange.sumOf { if (it in emptyRows) emptySpaceSize else 1 }

                val yRange = if (y1 > y2) y2..<y1 else y1..<y2
                sum += yRange.sumOf { if (it in emptyCols) emptySpaceSize else 1 }
            }
        }

        return sum
    }
}


fun parseInput(fileType: DataFile): StarChart {
    val raw = fileToStream(11, fileType).map { it.toList() }.toList()

    // Get the indexes of the empty rows and columns, so we can statically check this later.
    // Attempting to expand the raw value into the correct size causes OOM errors (rightfully so...)
    val emptyRows = raw.indices.filter { row -> raw[row].all { it == '.' } }.toSet()
    val emptyCol = (raw[0].indices).filter { col -> raw.all { it[col] == '.' } }.toSet()

    // We just want the X,Y values for the galaxies as a pair
    val galaxies = raw.flatMapIndexed { x: Int, row: List<Char> ->
        row.mapIndexedNotNull { y, c ->
            if (c == '.') {
                null
            } else {
                Galaxy(x, y)
            }
        }
    }

    return StarChart(galaxies, emptyRows, emptyCol)
}

fun part1(chart: StarChart) = chart.sumDistanceWithEmptySpace(2)
fun part2(chart: StarChart) = chart.sumDistanceWithEmptySpace(1000000)

fun day11() {
    val input = parseInput(DataFile.Part1)
    report(
        dayNumber = 11,
        part1 = part1(input),
        part2 = part2(input),
    )
}
