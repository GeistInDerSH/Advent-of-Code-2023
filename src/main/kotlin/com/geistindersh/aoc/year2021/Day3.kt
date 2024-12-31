package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.binary.bitAt
import com.geistindersh.aoc.helper.binary.setBit
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day3(
    dataFile: DataFile,
) : AoC<Int, Int> {
    private val bits = fileToStream(2021, 3, dataFile).map { line -> line.map { it } }.toList()

    private fun List<List<Char>>.getZerosAndOnesInColumn(column: Int) =
        this
            .indices
            .fold(Pair(0, 0)) { (zero, one), row ->
                when (this[row][column]) {
                    '0' -> zero + 1 to one
                    '1' -> zero to one + 1
                    else -> zero to one
                }
            }

    private fun getGammaAndEpsilon() =
        bits[0]
            .indices
            .fold(Pair(0, 0)) { (gamma, epsilon), index ->
                val (zeros, ones) = bits.getZerosAndOnesInColumn(index)
                val pos = bits[0].size - index - 1

                if (ones >= zeros) {
                    gamma.setBit(pos, 1) to epsilon.setBit(pos, 0)
                } else {
                    gamma.setBit(pos, 0) to epsilon.setBit(pos, 1)
                }
            }

    private fun List<List<Char>>.filterByCommonBit(
        index: Int,
        bit: Char,
        maximize: Boolean,
    ): String =
        if (this.size == 2 && index + 1 == this[0].size) {
            this[1].joinToString("")
        } else if (this.size == 1 || index + 1 == this[0].size) {
            this[0].joinToString("")
        } else {
            val filter = this.filter { it[index] == bit }
            val (zeros, ones) = filter.getZerosAndOnesInColumn(index + 1)

            val nextBit =
                if (maximize) {
                    if (ones >= zeros) '1' else '0'
                } else if (zeros > ones) {
                    '1'
                } else {
                    '0'
                }
            filter.filterByCommonBit(index + 1, nextBit, maximize)
        }

    override fun part1() = getGammaAndEpsilon().toList().reduce(Int::times)

    override fun part2(): Int {
        val gamma = getGammaAndEpsilon().first
        val (mostSig, leastSig) = if (gamma.bitAt(bits[0].size - 1) == 1) '1' to '0' else '0' to '1'
        val o2 = bits.filterByCommonBit(0, mostSig, true)
        val c = bits.filterByCommonBit(0, leastSig, false)
        return o2.toInt(2) * c.toInt(2)
    }
}

fun day3() {
    val day = Day3(DataFile.Part1)
    report(2021, 3, day.part1(), day.part2())
}
