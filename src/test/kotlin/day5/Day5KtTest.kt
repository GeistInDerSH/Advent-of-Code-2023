package day5

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day5KtTest {
    val input = parseInput("src/main/resources/day_5/example.txt")

    @Test
    fun part1() {
        assertEquals(part1(input), 35)
    }

    @Test
    fun part2() {
        assertEquals(part2(input), 46)
    }
}