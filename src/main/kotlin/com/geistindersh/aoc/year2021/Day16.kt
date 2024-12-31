package com.geistindersh.aoc.year2021

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day16(
    dataFile: DataFile,
) : AoC<Int, Long> {
    private val data = fileToString(2021, 16, dataFile)

    private val lookupTable =
        "0123456789ABCDEF"
            .mapIndexed { i, c ->
                val paddedBinary = i.toString(2).let { (0..<4 - it.length).joinToString("") { "0" } + it }
                c to paddedBinary
            }.toMap()

    private sealed class Packet(
        open val version: Int,
        open val typeId: Int,
    ) {
        data class LiteralValue(
            override val version: Int,
            override val typeId: Int,
            val literals: List<String>,
        ) : Packet(version, typeId)

        data class Operator(
            override val version: Int,
            override val typeId: Int,
            val subPackets: List<Packet>,
        ) : Packet(version, typeId)

        fun sumVersions(): Int =
            when (this) {
                is LiteralValue -> version
                is Operator -> version + subPackets.sumOf { it.sumVersions() }
            }

        fun value(): Long =
            when (this) {
                // Join to a binary string, and convert to a long
                is LiteralValue -> literals.joinToString("") { it }.toLong(2)
                is Operator -> {
                    when (typeId) {
                        0 -> subPackets.sumOf { it.value() }
                        1 -> subPackets.map { it.value() }.reduce(Long::times)
                        2 -> subPackets.minOf { it.value() }
                        3 -> subPackets.maxOf { it.value() }
                        5 -> if (subPackets.first().value() > subPackets.last().value()) 1 else 0
                        6 -> if (subPackets.first().value() < subPackets.last().value()) 1 else 0
                        7 -> if (subPackets.first().value() == subPackets.last().value()) 1 else 0
                        else -> throw IllegalStateException("$typeId")
                    }
                }
            }
    }

    /**
     * @return The packet and the length of the parent string that was parsed
     */
    private fun String.extractPacketsFromBinary(): Pair<Packet, Int> {
        val version = this.take(3).toInt(2)
        var index = 3
        val typeId = this.drop(index).take(3).toInt(2)
        index += 3
        return if (typeId == 4) { // Literal Packet Type
            val literals = mutableListOf<String>()
            while (true) {
                val isFinalLiteral = this.drop(index).take(1) == "0"
                val literal = this.drop(index + 1).take(4)
                literals.add(literal)
                index += 5
                if (isFinalLiteral) break
            }
            Packet.LiteralValue(version, typeId, literals) to index
        } else { // Operation Packet Type
            val lengthIdIsOne = this.drop(index).take(1) == "1"
            index += 1

            if (lengthIdIsOne) {
                // Packet containing 'n' number of sub-packets.
                // The number of packets is known, but their length is not
                val nextLength = 11
                val subPacketLength = this.drop(index).take(nextLength).toInt(2)
                index += nextLength

                val subPackets = mutableListOf<Packet>()
                for (i in 0..<subPacketLength) {
                    val (packet, skip) = this.drop(index).extractPacketsFromBinary()
                    subPackets.add(packet)
                    index += skip
                }

                Packet.Operator(version, typeId, subPackets) to index
            } else {
                // Packet containing 'n' number of sub packets
                // The length is known, but the number of packets is not
                val nextLength = 15
                val subPacketLengthBits = this.drop(index).take(nextLength).toInt(2)
                index += nextLength

                val subPackets = mutableListOf<Packet>()
                var i = 0
                while (i < subPacketLengthBits) {
                    val (newPackets, skip) = this.drop(index + i).extractPacketsFromBinary()
                    subPackets.add(newPackets)
                    i += skip
                }
                index += subPacketLengthBits

                Packet.Operator(version, typeId, subPackets) to index
            }
        }
    }

    private fun String.extractPackets() =
        this
            .map { lookupTable[it]!! }
            .joinToString("")
            .extractPacketsFromBinary()
            .first

    override fun part1() = data.extractPackets().sumVersions()

    override fun part2() = data.extractPackets().value()
}

fun day16() {
    val day = Day16(DataFile.Part1)
    report(2021, 16, day.part1(), day.part2())
}
