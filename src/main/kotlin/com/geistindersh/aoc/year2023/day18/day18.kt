package com.geistindersh.aoc.year2023.day18

import com.geistindersh.aoc.helper.enums.Direction
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

data class DigLocation(val direction: Direction, val distance: Int)

data class DigPlan(private val digLocations: List<DigLocation>) {
    /**
     * @return The area inside the [digLocations]
     * @see <a href="https://en.wikipedia.org/wiki/Shoelace_formula">Shoelace Formula</a>
     * @see <a href="https://en.wikipedia.org/wiki/Pick%27s_theorem">Pick's Theorem</a>
     */
    fun areaInsideLocations(): Long {
        var colPosition = 0.0
        var area = 1.0
        for (dig in digLocations) {
            // Offsets that will be used to generate the ending position of the range
            val (row, col) = dig.direction.pair()
            // ending column position for the current location
            colPosition += col * dig.distance
            // The area added by the current location
            val addedArea = row * dig.distance * colPosition
            // Add the new area and account for missing parts
            area += addedArea + dig.distance / 2.0
        }
        return area.toLong()
    }
}

fun part1(dataFile: DataFile): Long {
    val plan =
        fileToStream(2023, 18, dataFile)
            .map { line ->
                val (dir, dis, _) = line.split(' ')
                val direction = Direction.tryFromString(dir) ?: throw Exception("Couldn't convert $dir to Direction")
                val distance = dis.toInt()
                DigLocation(direction, distance)
            }
            .toList()
    return DigPlan(plan).areaInsideLocations()
}

fun part2(dataFile: DataFile): Long {
    val plan =
        fileToStream(2023, 18, dataFile)
            .map { line ->
                val (_, _, hex) = line.split(' ')
                val hexString = hex.substringAfter('#').substringBefore(')')
                // Get the dig Direction as Char
                val last = hexString.last()
                val direction = Direction.tryFromChar(last) ?: throw Exception("Failed to convert $last to Direction")

                // Get the distance of the edge
                val distance = hexString.substring(0, hexString.length - 1).toInt(16)

                DigLocation(direction, distance)
            }
            .toList()
    return DigPlan(plan).areaInsideLocations()
}

fun day18() {
    val file = DataFile.Part1
    report(
        year = 2023,
        dayNumber = 18,
        part1 = part1(file),
        part2 = part2(file),
    )
}
