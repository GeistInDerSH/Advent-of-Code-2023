package com.geistindersh.aoc.helper.math

fun combinations(options: Int, limit: Int): Set<IntArray> {
    fun MutableSet<IntArray>.populate(array: IntArray, n: Int) {
        if (n == array.size - 1) {
            this.add(array.apply { this[size - 1] = limit - (0..<n).sumOf { this[it] } })
        } else {
            val remain = limit - (0..<n).sumOf { array[it] }
            (remain downTo 0).forEach {
                array[n] = it
                this.populate(array.copyOf(), n + 1)
            }
        }
    }

    val array = IntArray(options) { 0 }
    return mutableSetOf<IntArray>().apply { this.populate(array, 0) }
}
