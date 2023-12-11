package day11

import helper.fileToStream
import helper.report
import kotlin.math.absoluteValue

private typealias Galaxy = Pair<Int, Int>

fun parseInput(fileName: String, emptySize: Int): Set<Galaxy> {
    val raw = fileToStream(fileName).map { it.toMutableList() }.toMutableList()

    val emptyRows = raw.indices.filter { row -> raw[row].all { it == '.' } }
    val emptyCol = (raw[0].indices).filter { col -> raw.all { it[col] == '.' } }

    var rowActual = -1
    return raw.flatMapIndexed { x: Int, row: MutableList<Char> ->
        rowActual += if (x in emptyRows) emptySize + 1 else 1
        var colActual = -1
        row.mapIndexed { y, c ->
            colActual += if (y in emptyCol) emptySize + 1 else 1

            if (c == '.') {
                null
            } else {
                Galaxy(rowActual, colActual)
            }
        }.filterNotNull()
    }.toSet()
}

fun getDistanceBetween(points: Pair<Galaxy, Galaxy>) = getDistanceBetween(points.first, points.second)
fun getDistanceBetween(start: Galaxy, end: Galaxy): Long {
    var (startRow, startCol) = start
    var size = 0L
    while (startRow != end.first || startCol != end.second) {
        val row = startRow - end.first
        val col = startCol - end.second
        if (row.absoluteValue >= col.absoluteValue) {
            startRow += if (row < 0) 1 else -1
        } else {
            startCol += if (col < 0) 1 else -1
        }
        size += 1
    }
    return size
}

fun getUniquePaths(galaxies: Set<Galaxy>): Set<Pair<Galaxy, Galaxy>> {
    val pairs = mutableSetOf<Pair<Galaxy, Galaxy>>()
    for (start in galaxies) {
        for (dest in (galaxies - start)) {
            if ((start to dest) in pairs || (dest to start) in pairs) {
                continue
            }
            pairs.add(start to dest)
        }
    }
    return pairs
}

fun sumOfLengths(galaxies: Set<Galaxy>): Long {
    val unique = getUniquePaths(galaxies).toList()
    return unique.windowed(100, 100, true).parallelStream().map { col ->
        col.sumOf { getDistanceBetween(it) }
    }.reduce(0) { acc, len -> acc + len }
}

fun part1(fileName: String) = sumOfLengths(parseInput(fileName, 1))
fun part2(fileName: String) = sumOfLengths(parseInput(fileName, 999999))

fun day11(skip: Boolean = true) {
    if (skip) {
        return
    }
    val fileName = "src/main/resources/day_11/part_1.txt"
    report(
        dayNumber = 11,
        part1 = part1(fileName), // 9543156
        part2 = part2(fileName), // 625243292686
    )
}
