package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day9(
    dataFile: DataFile,
) {
    private val data =
        fileToString(2024, 9, dataFile)
            .map(Char::digitToInt)
            .windowed(2, 2, true)
            .flatMapIndexed { idx: Int, vals: List<Int> ->
                if (vals.size == 2) {
                    listOf(DiskMap.BlockFile(idx, vals.first()), DiskMap.EmptyBlock(vals.last()))
                } else {
                    listOf(DiskMap.BlockFile(idx, vals.first()))
                }
            }.toList()

    private sealed class DiskMap(
        open val size: Int,
    ) {
        data class BlockFile(
            val id: Int,
            override val size: Int,
        ) : DiskMap(size)

        data class EmptyBlock(
            override val size: Int,
        ) : DiskMap(size)
    }

    private fun List<DiskMap>.toArray(): IntArray {
        val size = this.sumOf { it.size }
        val arr = IntArray(size) { -1 }
        var i = 0
        for (file in this) {
            when (file) {
                is DiskMap.BlockFile -> {
                    for (j in 0..<file.size) {
                        arr[i + j] = file.id
                    }
                    i += file.size
                }
                is DiskMap.EmptyBlock -> {
                    i += file.size
                }
            }
        }
        return arr
    }

    private fun IntArray.fillEmpty(): IntArray {
        var firstEmptyIndex = this.indexOfFirst { it == -1 }
        var lastFilledIndex = this.indexOfLast { it != -1 }
        while (lastFilledIndex > firstEmptyIndex && firstEmptyIndex != -1) {
            this[firstEmptyIndex] = this[lastFilledIndex]
            this[lastFilledIndex] = -1
            firstEmptyIndex = this.indexOfFirst { it == -1 }
            lastFilledIndex = this.indexOfLast { it != -1 }
        }
        return this
    }

    private fun IntArray.emptyBlockRanges(): MutableList<IntRange> {
        val emptyBlockRanges = mutableListOf<IntRange>()
        var i = 0
        while (i < this.size) {
            if (this[i] != -1) {
                i += 1
                continue
            }
            var end = i
            while (this[end] == -1) end += 1
            emptyBlockRanges.add(i..<end)
            i = end
        }
        return emptyBlockRanges
    }

    private fun List<DiskMap>.fillArrayContiguous(): IntArray {
        val arr = this.toArray()

        // Get ranges for the known empty blocks that we can later fill
        val emptyBlockRanges = arr.emptyBlockRanges()

        for (file in this.reversed()) {
            when (file) {
                is DiskMap.EmptyBlock -> continue
                is DiskMap.BlockFile -> {
                    val insertBlock = emptyBlockRanges.firstOrNull { it.count() >= file.size } ?: continue
                    val fileStart = arr.indexOfFirst { it == file.id }
                    if (insertBlock.first > fileStart) continue
                    for (i in 0..<file.size) {
                        arr[i + insertBlock.first] = file.id
                        arr[i + fileStart] = -1
                    }

                    emptyBlockRanges.remove(insertBlock)
                    if (file.size != insertBlock.count()) {
                        emptyBlockRanges.add(insertBlock.first + file.size..insertBlock.last)
                    }

                    emptyBlockRanges.add(fileStart..fileStart + file.size)
                    emptyBlockRanges.sortBy { it.first }
                }
            }
        }

        return arr
    }

    fun part1() =
        data
            .toArray()
            .fillEmpty()
            .foldIndexed(0L) { idx, acc, v ->
                if (v == -1) {
                    acc
                } else {
                    acc + (idx * v)
                }
            }

    fun part2() =
        data
            .fillArrayContiguous()
            .foldIndexed(0L) { idx, acc, v ->
                if (v == -1) {
                    acc
                } else {
                    acc + (idx * v)
                }
            }
}

fun day9() {
    val day = Day9(DataFile.Part1)
    report(2024, 9, day.part1(), day.part2())
}
