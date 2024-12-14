package com.geistindersh.aoc.helper.math

fun Int.floorModulo(div: Int): Int {
    val r = this % div
    return if ((this xor div) < 0 && r != 0) {
        r + div
    } else {
        r
    }
}

fun Long.floorModulo(div: Long): Long {
    val r = this % div
    return if ((this xor div) < 0L && r != 0L) {
        r + div
    } else {
        r
    }
}
