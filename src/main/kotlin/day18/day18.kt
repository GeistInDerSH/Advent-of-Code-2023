package day18

import helper.DataFile
import helper.fileToStream
import helper.report

enum class Direction(private val rowInc: Int, private val colInc: Int) {
    Up(-1, 0),
    Left(0, -1),
    Right(0, 1),
    Down(1, 0);

    fun pair() = rowInc to colInc

    companion object {
        fun tryFromString(s: String): Direction? {
            return when (s) {
                "R", "0" -> Right
                "L", "2" -> Left
                "U", "3" -> Up
                "D", "1" -> Down
                else -> null
            }
        }
    }
}

data class DigLocation(val direction: Direction, val distance: Int)

data class DigPlan(private val digLocations: List<DigLocation>) {
    /**
     * @return The area inside the [digLocations]
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
    val plan = fileToStream(18, dataFile).map { line ->
        val (dir, dis, _) = line.split(' ')
        val direction = Direction.tryFromString(dir) ?: throw Exception("Couldn't convert $dir to Direction")
        val distance = dis.toIntOrNull() ?: throw Exception("Failed to convert $dis to int")
        DigLocation(direction, distance)
    }.toList()
    return DigPlan(plan).areaInsideLocations()
}

fun part2(dataFile: DataFile): Long {
    val plan = fileToStream(18, dataFile).map { line ->
        val (_, _, hex) = line.split(' ')
        val hexString = hex.substringAfter('#').substringBefore(')')
        // Get the dig Direction as Char
        val last = hexString.last().toString()
        val direction = Direction.tryFromString(last) ?: throw Exception("Failed to convert $last to Direction")

        // Get the distance of the edge
        val distance = hexString.substring(0, hexString.length - 1).toInt(16)

        DigLocation(direction, distance)
    }.toList()
    return DigPlan(plan).areaInsideLocations()
}

fun day18() {
    val file = DataFile.Part1
    report(
        dayNumber = 18,
        part1 = part1(file),
        part2 = part2(file),
    )
}
