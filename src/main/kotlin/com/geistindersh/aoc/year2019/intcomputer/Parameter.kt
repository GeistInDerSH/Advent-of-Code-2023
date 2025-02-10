package com.geistindersh.aoc.year2019.intcomputer

sealed class Parameter(
    open val value: Long,
) {
    data class Position(
        override val value: Long,
    ) : Parameter(value)

    data class Immediate(
        override val value: Long,
    ) : Parameter(value)

    data class Relative(
        override val value: Long,
    ) : Parameter(value)
}
