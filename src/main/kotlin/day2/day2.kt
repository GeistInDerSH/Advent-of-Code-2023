package day2

import helper.fileToStream
import helper.report

data class Pull(val red: Int, val blue: Int, val green: Int)
data class Game(val id: Int, val pulls: List<Pull>)

fun parseInput(fileName: String): List<Game> {
    return fileToStream(fileName).mapIndexed { index, s ->
        val pulls = s.substringAfter(':').split(";").map { sections ->
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

fun part1(games: List<Game>, cubeCounts: Pull): Int {
    return games.sumOf { game ->
        val isValid = game.pulls.all { pull ->
            cubeCounts.red >= pull.red && cubeCounts.blue >= pull.blue && cubeCounts.green >= pull.green
        }

        if (isValid) {
            game.id
        } else {
            0
        }
    }
}

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
    val input = parseInput("src/main/resources/day_2/part_1.txt")
    val cubeCounts = Pull(red = 12, blue = 14, green = 13)
    report(
        dayNumber = 2,
        part1 = part1(input, cubeCounts),
        part2 = part2(input)
    )
}