package com.geistindersh.aoc.helper.math

fun Int.positiveModulo(div: Int): Int {
    val mod = this % div
    return if (this >= 0) mod else mod + div
}

fun Long.positiveModulo(div: Long): Long {
    val mod = this % div
    return if (this >= 0) mod else mod + div
}

fun Long.positiveModulo(div: Int) = this.positiveModulo(div.toLong())
