package com.geistindersh.aoc.helper.enums

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Point3DTest {

    @Test
    fun neighbors() {
        val point = Point3D(0, 0, 0)
        assertEquals(point.neighbors().size, 26)
        assertEquals(
            setOf(
                Point3D(1, 0, 0),
                Point3D(1, 1, 0),
                Point3D(1, 1, 1),
                Point3D(1, 1, -1),
                Point3D(1, -1, 0),
                Point3D(1, -1, 1),
                Point3D(1, -1, -1),
                Point3D(1, 0, 1),
                Point3D(1, 0, -1),
                Point3D(-1, 0, 0),
                Point3D(-1, 1, 0),
                Point3D(-1, 1, 1),
                Point3D(-1, 1, -1),
                Point3D(-1, -1, 0),
                Point3D(-1, -1, 1),
                Point3D(-1, -1, -1),
                Point3D(-1, 0, 1),
                Point3D(-1, 0, -1),
                Point3D(0, 1, 0),
                Point3D(0, 1, 1),
                Point3D(0, 1, -1),
                Point3D(0, -1, 0),
                Point3D(0, -1, 1),
                Point3D(0, -1, -1),
                Point3D(0, 0, 1),
                Point3D(0, 0, -1),
            ),
            point.neighbors().toSet(),
        )
    }
}