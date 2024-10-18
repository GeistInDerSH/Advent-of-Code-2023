package com.geistindersh.aoc.helper.iterators

fun <T> List<T>.pairCombinations() = sequence {
    val seen = mutableSetOf<Pair<T, T>>()
    for (i in this@pairCombinations) {
        for (j in this@pairCombinations) {
            if (i == j) continue
            val p = Pair(i, j)
            val rp = Pair(j, i)
            if (p !in seen && rp !in seen) yield(p)
            seen.add(p)
            seen.add(rp)
        }
    }
}

fun <T> List<T>.cycle() = sequence {
    var i = 0
    while (true) {
        val idx = i % this@cycle.size
        yield(get(idx))
        i += 1
    }
}