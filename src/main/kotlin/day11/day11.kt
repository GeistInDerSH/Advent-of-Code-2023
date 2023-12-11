package day11

import helper.fileToStream
import helper.report

private typealias Galaxy = Pair<Int, Int>

data class StarChart(val galaxies: List<Galaxy>, val emptyRows: Set<Int>, val emptyCols: Set<Int>) {
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


fun parseInput(fileName: String): StarChart {
    val raw = fileToStream(fileName).map { it.toList() }.toList()

    val emptyRows = raw.indices.filter { row -> raw[row].all { it == '.' } }.toSet()
    val emptyCol = (raw[0].indices).filter { col -> raw.all { it[col] == '.' } }.toSet()

    val galaxies = raw.flatMapIndexed { x: Int, row: List<Char> ->
        row.mapIndexed { y, c ->
            if (c == '.') {
                null
            } else {
                Galaxy(x, y)
            }
        }.filterNotNull()
    }

    return StarChart(galaxies, emptyRows, emptyCol)
}

fun part1(chart: StarChart) = chart.sumDistanceWithEmptySpace(2)
fun part2(chart: StarChart) = chart.sumDistanceWithEmptySpace(1000000)

fun day11() {
    val input = parseInput("src/main/resources/day_11/part_1.txt")
    report(
        dayNumber = 11,
        part1 = part1(input), // 9543156
        part2 = part2(input), // 625243292686
    )
}