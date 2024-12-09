package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day9(
    dataFile: DataFile,
) {
    private val blockSectors =
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

    /**
     * Convert the list of [DiskMap] into an [IntArray].
     *
     * The id of each of the files is inserted at an appropriate index to match its block
     * offset. Empty locations are denoted with a value of '-1'.
     *
     * @return The block location of each of the files
     */
    private fun List<DiskMap>.toArray(): IntArray {
        val size = this.sumOf { it.size }
        val fileArray = IntArray(size) { -1 }
        var i = 0
        for (file in this) {
            when (file) {
                is DiskMap.EmptyBlock -> {}
                is DiskMap.BlockFile -> {
                    for (j in 0..<file.size) {
                        fileArray[i + j] = file.id
                    }
                }
            }
            i += file.size
        }
        return fileArray
    }

    /**
     * Compact the array by moving the right most value(s) into the left most open spot(s).
     *
     * Note that the [IntArray] is modified in place, and the value is returned to allow for chaining.
     *
     * @return The modified IntArray
     */
    private fun IntArray.compactFragmenting(): IntArray {
        var firstEmptyIndex = this.indexOfFirst { it == -1 }
        var lastFilledIndex = this.indexOfLast { it != -1 }
        while (lastFilledIndex > firstEmptyIndex && firstEmptyIndex != -1) {
            // Move the single block
            this[firstEmptyIndex] = this[lastFilledIndex]
            this[lastFilledIndex] = -1
            firstEmptyIndex = this.indexOfFirst { it == -1 }
            lastFilledIndex = this.indexOfLast { it != -1 }
        }
        return this
    }

    /**
     * Get the start and end indices for each "blank" block of data
     *
     * @return A mutable list of the ranges
     */
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

    /**
     * Attempt to compact file locations by moving whole files into the right-most space
     * with enough contiguous memory.
     *
     * @return The compacted memory
     */
    private fun List<DiskMap>.toCompactArrayContiguous(): IntArray {
        val fileArray = this.toArray()

        // Get ranges for the known empty blocks that we can later fill
        val emptyBlockRanges = fileArray.emptyBlockRanges()

        for (file in this.reversed()) {
            when (file) {
                is DiskMap.EmptyBlock -> continue
                is DiskMap.BlockFile -> {
                    val insertBlock = emptyBlockRanges.firstOrNull { it.count() >= file.size } ?: continue
                    val fileStart = fileArray.indexOfFirst { it == file.id }
                    // Check that the first open spot is before the current file location
                    if (insertBlock.first > fileStart) continue

                    // Move the file, and clear the old position
                    for (i in 0..<file.size) {
                        fileArray[i + insertBlock.first] = file.id
                        fileArray[i + fileStart] = -1
                    }

                    // Remove, and possibly adjust the free space we just filled with the moved file
                    emptyBlockRanges.remove(insertBlock)
                    if (file.size != insertBlock.count()) {
                        emptyBlockRanges.add(insertBlock.first + file.size..insertBlock.last)
                    }

                    // Add the cleared file range, and reorder the blocks by the first index free
                    emptyBlockRanges.add(fileStart..fileStart + file.size)
                    emptyBlockRanges.sortBy { it.first }
                }
            }
        }

        return fileArray
    }

    fun part1() =
        blockSectors
            .toArray()
            .compactFragmenting()
            .foldIndexed(0L) { idx, acc, v -> if (v == -1) acc else acc + (idx * v) }

    fun part2() =
        blockSectors
            .toCompactArrayContiguous()
            .foldIndexed(0L) { idx, acc, v -> if (v == -1) acc else acc + (idx * v) }
}

fun day9() {
    val day = Day9(DataFile.Part1)
    report(2024, 9, day.part1(), day.part2())
}
