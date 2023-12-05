package day5

import helper.fileToStream
import helper.report
import java.math.BigInteger

data class HLM(val seeds: List<BigInteger>, val mappings: List<Mapping>) {
    val sourceToDest = mappings.associate { it.sourceName to it.destName }
    private val destToCropMapping = mappings.associate { it.destName to it.crops }

    fun getMappedValue(dest: String, value: BigInteger): BigInteger {
        if (dest !in destToCropMapping) {
            return value
        }
        return destToCropMapping[dest]!!.firstNotNullOfOrNull { it.getMappedValue(value) } ?: value
    }
}

data class Mapping(val sourceName: String, val destName: String, val crops: List<CropMapping>)

data class CropMapping(val sourceStart: BigInteger, val destStart: BigInteger, val size: BigInteger) {
    fun getMappedValue(value: BigInteger): BigInteger? {
        return if (value >= sourceStart && value <= sourceStart + size) {
            val offset = value - sourceStart
            destStart + offset
        } else {
            null
        }
    }
}

fun parseInput(fileName: String): HLM {
    val lines = fileToStream(fileName).joinToString(separator = "\n") { it }.split("\n\n")
    val seeds = lines.first().substringAfter(':').trim().split(' ').map { it.toBigInteger() }

    val f = lines.drop(1).map { line ->
        val parts = line.split('\n')
        val name = parts[0].substringBefore(' ').split('-')
        val crops = parts.drop(1).map {
            val nums = it.split(' ').map { it.toBigInteger() }
            val dest = nums[0]
            val src = nums[1]
            val size = nums[2]
            CropMapping(src, dest, size)
        }
        Mapping(name[0], name[2], crops)
    }

    return HLM(seeds, f)
}

fun part1(hlm: HLM): BigInteger {
    return hlm.seeds.map {
        val queue = ArrayDeque(listOf("seed" to it))
        var minimum = BigInteger.valueOf(Long.MAX_VALUE)
        while (queue.isNotEmpty()) {
            val (source, number) = queue.removeFirst()

            if (source !in hlm.sourceToDest) {
                if (number < minimum) {
                    minimum = number
                }
            } else {
                val dest = hlm.sourceToDest[source]!!
                val value = hlm.getMappedValue(dest, number)

                queue.add(dest to value)
            }
        }

        minimum
    }.minBy { it }
}

fun day5() {
    val input = parseInput("src/main/resources/day_5/part_1.txt")
    report(
        dayNumber = 5,
        part1 = part1(input),
        part2 = ""
    )
}
