package com.geistindersh.aoc.year2024

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.ranges.size
import com.geistindersh.aoc.helper.report

class Day9(
    dataFile: DataFile,
) {
    private val blockSectors =
        fileToString(2024, 9, dataFile)
            .let {
                buildList {
                    var idx = -1
                    for (i in it.indices) {
                        val size = it[i].digitToInt()
                        val newBlock =
                            if (i % 2 == 0) {
                                idx += 1
                                BlockFile(idx, size)
                            } else {
                                BlockFile(-1, size)
                            }
                        add(newBlock)
                    }
                }
            }

    private data class BlockFile(
        val id: Int,
        val size: Int,
    )

    /**
     * Convert the list of [BlockFile] into an [IntArray].
     *
     * The id of each of the files is inserted at an appropriate index to match its block
     * offset. Empty locations are denoted with a value of '-1'.
     *
     * @return The block location of each of the files
     */
    private fun List<BlockFile>.toArray(): IntArray {
        val size = this.sumOf { it.size }
        val fileArray = IntArray(size) { -1 }
        var i = 0
        for (file in this) {
            for (j in 0..<file.size) {
                fileArray[i + j] = file.id
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
            // Loop to find the next index. Faster than calling indexOfFirst / indexOfLast
            while (this[firstEmptyIndex] != -1) firstEmptyIndex += 1
            while (this[lastFilledIndex] == -1) lastFilledIndex -= 1
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
    private fun List<BlockFile>.toCompactArrayContiguous(): IntArray {
        val fileArray = this.toArray()

        // Get ranges for the known empty blocks that we can later fill
        val emptyBlockRanges = fileArray.emptyBlockRanges()

        // Use this to track where we can still write to. This allows for us to do an early return once the
        // start of a file block is larger than this value
        var firstOpenIndex = fileArray.indexOfFirst { it == -1 }

        for (file in this.reversed()) {
            // Skip empty blocks
            if (file.id == -1) continue

            // Because the files are contiguous, we can get the final id position and remove the size of the file.
            // This is better for the common case where we are moving files from the end to the start.
            val fileStart = fileArray.lastIndexOf(file.id) - file.size + 1
            if (firstOpenIndex > fileStart) break
            // Check that the first open spot is before the current file location
            val insertBlock = emptyBlockRanges.firstOrNull { it.first < fileStart && it.size() >= file.size } ?: continue

            // Move the file, and clear the old position
            for (i in 0..<file.size) {
                fileArray[i + insertBlock.first] = file.id
                fileArray[i + fileStart] = -1
            }

            // Remove, and possibly adjust the free space we just filled with the moved file
            emptyBlockRanges.remove(insertBlock)
            if (file.size != insertBlock.size()) {
                // Insert at the sorted position. This saves needing to call emptyBlockRanges.sort
                var insertIdx = 0
                val insertRange = insertBlock.first + file.size..insertBlock.last
                while (emptyBlockRanges[insertIdx].first < insertRange.first) insertIdx += 1
                emptyBlockRanges.add(insertIdx, insertRange)
            }

            // Add the cleared file range, and reorder the blocks by the first index free
            emptyBlockRanges.add(fileStart..fileStart + file.size)

            while (fileArray[firstOpenIndex] != -1) firstOpenIndex += 1
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
