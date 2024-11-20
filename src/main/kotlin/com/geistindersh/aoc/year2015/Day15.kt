package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.math.combinations
import com.geistindersh.aoc.helper.report

class Day15(dataFile: DataFile) {
    private val ingredients =
        fileToStream(2015, 15, dataFile)
            .map { line ->
                val name = line.substringBefore(": ")
                val nums = "-?[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList()
                Ingredient(name, nums[0], nums[1], nums[2], nums[3], nums[4])
            }
            .sortedBy { it.name }
            .toList()

    data class Ingredient(val name: String, val cap: Int, val dur: Int, val flav: Int, val text: Int, val cal: Int) {
        fun scoreList(count: Int) = listOf(cap * count, dur * count, flav * count, text * count)
    }

    data class Cookie(private val recipe: Map<Ingredient, Int>) {
        fun score() =
            recipe
                .entries
                .map { (ingredient, count) -> ingredient.scoreList(count) } // get the scores as a list, that can be reduced in place
                .reduce { l1, l2 ->
                    val acc = l1.toMutableList()
                    l1.indices.forEach { i ->
                        acc[i] += l2[i]
                    }
                    acc
                }
                .map { if (it < 0) 0 else it } // ensure no negative numbers
                .reduce(Int::times)

        fun calories() = recipe.entries.sumOf { (ingredient, count) -> ingredient.cal * count }
    }

    private fun getCookies() =
        combinations(ingredients.size, 100)
            .asSequence()
            .map { it.toList() }
            .map { ingredients.zip(it).toMap() }
            .map { Cookie(it) }

    fun part1() = getCookies().maxOf { it.score() }

    fun part2() = getCookies().filter { it.calories() == 500 }.maxOf { it.score() }
}

fun day15() {
    val day = Day15(DataFile.Part1)
    report(2015, 15, day.part1(), day.part2())
}
