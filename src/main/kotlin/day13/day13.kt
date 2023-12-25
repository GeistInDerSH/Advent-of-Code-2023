package day13

import helper.files.DataFile
import helper.files.fileToString
import helper.report

data class Image(val image: List<String>) {
    /**
     * @return The value of the reflected image
     */
    fun withoutSmudges(): Int {
        // Check that a window of rows reflects perfectly on the other side of the list
        val rowCount = image.size
        val rowSum = (1..<rowCount).sumOf { row ->
            val shortest = row.coerceAtMost(rowCount - row)
            val start = image.subList(row, row + shortest)
            val end = image.subList(row - shortest, row).reversed()
            if (start == end) row * 100 else 0
        }

        // Check that a substring of all the rows can be folded in half to make a mirror
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

    /**
     * @return The value of the reflected image, when accounting for smudges
     */
    fun withSmudges(): Int {
        val rowCount = image.size
        val columnCount = image[0].length

        // Check that a window of rows reflects perfectly on the other side of the list
        val rowSum = (1..<rowCount).sumOf { row ->
            val shortest = row.coerceAtMost(rowCount - row)
            val start = image.subList(row, row + shortest)
            val end = image.subList(row - shortest, row).reversed()
            // The procedure is mostly the same as before, however now we want to know how many of the
            // characters match in all of them, and check to see if there is only 1 mismatch
            val matchingChars = start
                .indices
                .sumOf { col ->
                    start[col]
                        .indices
                        .count { start[col][it] == end[col][it] }
                }
            if (matchingChars == shortest * columnCount - 1) row * 100 else 0
        }

        // Check that a substring of all the rows can be folded in half to make a mirror
        val colSum = (1..<columnCount).sumOf { col ->
            val shortest = (col).coerceAtMost(columnCount - col)
            // Check to see if there is only 1 mismatch in all the rows
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

fun parseInput(type: DataFile): List<Image> {
    return fileToString(13, type)
        .split("\n\n")
        .map { Image(it.split('\n')) }
}

fun part1(images: List<Image>) = images.sumOf { it.withoutSmudges() }
fun part2(images: List<Image>) = images.sumOf { it.withSmudges() }

fun day13() {
    val input = parseInput(DataFile.Part1)

    report(
        dayNumber = 13,
        part1 = part1(input),
        part2 = part2(input),
    )
}
