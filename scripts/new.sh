#!/bin/bash
# Configure the code for a new Advent of Code problem

YEAR=$1
DAY=$2

if [ -z "$YEAR" ] || [ -z "$DAY" ]; then
  echo "Usage: $0 <year> <day>"
  exit 1
fi
# Input Validation
if ((YEAR < 2015 || YEAR > 2024)); then
  echo "Year must be between 2015 and 2024"
  exit 1
fi
if ((DAY < 1 || DAY > 25)); then
  echo "Day must be between 1 and 25"
  exit 1
fi

# Configure the base code from a template to make it easier to start working
CODE_PATH="src/main/kotlin/com/geistindersh/aoc/year${YEAR}/Day${DAY}.kt"
if [ -f "$CODE_PATH" ]; then
  echo "File $CODE_PATH already exists"
  exit 0
fi

mkdir -pv "$(dirname "$CODE_PATH")"
cat > "$CODE_PATH" <<EOL
package com.geistindersh.aoc.year${YEAR}

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToString
import com.geistindersh.aoc.helper.report

class Day${DAY}(dataFile: DataFile) {
  private val data = fileToString(${YEAR}, ${DAY}, dataFile)

  fun part1() = 0
  fun part2() = 0
}

fun day${DAY}() {
  val day = Day${DAY}(DataFile.Part1)
  report(${YEAR}, ${DAY}, day.part1(), day.part2())
}
EOL

# Configure the unit tests, they should fail when first run
TEST_PATH="src/test/kotlin/com/geistindersh/aoc/year${YEAR}/Day${DAY}Test.kt"
mkdir -pv "$(dirname "$TEST_PATH")"
cat > "$TEST_PATH" <<EOL
package com.geistindersh.aoc.year${YEAR}

import com.geistindersh.aoc.helper.files.DataFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day${DAY}Test {

    @Test
    fun part1() {
        assertEquals(-1, Day${DAY}(DataFile.Example).part1())
        assertEquals(-1, Day${DAY}(DataFile.Part1).part1())
    }

    @Test
    fun part2() {
        assertEquals(-1, Day${DAY}(DataFile.Example).part2())
        assertEquals(-1, Day${DAY}(DataFile.Part1).part2())
    }
}
EOL

# Setup the resource files to store problems in
RESOURCE_PATH=src/main/resources/${YEAR}/${DAY}
mkdir -pv "$RESOURCE_PATH"
touch "$RESOURCE_PATH/example.txt"
touch "$RESOURCE_PATH/part_1.problem.txt"