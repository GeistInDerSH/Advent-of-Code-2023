package com.geistindersh.aoc.helper.strings

fun String.removeAll(chars: String): String {
    var result = this
    for (c in chars) {
        result = result.replace(c.toString(), "")
    }
    return result
}