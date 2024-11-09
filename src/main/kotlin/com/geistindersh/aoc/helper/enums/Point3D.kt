package com.geistindersh.aoc.helper.enums

data class Point3D(val x: Int, val y: Int, val z: Int) {
    fun neighbors() = listOf(
        this.copy(x = x + 1),
        this.copy(x = x + 1, y = y + 1),
        this.copy(x = x + 1, y = y + 1, z = z + 1),
        this.copy(x = x + 1, y = y + 1, z = z - 1),
        this.copy(x = x + 1, y = y - 1),
        this.copy(x = x + 1, y = y - 1, z = z + 1),
        this.copy(x = x + 1, y = y - 1, z = z - 1),
        this.copy(x = x + 1, z = z + 1),
        this.copy(x = x + 1, z = z - 1),
        this.copy(x = x - 1),
        this.copy(x = x - 1, y = y + 1),
        this.copy(x = x - 1, y = y + 1, z = z + 1),
        this.copy(x = x - 1, y = y + 1, z = z - 1),
        this.copy(x = x - 1, y = y - 1),
        this.copy(x = x - 1, y = y - 1, z = z + 1),
        this.copy(x = x - 1, y = y - 1, z = z - 1),
        this.copy(x = x - 1, z = z + 1),
        this.copy(x = x - 1, z = z - 1),
        this.copy(y = y + 1),
        this.copy(y = y + 1, z = z + 1),
        this.copy(y = y + 1, z = z - 1),
        this.copy(y = y - 1),
        this.copy(y = y - 1, z = z + 1),
        this.copy(y = y - 1, z = z - 1),
        this.copy(z = z + 1),
        this.copy(z = z - 1),
    )
}