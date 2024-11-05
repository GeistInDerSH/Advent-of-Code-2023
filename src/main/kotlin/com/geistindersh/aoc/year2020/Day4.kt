package com.geistindersh.aoc.year2020

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day4(dataFile: DataFile) {
    private val passports = fileToString(2020, 4, dataFile)
        .split("\n\n")
        .map { Passport.from(it) }
        .toList()

    private data class Passport(val fields: Map<String, String>) {
        fun hasRequiredFields() = REQUIRED_FIELDS.all { it in fields }
        fun isValid(): Boolean {
            if (!hasRequiredFields()) return false

            val byr = fields["byr"]!!.toIntOrNull() ?: return false
            if (byr !in 1920..2002) return false

            val iyr = fields["iyr"]!!.toIntOrNull() ?: return false
            if (iyr !in 2010..2020) return false

            val eyr = fields["eyr"]!!.toIntOrNull() ?: return false
            if (eyr !in 2020..2030) return false

            val hgt = fields["hgt"]!!
            val hgtValue = hgt.dropLast(2).toIntOrNull() ?: return false
            val range = if (hgt.last() == 'm') 150..193 else 59..76
            if (hgtValue !in range) return false

            if (!HCL_REGEX.matches(fields["hcl"]!!)) return false
            if (fields["ecl"]!! !in EYE_COLORS) return false
            if (!PID_REGEX.matches(fields["pid"]!!)) return false
            return true
        }

        companion object {
            private val HCL_REGEX = "#[0-9a-f]{6}".toRegex()
            private val PID_REGEX = "[0-9]{9}".toRegex()
            private val EYE_COLORS = setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
            private val REQUIRED_FIELDS = listOf(
                "byr",
                "iyr",
                "eyr",
                "hgt",
                "hcl",
                "ecl",
                "pid",
            )

            fun from(str: String): Passport {
                val fields = str
                    .replace("\n", " ")
                    .replace(":", " ")
                    .split(" ")
                    .windowed(2, 2) { it.first() to it.last() }
                    .toMap()
                return Passport(fields)
            }
        }
    }

    fun part1() = passports.count { it.hasRequiredFields() }
    fun part2() = passports.count { it.isValid() }
}

fun day4() {
    val day = Day4(DataFile.Part1)
    report(2020, 4, day.part1(), day.part2())
}
