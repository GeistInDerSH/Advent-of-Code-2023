package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.removeAll
import java.util.PriorityQueue

class Day21(
    dataFile: DataFile,
) {
    private val food =
        fileToStream(2020, 21, dataFile)
            .map { Food.from(it) }
            .toList()

    private data class Food(
        val ingredients: Set<String>,
        val allergen: Set<String>,
    ) {
        companion object {
            fun from(line: String): Food {
                val (ingredients, allergen) =
                    line
                        .removeAll("(),")
                        .split(" contains ")
                        .map { it.split(" ").toSet() }
                return Food(ingredients, allergen)
            }
        }
    }

    private fun List<Food>.determineAllergens(): Map<String, String> {
        val known = mutableMapOf<String, String>()
        val queue =
            PriorityQueue<Food>(compareBy { it.allergen.size })
                .apply { addAll(this@determineAllergens) }

        while (queue.isNotEmpty()) {
            val head = queue.poll()
            for (unknown in head.allergen - known.keys) {
                var possibilities = head.ingredients - known.values.toSet()
                // Get the Food that contain the unknown allergen.
                // Repeatedly apply set intersection to get the overlap
                for (it in this.filter { unknown in it.allergen }) {
                    possibilities = possibilities.intersect(it.ingredients)
                }
                // If we only have one item left, that's the ingredient
                if (possibilities.size == 1) {
                    known[unknown] = possibilities.first()
                }
            }
        }
        return known
    }

    fun part1() =
        food
            .determineAllergens()
            .let { known ->
                food.sumOf { f -> f.ingredients.count { it !in known.values } }
            }

    fun part2() =
        food
            .determineAllergens()
            .toSortedMap()
            .map { it.value }
            .joinToString(",")
}

fun day21() {
    val day = Day21(DataFile.Part1)
    report(2020, 21, day.part1(), day.part2())
}
