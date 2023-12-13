package day12

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12KtTest {
    private val inputFileName = "src/main/resources/day_12/part_1.txt"
    private val exampleFileName = "src/main/resources/day_12/example.txt"

    @Test
    fun part1() {
        assertEquals(part1(exampleFileName), 21)
        assertEquals(part1(inputFileName), 7922)
    }

    @Test
    fun part2() {
        assertEquals(part2(exampleFileName), 525152)
        assertEquals(part2(inputFileName), 18093821750095)
    }
}