package day7

import helper.DataFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day7KtTest {

    @Test
    fun part1() {
        assertEquals(part1(DataFile.Example), 6440)
        assertEquals(part1(DataFile.Part1), 251287184)
    }

    @Test
    fun part2() {
        assertEquals(part2(DataFile.Example), 5905)
        assertEquals(part2(DataFile.Part1), 250757288)
    }
}