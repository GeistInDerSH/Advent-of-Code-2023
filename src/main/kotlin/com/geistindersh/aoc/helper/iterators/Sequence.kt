package com.geistindersh.aoc.helper.iterators

fun <T> Sequence<T>.takeWhileInclusive(predicate: (T) -> Boolean) =
    sequence {
        with(iterator()) {
            while (hasNext()) {
                val next = next()
                yield(next)
                if (!predicate(next)) break
            }
        }
    }

fun <T> Iterable<T>.takeWhileInclusive(predicate: (T) -> Boolean): Sequence<T> =
    this
        .asSequence()
        .takeWhileInclusive(predicate)
