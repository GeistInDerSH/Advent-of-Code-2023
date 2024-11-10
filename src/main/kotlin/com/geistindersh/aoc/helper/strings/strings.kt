package com.geistindersh.aoc.helper.strings

/**
 * Remove all instances of the [chars] from [this]
 *
 * @param chars All chars to remove
 * @return String with the chars removed
 */
fun String.removeAll(chars: String): String {
    var result = this
    for (c in chars) {
        result = result.replace(c.toString(), "")
    }
    return result
}