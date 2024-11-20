package com.geistindersh.aoc.helper.enums

data class Point3D(val x: Int, val y: Int, val z: Int) {
    operator fun plus(other: Point3D) = Point3D(x + other.x, y + other.y, z + other.z)

    fun neighbors() =
        (-1..1).flatMap { nx ->
            (-1..1).flatMap { ny ->
                (-1..1).mapNotNull { nz ->
                    if (nx == 0 && ny == 0 && nz == 0) {
                        null // Don't include yourself
                    } else {
                        Point3D(x + nx, y + ny, z + nz)
                    }
                }
            }
        }
}
