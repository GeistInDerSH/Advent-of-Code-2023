package com.geistindersh.aoc.helper.iterators

import com.geistindersh.aoc.helper.math.floorModulo

fun <T> Collection<T>.pairCombinations() =
    sequence {
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

/**
 * Generate all possible pairs of the given collection, including duplicates
 * @return A List of Pairs of the items in the collection
 */
fun <T> Collection<T>.generatePairs() = this.flatMap { i -> this.map { j -> Pair(i, j) } }

fun <T> List<T>.pairCombinationsNonInvertible() =
    sequence {
        val seen = mutableSetOf<Pair<T, T>>()
        for (i in this@pairCombinationsNonInvertible) {
            for (j in this@pairCombinationsNonInvertible) {
                if (i == j) continue
                val p = Pair(i, j)
                if (p !in seen) yield(p)
                seen.add(p)
            }
        }
    }

fun <T> List<T>.cycle() =
    sequence {
        var i = 0
        while (true) {
            val idx = i % this@cycle.size
            yield(get(idx))
            i += 1
        }
    }

fun <T> Collection<T>.permutations() =
    sequence {
        val set = this@permutations.toSet()
        if (set.isEmpty()) yieldAll(emptyList())

        fun <T> allPermutationsInt(list: List<T>): Set<List<T>> {
            if (list.isEmpty()) return setOf(emptyList())

            val result: MutableSet<List<T>> = mutableSetOf()
            for (i in list.indices) {
                allPermutationsInt(list - list[i]).forEach { item ->
                    result.add(item + list[i])
                }
            }
            return result
        }

        for (lst in allPermutationsInt(set.toList())) {
            yield(lst)
        }
    }

fun List<Int>.subsetSum(target: Int): Sequence<List<Int>> =
    sequence {
        if (target == 0) {
            yield(emptyList())
            return@sequence
        }
        val options = this@subsetSum
        options.forEachIndexed { index, option ->
            if (option <= target) {
                this@subsetSum
                    .subList(index + 1, options.size)
                    .subsetSum(target - option)
                    .forEach { yield(listOf(option) + it) }
            }
        }
    }

fun <T> List<T>.rotateLeft(n: Int): List<T> {
    val new = this.toMutableList()

    this.indices.forEach { i ->
        new[i] = this[(i + n) % this.size]
    }

    return new
}

@Suppress("unused")
fun IntArray.rotateLeft(n: Int): IntArray {
    val new = this.clone()
    this.indices.forEach { i ->
        new[i] = this[(i + n) % this.size]
    }
    return new
}

fun LongArray.rotateLeft(n: Int): LongArray {
    val new = this.clone()
    this.indices.forEach { i ->
        new[i] = this[(i + n) % this.size]
    }
    return new
}

@Suppress("unused")
fun <T> List<T>.rotateRight(n: Int): List<T> {
    val new = this.toMutableList()

    this.indices.forEach { i ->
        val pos = (i - n).floorModulo(this.size)
        new[i] = this[pos]
    }

    return new
}

@Suppress("unused")
fun IntArray.rotateRight(n: Int): IntArray {
    val new = this.clone()
    this.indices.forEach { i ->
        val pos = (i - n).floorModulo(this.size)
        new[i] = this[pos]
    }
    return new
}

@Suppress("unused")
fun LongArray.rotateRight(n: Int): LongArray {
    val new = this.clone()
    this.indices.forEach { i ->
        val pos = (i - n).floorModulo(this.size)
        new[i] = this[pos]
    }
    return new
}
