package day2

import helper.DataFile
import helper.fileToStream
import helper.report

data class Pull(val red: Int, val blue: Int, val green: Int)
data class Game(val id: Int, val pulls: List<Pull>)

fun parseInput(fileType: DataFile): List<Game> {
    return fileToStream(2, fileType).mapIndexed { index, s ->
        val pulls = s.substringAfter(':').split(";").map { sections ->
            // Map the name to the count, so we can do a lookup later
            val cubes = sections.split(",").map { it.trim() }.map {
                val parts = it.split(" ")
                val name = parts.last()
                val count = parts.first().toInt()
                name to count
            }

            Pull(
                red = cubes.singleOrNull { it.first == "red" }?.second ?: 0,
                blue = cubes.singleOrNull { it.first == "blue" }?.second ?: 0,
                green = cubes.singleOrNull { it.first == "green" }?.second ?: 0,
            )
        }

        Game(index + 1, pulls)
    }.toList()
}

/**
 * Go through each game, and add the IDs of each one that is a valid game
 *
 * @param games The games that have been played
 * @param cubeCounts A [Pull] that contains the maximum values for each of the cube types
 * @return The sum of the IDs
 */
fun part1(games: List<Game>, cubeCounts: Pull): Int {
    return games.filter { game ->
        game.pulls.all { cubeCounts.red >= it.red && cubeCounts.blue >= it.blue && cubeCounts.green >= it.green }
    }.sumOf { it.id }
}

/**
 * Determine the minimum number of cubes needed for each game to be playable, then multiply them together, and
 * sum them up for each game
 *
 * @param games The games that have been played
 * @return The sum of the product of the minimum number of each cube type for each game
 */
fun part2(games: List<Game>): Int {
    return games.sumOf { game ->
        var red = 0
        var blue = 0
        var green = 0
        game.pulls.forEach { pull ->
            red = red.coerceAtLeast(pull.red)
            blue = blue.coerceAtLeast(pull.blue)
            green = green.coerceAtLeast(pull.green)
        }

        red * blue * green
    }
}

fun day2() {
    val input = parseInput(DataFile.Part1)
    val cubeCounts = Pull(red = 12, blue = 14, green = 13)
    report(
        dayNumber = 2,
        part1 = part1(input, cubeCounts),
        part2 = part2(input)
    )
}