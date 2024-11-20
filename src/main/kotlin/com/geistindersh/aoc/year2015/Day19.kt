package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report
import java.util.PriorityQueue

class Day19(
    dataFile: DataFile,
) {
    private val molecule = fileToStream(2015, 19, dataFile).last()
    private val lookupTable =
        fileToStream(2015, 19, dataFile)
            .takeWhile { it.isNotBlank() }
            .map { it.substringBefore(" ") to it.substringAfterLast(" ") }
            .let {
                val table = mutableMapOf<String, MutableList<String>>()
                it.forEach { (k, v) ->
                    val lst = table.getOrPut(k) { mutableListOf() }
                    lst.add(v)
                }
                table.entries.associate { (k, v) -> k to v.toList() }
            }

    @JvmInline
    value class Molecule(
        val name: String,
    )

    private fun Molecule.replace(
        from: String,
        to: String,
    ) = sequence {
        var idx = name.indexOf(from)
        while (idx >= 0) {
            val toAdd =
                StringBuilder().let { sb ->
                    sb.append(name.subSequence(0, idx))
                    sb.append(to)
                    sb.append(name.subSequence(idx + from.length, name.length))
                    sb.toString()
                }
            yield(Molecule(toAdd))
            idx = name.indexOf(from, idx + 1)
        }
    }

    fun part1() =
        lookupTable
            .entries
            .flatMap { (k, v) -> v.flatMap { Molecule(molecule).replace(k, it) } }
            .toSet()
            .count()

    fun part2(): Int {
        var minSteps = Int.MAX_VALUE

        val queue =
            PriorityQueue<Pair<Int, Molecule>>(compareBy { it.second.name.length })
                .apply { add(Pair(0, Molecule(molecule))) }
        val seen = mutableSetOf<Molecule>()
        while (queue.isNotEmpty()) {
            val (step, mole) = queue.poll()
            if (mole in seen) continue
            seen.add(mole)

            if (mole.name == "e") {
                minSteps = step
                break
            }

            val toAdd =
                lookupTable
                    .entries
                    .flatMap { (k, v) -> v.flatMap { mole.replace(it, k) } }
                    .map { step + 1 to it }
            queue.addAll(toAdd)
        }

        return minSteps
    }
}

fun day19() {
    val day = Day19(DataFile.Part1)
    report(2015, 19, day.part1(), day.part2())
}
