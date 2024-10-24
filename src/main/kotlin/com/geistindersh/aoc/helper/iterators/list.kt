package com.geistindersh.aoc.helper.iterators

import com.geistindersh.aoc.helper.math.positiveModulo

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

fun <T> Collection<T>.permutations() = sequence {
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

fun List<Int>.subsetSum(target: Int): Sequence<List<Int>> = sequence {
	if (target == 0) {
		yield(emptyList())
		return@sequence
	}
	val options = this@subsetSum
	options.forEachIndexed { index, option ->
		if (option <= target) {
			this@subsetSum.subList(index + 1, options.size)
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

fun <T> List<T>.rotateRight(n: Int): List<T> {
	val new = this.toMutableList()

	this.indices.forEach { i ->
		val pos = (i - n).positiveModulo(this.size)
		new[i] = this[pos]
	}

	return new
}

fun IntArray.rotateRight(n: Int): IntArray {
	val new = this.clone()
	this.indices.forEach { i ->
		val pos = (i - n).positiveModulo(this.size)
		new[i] = this[pos]
	}
	return new
}

fun LongArray.rotateRight(n: Int): LongArray {
	val new = this.clone()
	this.indices.forEach { i ->
		val pos = (i - n).positiveModulo(this.size)
		new[i] = this[pos]
	}
	return new
}
