package com.geistindersh.aoc.year2022

import com.geistindersh.aoc.helper.files.DataFile
import com.geistindersh.aoc.helper.files.fileToStream
import com.geistindersh.aoc.helper.report

class Day7(dataFile: DataFile) {
    private val commands = fileToStream(2022, 7, dataFile)
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
        .reversed()

    interface Cmd
    private data class Command(val name: String, val arg: String?) : Cmd
    private data class File(val size: Int) : Cmd
    private data class Dir(val name: String) : Cmd
    private data class Tree(val cwd: String, val subDirs: List<String>, val files: List<File>) : Cmd

    private fun getNewDirName(cwd: String, newDir: String): String {
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

    private fun directoryWalk(
        cwd: String,
        commands: List<Cmd>,
        tree: Map<String, Tree>
    ): Pair<String, Map<String, Tree>> {
        return if (commands.size == 1) {
            when (val command = commands[0]) {
                is Command -> {
                    if (command.name == "ls") {
                        Pair(cwd, tree)
                    } else {
                        val dirName = command.arg ?: ""
                        val newDir = getNewDirName(cwd, dirName)
                        Pair(newDir, tree)
                    }
                }

                is Dir -> {
                    val name = getNewDirName(cwd, command.name)
                    val newDir = Tree(name, mutableListOf(), mutableListOf())
                    val dirEntry: Tree = tree[cwd] ?: Tree(cwd, emptyList(), emptyList())
                    val updatedDir = Tree(name, dirEntry.subDirs + listOf(name), dirEntry.files)
                    val updatedTree = tree.toMutableMap()
                    updatedTree[cwd] = updatedDir
                    updatedTree[name] = newDir
                    Pair(cwd, updatedTree.toMap())
                }

                is File -> {
                    val dirEntry = tree[cwd] as Tree
                    val newFiles = dirEntry.files + listOf(command)
                    val updatedDir = Tree(cwd, dirEntry.subDirs, newFiles)

                    val updatedTree = tree.toMutableMap()
                    updatedTree[cwd] = updatedDir
                    Pair(cwd, updatedTree.toMap())
                }

                else -> throw UnknownError("Could not reach")
            }
        } else {
            val command = commands[0]
            val (workDir, dirTree) = directoryWalk(cwd, commands.drop(1), tree)
            directoryWalk(workDir, listOf(command), dirTree)
        }
    }

    private fun sumSize(root: Map<String, Tree>, dir: Tree): Int {
        var size = dir.files.sumOf { it.size }
        dir.subDirs.forEach {
            val subDir: Tree = root[it]!!
            size += sumSize(root, subDir)
        }
        return size
    }

    fun part1(): Int {
        val fs = directoryWalk("/", commands, emptyMap()).second
        return fs
            .map { (_, value) -> sumSize(fs, value) }
            .filter { it < 100000 }
            .sum()
    }

    fun part2(): Int {
        val spaceNeeded = 30000000
        val fs = directoryWalk("/", commands, emptyMap()).second
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
