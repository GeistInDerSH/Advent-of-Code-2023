package day5

import helper.fileToStream
import helper.report

data class Almanac(val seeds: List<Long>, val mappings: List<List<CropMapping>>) {
    fun seedToLocation(seed: Long): Long {
        var value = seed
        for (row in mappings.indices) {
            for (col in mappings[row].indices) {
                val crop = mappings[row][col]
                if (crop.sourceStart <= value && value < crop.sourceEnd) {
                    value += crop.offset
                    break
                }
            }
        }
        return value
    }
}

data class CropMapping(val sourceStart: Long, val destStart: Long, val size: Long) {
    val sourceEnd = sourceStart + size
    val offset = destStart - sourceStart
}

fun parseInput(fileName: String): Almanac {
    val lines = fileToStream(fileName).joinToString(separator = "\n") { it }.split("\n\n")
    val seeds = lines.first().substringAfter(':').trim().split(' ').map { it.toLong() }

    val mappings = lines.drop(1).map { line ->
        line.split('\n').drop(1).map {
            val nums = it.split(' ').map { num -> num.toLong() }
            val dest = nums[0]
            val src = nums[1]
            val size = nums[2]
            CropMapping(src, dest, size)
        }
    }

    return Almanac(seeds, mappings)
}

fun part1(almanac: Almanac) = almanac.seeds.map { almanac.seedToLocation(it) }.minBy { it }

fun part2(almanac: Almanac): Long {
    return almanac.seeds.chunked(2).parallelStream()
        .map { (start, length) ->
            (start..<start + length).minOf { almanac.seedToLocation(it) }
        }.toList().minOf { it }
}

fun day5() {
    val input = parseInput("src/main/resources/day_5/part_1.txt")
    report(
        dayNumber = 5,
        part1 = part1(input),
        part2 = part2(input),
    )
}
