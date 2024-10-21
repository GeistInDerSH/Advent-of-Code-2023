package com.geistindersh.aoc.year2015

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day8(dataFile: DataFile) {
    private val lines = fileToStream(2015, 8, dataFile).toList()

    private fun String.inMemoryLength(): Int {
        var id = 0
        var len = -2
        while (id < this.length) {
            len += 1
            if (this[id] == '\\') {
                id += if (this[id + 1] == 'x') 4 else 2
                continue
            }
            id += 1
        }
        return len
    }

    private fun String.literalLength() = this.length

    private fun String.encodeLength(): Int {
        // Skip the opening and closing '"', and account for it with base len 6 for '"\"' at the start and end
        var id = 1
        var len = 6
        while (id < this.length - 1) {
            len += 1
            if (this[id] == '\\') {
                if (this[id + 1] == 'x') {
                    len += 4
                    id += 4
                } else {
                    len += 3
                    id += 2
                }
                continue
            }
            id += 1
        }
        return len
    }

    fun part1() = lines.sumOf { it.literalLength() - it.inMemoryLength() }
    fun part2() = lines.sumOf { it.encodeLength() - it.literalLength() }
}

fun day8() {
    val day = Day8(DataFile.Part1)
    report(2015, 8, day.part1(), day.part2())
}
