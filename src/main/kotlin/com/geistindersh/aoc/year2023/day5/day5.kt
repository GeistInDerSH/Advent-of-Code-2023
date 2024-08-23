package com.geistindersh.aoc.year2023.day5

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

data class Almanac(val seeds: List<Long>, val mappings: List<List<CropMapping>>) {
    /**
     * Transform the [seed] by sending it through each of the [mappings] to get the next value
     * of the seed.
     *
     * @param seed The starting value
     * @return The ending location after all transformations
     */
    fun seedToLocation(seed: Long): Long {
        var value = seed
        // Because the mappings are in transformation order, we can walk through the list
        // iteratively without needing to jump between source and destinations
        for (row in mappings.indices) {
            // Locate the current transformation, and run through each of the ranges
            // finding the first one that the current values exists in and add the offset
            val cropRow = mappings[row]
            for (col in cropRow.indices) {
                val crop = cropRow[col]
                if (crop.sourceStart <= value && value < crop.sourceEnd) {
                    value += crop.offset
                    break
                }
            }
        }
        return value
    }
}

/**
 * The container for storing how values are mapped between different plots. Because not all the "raw" data is needed,
 * some pre-processing is done when parsing
 */
data class CropMapping(val sourceStart: Long, val sourceEnd: Long, val offset: Long)

fun parseInput(fileType: DataFile): Almanac {
    val lines = fileToString(5, fileType).split("\n\n")
    val seeds = lines
        .first()
        .substringAfter(':')
        .trim()
        .split(' ')
        .map { it.toLong() }

    val mappings = lines.drop(1).map { line ->
        line.split('\n').drop(1).map {
            val (dest, src, size) = it.split(' ').map { num -> num.toLong() }
            // Calculate the end of the source range, and the offset we will jump to if there is a match,
            // so we don't have to do it later
            CropMapping(src, src + size, dest - src)
        }
    }

    return Almanac(seeds, mappings)
}

/**
 * Send each seed through the transformation process, then take the minimum value of the result
 *
 * @param almanac The container with the seeds, and their transformations
 * @return The minimum value of the seed transformation
 */
fun part1(almanac: Almanac) = almanac.seeds.minOf { almanac.seedToLocation(it) }

/**
 * Each seed is now a pair of start to length, so we need to test each seed in all the pairs of ranges to
 * determine the minimum value.
 *
 * @param almanac The container with the seeds, and their transformations
 * @return The minimum value of the seed transformation
 */
fun part2(almanac: Almanac): Long {
    return almanac
        .seeds
        .chunked(2)
        .parallelStream()
        .map { (start, length) -> (start..<start + length).minOf { almanac.seedToLocation(it) } }
        .toList()
        .minOf { it }
}

fun day5() {
    val input = parseInput(DataFile.Part1)
    report(
        dayNumber = 5,
        part1 = part1(input),
        part2 = part2(input),
    )
}
