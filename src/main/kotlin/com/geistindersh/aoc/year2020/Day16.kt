package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report
import com.geistindersh.aoc.helper.strings.extractPositiveIntegers

class Day16(
    dataFile: DataFile,
) : AoC<Int, Long> {
    private val fields: Map<String, Set<Int>>
    private val userTickets: List<Int>
    private val nearbyTickets: List<List<Int>>

    init {
        val data = fileToString(2020, 16, dataFile).split("\n\n")
        fields =
            data[0]
                .split("\n")
                .associate { line ->
                    val name = line.substringBefore(": ")
                    val ranges =
                        line
                            .extractPositiveIntegers()
                            .windowed(2, 2) { it.first()..it.last() }
                            .flatMap { it.toSet() }
                            .toSet()
                    name to ranges
                }
        userTickets = data[1].extractPositiveIntegers()
        nearbyTickets =
            data[2]
                .split("\n")
                .drop(1)
                .map(String::extractPositiveIntegers)
    }

    private fun invalidTickets() =
        nearbyTickets
            .flatten()
            .filter { ticket -> fields.values.none { ticket in it } }

    override fun part1() = invalidTickets().reduce(Int::plus)

    override fun part2(): Long {
        val invalid = invalidTickets().toSet()
        val validTickets =
            nearbyTickets
                .filter { ticket -> ticket.none { it in invalid } }

        val remainingFields = fields.keys.toMutableSet()
        val mapIndex: MutableMap<String, Int> = mutableMapOf()
        while (remainingFields.isNotEmpty()) {
            val validFields =
                mutableMapOf<String, MutableList<Int>>()
                    .withDefault { mutableListOf() }

            for (col in validTickets[0].indices) {
                if (col in mapIndex.values) continue

                for (field in remainingFields) {
                    val values = fields[field]!!
                    if (validTickets.indices.all { validTickets[it][col] in values }) {
                        val lst = validFields.getValue(field)
                        lst.add(col)
                        validFields[field] = lst
                    }
                }
            }

            for ((name, values) in validFields) {
                if (name in mapIndex) continue
                if (values.size != 1) continue
                mapIndex[name] = values.first()
                remainingFields.remove(name)
            }
        }

        return mapIndex
            .filter { it.key.startsWith("departure") }
            .map { userTickets[it.value].toLong() }
            .toList()
            .reduce(Long::times)
    }
}

fun day16() {
    val day = Day16(DataFile.Part1)
    report(2020, 16, day.part1(), day.part2())
}
