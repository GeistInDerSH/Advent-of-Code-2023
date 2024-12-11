package com.geistindersh.aoc.helper.caching

/**
 * Generate a function that will keep track of the input and output of the function
 * and cache the results.
 * Repeated calls with the same arguments will return the same result, and [fn] should
 * be free of side effects.
 *
 * @param fn A function to memoize
 * @return A function that takes the same arguments as the memoized function
 */
@Suppress("unused")
fun <A, Z> memoize(fn: (A) -> Z): (A) -> Z {
    val memory = mutableMapOf<A, Z>()
    return { memory.getOrPut(it) { fn(it) } }
}

/**
 * Generate a function that will keep track of the input and output of the function
 * and cache the results.
 * Repeated calls with the same arguments will return the same result, and [fn] should
 * be free of side effects.
 *
 * @param fn A function to memoize
 * @param defaultValues Values to pre-populate the input-output cache with
 * @return A function that takes the same arguments as the memoized function
 */
@Suppress("unused")
fun <A, Z> memoize(
    fn: (A) -> Z,
    defaultValues: Map<A, Z>,
): (A) -> Z {
    val memory = defaultValues.toMutableMap()
    return { memory.getOrPut(it) { fn(it) } }
}

/**
 * Generate a function that will keep track of the input and output of the function
 * and cache the results.
 * Repeated calls with the same arguments will return the same result, and [fn] should
 * be free of side effects.
 *
 * @param fn A function to memoize
 * @return A function that takes the same arguments as the memoized function
 */
@Suppress("unused")
fun <A, B, Z> memoize(fn: (A, B) -> Z): (A, B) -> Z {
    val memory = mutableMapOf<Pair<A, B>, Z>()
    return { a, b -> memory.getOrPut(a to b) { fn(a, b) } }
}

/**
 * Generate a function that will keep track of the input and output of the function
 * and cache the results.
 * Repeated calls with the same arguments will return the same result, and [fn] should
 * be free of side effects.
 *
 * @param fn A function to memoize
 * @param defaultValues Values to pre-populate the input-output cache with
 * @return A function that takes the same arguments as the memoized function
 */
@Suppress("unused")
fun <A, B, Z> memoize(
    fn: (A, B) -> Z,
    defaultValues: Map<Pair<A, B>, Z>,
): (A, B) -> Z {
    val memory = defaultValues.toMutableMap()
    return { a, b -> memory.getOrPut(a to b) { fn(a, b) } }
}

/**
 * Generate a function that will keep track of the input and output of the function
 * and cache the results.
 * Repeated calls with the same arguments will return the same result, and [fn] should
 * be free of side effects.
 *
 * @param fn A function to memoize
 * @return A function that takes the same arguments as the memoized function
 */
@Suppress("unused")
fun <A, B, C, Z> memoize(fn: (A, B, C) -> Z): (A, B, C) -> Z {
    val memory = mutableMapOf<Triple<A, B, C>, Z>()
    return { a, b, c -> memory.getOrPut(Triple(a, b, c)) { fn(a, b, c) } }
}

/**
 * Generate a function that will keep track of the input and output of the function
 * and cache the results.
 * Repeated calls with the same arguments will return the same result, and [fn] should
 * be free of side effects.
 * @param fn A function to memoize
 * @param defaultValues Values to pre-populate the input-output cache with
 *
 * @return A function that takes the same arguments as the memoized function
 */
@Suppress("unused")
fun <A, B, C, Z> memoize(
    fn: (A, B, C) -> Z,
    defaultValues: Map<Triple<A, B, C>, Z>,
): (A, B, C) -> Z {
    val memory = defaultValues.toMutableMap()
    return { a, b, c -> memory.getOrPut(Triple(a, b, c)) { fn(a, b, c) } }
}
