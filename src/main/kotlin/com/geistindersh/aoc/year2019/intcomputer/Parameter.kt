package com.geistindersh.aoc.year2019.intcomputer

sealed class Parameter(
    open val value: Int,
) {
    data class Position(
        override val value: Int,
    ) : Parameter(value)

    data class Immediate(
        override val value: Int,
    ) : Parameter(value)
}
