package com.geistindersh.aoc.year2023.day15

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

data class HASH(val string: String) {
    // Only Part 2 uses these values, so there's not a need to generate them for part 1
    private val delim = lazy { if ('=' in string) '=' else '-' }
    private val label = lazy { string.substringBefore(delim.value) }
    private val focus = lazy { string.substringAfter(delim.value).toIntOrNull() }

    /**
     * Apply the hashing function, given in the text of part 1, to the given string [str]
     *
     * @param str The string to hash
     * @return The numeric value of the string
     */
    private fun hashString(str: String): Int {
        var value = 0
        for (char in str) {
            value += char.code
            value *= 17
            value %= 256
        }
        return value
    }

    override fun hashCode() = hashString(string)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return string == (other as HASH).string
    }

    /**
     * @return The string label, i.e. anything before the '=' or '-'
     */
    fun label() = label.value

    /**
     * @return The focus number or null, i.e. the number after '=' or '-', or null if it DNE
     */
    fun focus() = focus.value

    /**
     * @return The box that the [HASH] should go into
     */
    fun box() = hashString(label.value)
}

fun parseInput(fileType: DataFile): List<HASH> {
    return fileToString(2023, 15, fileType)
        .split(',')
        .map { HASH(it) }
}

fun part1(hashes: List<HASH>) = hashes.sumOf { it.hashCode() }

fun part2(hashes: List<HASH>): Int {
    val boxes = mutableMapOf<Int, MutableList<HASH>>()
    for (hash in hashes) {
        val box = hash.box()
        if (hash.focus() == null) { // There isn't a focus, so it means the lens should be removed (if the box exists)
            val index = boxes[box]?.firstOrNull { it.label() == hash.label() } ?: continue
            boxes[box]!!.remove(index)
        } else if (box !in boxes) { // Box does not yet exist, so add it and move on
            boxes[box] = mutableListOf(hash)
        } else { // Possibly locate the value and update it or add it if the label DNE
            val index = boxes[box]!!.indexOfFirst { it.label() == hash.label() }
            if (index >= 0) {
                boxes[box]!![index] = hash
            } else {
                boxes[box]!!.add(hash)
            }
        }
    }

    return boxes
        .flatMap { (key, value) ->
            value.mapIndexed { index, hash -> (key + 1) * (index + 1) * hash.focus()!! }
        }
        .sum()
}

fun day15() {
    val input = parseInput(DataFile.Part1)

    report(
        year = 2023,
        dayNumber = 15,
        part1 = part1(input),
        part2 = part2(input),
    )
}