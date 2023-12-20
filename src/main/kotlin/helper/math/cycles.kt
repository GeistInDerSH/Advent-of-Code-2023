package helper.math

import kotlin.math.max


/**
 * @param a The first value
 * @param b The second value
 * @return The least common multiple of [a] and [b]
 */
fun lcm(a: Long, b: Long): Long {
    val larger = max(a, b)
    val maxLCM = a * b
    var lcm = larger
    while (lcm <= maxLCM) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLCM
}
