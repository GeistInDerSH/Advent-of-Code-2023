package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull

class Day12(dataFile: DataFile) {
    private val data = fileToString(2015, 12, dataFile)

    private fun filterInts(value: Any?): Pair<Int, Boolean> {
        return when (value) {
            is JsonPrimitive -> {
                val int = value.intOrNull ?: 0
                int to false
            }

            is Int -> value to false
            is List<*> -> value.map(::filterInts).sumOf { it.first } to false
            is Map<*, *> -> {
                val hasRed =
                    value
                        .values
                        .filterIsInstance<JsonPrimitive>()
                        .filter { it.isString }
                        .any { it.toString() == "\"red\"" }

                if (hasRed) {
                    0 to true
                } else {
                    value.values.map(::filterInts).sumOf { it.first } to false
                }
            }

            else -> 0 to false
        }
    }

    fun part1() = "-?[0-9]+".toRegex().findAll(data).sumOf { it.value.toInt() }

    fun part2() =
        Json.parseToJsonElement(data)
            .let {
                when (it) {
                    is JsonObject -> it
                    else -> mapOf("a" to it)
                }
            }
            .values
            .map(::filterInts)
            .sumOf { it.first }
}

fun day12() {
    val day = Day12(DataFile.Part1)
    report(2015, 12, day.part1(), day.part2())
}
