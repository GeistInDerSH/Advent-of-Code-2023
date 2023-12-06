package day5

import helper.fileToStream
import helper.report

data class Almanac(val seeds: List<Long>, val mappings: List<Mapping>) {
    private val destToCropMapping = mappings.associate { map -> map.destName to map.crops }

    private fun getMappedValue(dest: String, value: Long): Long {
        return destToCropMapping[dest]?.firstNotNullOfOrNull { it.getMappedValue(value) } ?: value
    }

    fun seedToLocation(seed: Long): Long {
        val soil = getMappedValue("soil", seed)
        val fertilizer = getMappedValue("fertilizer", soil)
        val water = getMappedValue("water", fertilizer)
        val light = getMappedValue("light", water)
        val temperature = getMappedValue("temperature", light)
        val humidity = getMappedValue("humidity", temperature)
        return getMappedValue("location", humidity)
    }
}

data class Mapping(val destName: String, val crops: List<CropMapping>)

data class CropMapping(val sourceStart: Long, val destStart: Long, val size: Long) {
    private val sourceEnd = sourceStart + size
    fun getMappedValue(value: Long): Long? {
        return if (value in sourceStart..<sourceEnd) {
            destStart + value - sourceStart
        } else {
            null
        }
    }
}

fun parseInput(fileName: String): Almanac {
    val lines = fileToStream(fileName).joinToString(separator = "\n") { it }.split("\n\n")
    val seeds = lines.first().substringAfter(':').trim().split(' ').map { it.toLong() }

    val f = lines.drop(1).map { line ->
        val parts = line.split('\n')
        val name = parts[0].substringBefore(' ').substringAfter("to-")
        val crops = parts.drop(1).map {
            val nums = it.split(' ').map { num -> num.toLong() }
            val dest = nums[0]
            val src = nums[1]
            val size = nums[2]
            CropMapping(src, dest, size)
        }
        Mapping(name, crops)
    }

    return Almanac(seeds, f)
}

fun part1(almanac: Almanac): Long {
    return almanac.seeds.map {
        almanac.seedToLocation(it)
    }.minBy { it }
}

fun part2(almanac: Almanac): Long {
    return almanac.seeds.chunked(2).parallelStream().map { pair ->
        val start = pair.first()
        val length = pair.last()
        var min = Long.MAX_VALUE
        (start..<start + length).forEach { min = min.coerceAtMost(almanac.seedToLocation(it)) }
        min
    }.toList().minBy { it }
}

fun day5() {
    val input = parseInput("src/main/resources/day_5/part_1.txt")
    report(
        dayNumber = 5,
        part1 = part1(input),
        part2 = part2(input),
    )
}
