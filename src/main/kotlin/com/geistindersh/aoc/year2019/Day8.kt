package com.geistindersh.aoc.year2019

import com.geistindersh.aoc.helper.AoC
import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day8(
    dataFile: DataFile,
    private val width: Int = 25,
    height: Int = 6,
) : AoC<Int, String> {
    private val layers =
        fileToString(2019, 8, dataFile)
            .map { it.digitToInt() }
            .windowed(width * height, step = width * height)
            .map { Layer(it) }

    private data class Layer(
        val numbers: List<Int>,
    ) {
        val distribution = numbers.groupingBy { it }.eachCount()
        val zeros: Int = distribution.getOrDefault(0, 0)
        val ones: Int = distribution.getOrDefault(1, 0)
        val twos: Int = distribution.getOrDefault(2, 0)

        operator fun plus(layer: Layer): Layer =
            this.numbers
                .zip(layer.numbers)
                .map { (higher, lower) ->
                    when {
                        higher == 0 || higher == 1 -> higher // keep the higher layer if it's not transparent (2)
                        lower == 0 || lower == 1 -> lower // otherwise keep the lower layer
                        else -> 2 // transparent
                    }
                }.let { Layer(it) }
    }

    override fun part1() = layers.minBy { it.zeros }.let { it.ones * it.twos }

    override fun part2() =
        layers
            .reduce(Layer::plus)
            .let {
                val sb = StringBuilder()
                it.numbers.windowed(width, step = width).forEach { line ->
                    line
                        .map { n -> if (n == 1) '#' else ' ' }
                        .forEach { sb.append(it) }
                    sb.append('\n')
                }
                sb.toString()
            }
}

fun day8() {
    val day = Day8(DataFile.Part1)
    report(2019, 8, day.part1(), day.part2())
}
