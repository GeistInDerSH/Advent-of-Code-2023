package com.geistindersh.aoc.helper.enums

enum class Direction(val rowInc: Int, val colInc: Int) {
    North(-1, 0),
    South(1, 0),
    East(0, 1),
    West(0, -1);

    fun pair() = rowInc to colInc

    operator fun plus(pair: Pair<Int, Int>) = Pair(pair.first + rowInc, pair.second + colInc)

    companion object {
        fun tryFromChar(c: Char) = tryFromString(c.toString())

        fun tryFromString(s: String): Direction? {
            return when (s) {
                "E", "R", "0" -> East
                "W", "L", "2" -> West
                "N", "U", "3" -> North
                "S", "D", "1" -> South
                else -> null
            }
        }
    }
}