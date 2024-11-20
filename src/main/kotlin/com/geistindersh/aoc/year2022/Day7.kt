package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day7(dataFile: DataFile) {
    private val commands =
        fileToStream(2022, 7, dataFile)
            .map {
                val tokens = it.split(" ")
                when (val tokenType = tokens[0]) {
                    "$" -> Command(tokens[1], tokens.getOrNull(2))
                    "dir" -> Dir(tokens[1])
                    else -> File(tokenType.toInt())
                }
            }
            .toList()
            .drop(1)

    interface Cmd

    private data class Command(val name: String, val arg: String?) : Cmd

    private data class File(val size: Int) : Cmd

    private data class Dir(val name: String) : Cmd

    private data class Tree(val cwd: String, val subDirs: List<String>, val files: List<File>) : Cmd

    private fun getNewDirName(
        cwd: String,
        newDir: String,
    ): String {
        return if (newDir == "..") {
            cwd.split("/")
                .dropLast(1)
                .joinToString("/")
        } else if (cwd == "/") {
            "$cwd$newDir"
        } else {
            "$cwd/$newDir"
        }
    }

    private fun directoryWalk(): Map<String, Tree> {
        var cwd = "/"
        val dirTree = mutableMapOf<String, Tree>()
        for (cmd in commands) {
            when (cmd) {
                is Command -> {
                    if (cmd.name == "ls") {
                        continue
                    }
                    val dirName = cmd.arg ?: ""
                    cwd = getNewDirName(cwd, dirName)
                }

                is Dir -> {
                    val name = getNewDirName(cwd, cmd.name)
                    val newDir = Tree(name, mutableListOf(), mutableListOf())
                    val dirEntry: Tree = dirTree[cwd] ?: Tree(cwd, emptyList(), emptyList())
                    val updatedDir = Tree(name, dirEntry.subDirs + listOf(name), dirEntry.files)

                    dirTree[cwd] = updatedDir
                    dirTree[name] = newDir
                }

                is File -> {
                    val dirEntry = dirTree[cwd] as Tree
                    val newFiles = dirEntry.files + listOf(cmd)
                    dirTree[cwd] = Tree(cwd, dirEntry.subDirs, newFiles)
                }

                else -> throw UnknownError("Could not reach")
            }
        }

        return dirTree
    }

    private fun sumSize(
        root: Map<String, Tree>,
        dir: Tree,
    ): Int {
        var size = dir.files.sumOf { it.size }
        dir.subDirs.forEach {
            val subDir: Tree = root[it]!!
            size += sumSize(root, subDir)
        }
        return size
    }

    fun part1(): Int {
        val fs = directoryWalk()
        return fs
            .map { (_, value) -> sumSize(fs, value) }
            .filter { it < 100000 }
            .sum()
    }

    fun part2(): Int {
        val spaceNeeded = 30000000
        val fs = directoryWalk()
        val spaceUsed = sumSize(fs, fs["/"]!!)
        val spaceAvail = 70000000 - spaceUsed
        val toFree = spaceNeeded - spaceAvail
        return fs
            .map { (_, value) -> sumSize(fs, value) }
            .filter { it > toFree }
            .min()
    }
}

fun day7() {
    val day = Day7(DataFile.Part1)
    report(2022, 7, day.part1(), day.part2())
}
