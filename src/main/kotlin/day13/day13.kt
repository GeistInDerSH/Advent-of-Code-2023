package day13

import helper.report
import java.io.File

data class Image(val image: List<String>) {
    fun withoutSmudges(): Int {
        val rowCount = image.size
        val rowSum = (1..<rowCount).sumOf { row ->
            val shortest = row.coerceAtMost(rowCount - row)
            val start = image.subList(row, row + shortest)
            val end = image.subList(row - shortest, row).reversed()
            if (start == end) row * 100 else 0
        }

        val columnCount = image[0].length
        val colSum = (1..<columnCount).sumOf { col ->
            val shortest = (col).coerceAtMost(columnCount - col)
            val allEqual = image.all {
                val start = it.substring(col, col + shortest)
                val end = it.substring(col - shortest, col).reversed()
                start == end
            }
            if (allEqual) col else 0
        }

        return rowSum + colSum
    }

    fun withSmudges(): Int {
        val rowCount = image.size
        val columnCount = image[0].length

        val rowSum = (1..<rowCount).sumOf { row ->
            val shortest = row.coerceAtMost(rowCount - row)
            val start = image.subList(row, row + shortest)
            val end = image.subList(row - shortest, row).reversed()
            val matchingChars =
                start.indices.sumOf { col -> start[col].indices.count { start[col][it] == end[col][it] } }
            if (matchingChars == shortest * columnCount - 1) row * 100 else 0
        }

        val colSum = (1..<columnCount).sumOf { col ->
            val shortest = (col).coerceAtMost(columnCount - col)
            val matchingChars = image.sumOf { row ->
                val start = row.substring(col, col + shortest)
                val end = row.substring(col - shortest, col).reversed()
                start.indices.count { start[it] == end[it] }
            }
            if (matchingChars == shortest * rowCount - 1) col else 0
        }

        return rowSum + colSum
    }
}

fun parseInput(fileName: String) = File(fileName).readText().split("\n\n").map { Image(it.split('\n')) }
fun part1(images: List<Image>) = images.sumOf { it.withoutSmudges() }
fun part2(images: List<Image>) = images.sumOf { it.withSmudges() }

fun day13() {
    val input = parseInput("src/main/resources/day_13/part_1.txt")

    report(
        dayNumber = 13,
        part1 = part1(input),
        part2 = part2(input),
    )
}
