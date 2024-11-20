package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day7(
    dataFile: DataFile,
) {
    private val target = "shiny gold"
    private val bags =
        fileToStream(2020, 7, dataFile)
            .map { line ->
                val parts = line.split(" ")
                val outer = "${parts[0]} ${parts[1]}"

                val inner = mutableListOf<Bag>()
                var i = 4
                while (i < parts.size) {
                    val count = parts[i].toIntOrNull() ?: break
                    val name = "${parts[i + 1]} ${parts[i + 2]}"
                    inner.add(Bag(name, count))
                    i += 4
                }

                outer to inner.toList()
            }.toMap()

    private data class Bag(
        val name: String,
        val count: Int,
    )

    private fun isReachable(
        name: String,
        reachabilityMap: MutableMap<String, Boolean>,
    ): MutableMap<String, Boolean> {
        if (name in reachabilityMap) return reachabilityMap
        if (name !in bags) {
            reachabilityMap[name] = false
            return reachabilityMap
        }

        for (bag in bags[name]!!) {
            when (bag.name) {
                in reachabilityMap -> {
                    if (reachabilityMap[bag.name]!!) {
                        reachabilityMap[name] = true
                    }
                }

                target -> {
                    reachabilityMap[name] = true
                    reachabilityMap[bag.name] = true
                }

                else -> {
                    isReachable(bag.name, reachabilityMap)
                    if (reachabilityMap[bag.name]!!) {
                        reachabilityMap[name] = true
                    }
                }
            }
        }
        if (name !in reachabilityMap) {
            reachabilityMap[name] = false
        }
        return reachabilityMap
    }

    private fun bagCount(
        name: String,
        reachabilityMap: MutableMap<String, Int>,
    ): Int {
        if (bags[name]!!.isEmpty()) return 0

        var count = 0
        for (bag in bags[name]!!) {
            if (bag.name !in reachabilityMap) {
                reachabilityMap[bag.name] = bagCount(bag.name, reachabilityMap)
            }
            count += bag.count
            count += bag.count * reachabilityMap[bag.name]!!
        }
        return count
    }

    fun part1(): Int {
        val reachable = mutableMapOf<String, Boolean>()
        for (bag in bags) {
            isReachable(bag.key, reachable)
        }
        return reachable.entries.filter { it.key != target }.count { it.value }
    }

    fun part2() = bagCount(target, mutableMapOf())
}

fun day7() {
    val day = Day7(DataFile.Part1)
    report(2020, 7, day.part1(), day.part2())
}
