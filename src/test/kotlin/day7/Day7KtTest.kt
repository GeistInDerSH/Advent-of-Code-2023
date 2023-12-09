package day7

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day7KtTest {

    @Test
    fun part1() {
        assertEquals(part1("src/main/resources/day_7/example.txt"), 6440)
        assertEquals(part1("src/main/resources/day_7/part_1.txt"), 251287184)
    }

    @Test
    fun part2() {
        assertEquals(part2("src/main/resources/day_7/example.txt"), 5905)
        assertEquals(part2("src/main/resources/day_7/part_1.txt"), 250757288)
    }
}