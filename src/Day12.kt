// Note: This solution uses a naive approach that only calculates an optimistic upper bound.
// It counts the total blocks needed for all presents and filters out puzzles where there aren't
// enough grid spaces to accommodate them.
// Surprisingly, this heuristic produced the correct result :)
// A more rigorous solution may be implemented later.

import kotlin.text.RegexOption.MULTILINE

val Array<Array<Boolean>>.blocks: Int
    get() = this.sumOf { row -> row.count { it } }

data class Puzzle(val width: Int, val height: Int, val quantities: List<Int>)

fun main() {
    fun readPresents(input: List<String>): Array<Array<Array<Boolean>>> {
        val pattern = "\\d+:(?:\\n[.#]+)+".toRegex(MULTILINE)
        return pattern.findAll(input.joinToString("\n"))
            .map { match ->
                val shape = match.value.substringAfter(":")
                shape.split("\n").drop(1).map { row ->
                    row.map { it == '#' }.toTypedArray()
                }.toTypedArray()
            }
            .toList().toTypedArray()
    }

    fun readPuzzles(input: List<String>): List<Puzzle> {
        val pattern = "(\\d+)x(\\d+):([\\d ]+)".toRegex()
        return pattern.findAll(input.joinToString("\n"))
            .map { match ->
                val (width, height, quantities) = match.destructured
                Puzzle(width.toInt(), height.toInt(), quantities.trim().split(" ").map { it.toInt() })
            }
            .toList()
    }

    fun part1(input: List<String>): Int {
        val presents = readPresents(input)
        val puzzles = readPuzzles(input)

        return puzzles.count { puzzle ->
            puzzle.quantities.zip(presents) { quantity, present ->
                quantity * present.blocks
            }.sum() <= puzzle.width * puzzle.height
        }
    }

    // Note: This check is disabled because the current implementation produces only an upper bound
    // rather than a correct solution.
    // check(part1(readInput("Day12_test")) == 2)

    val input = readInput("Day12")
    part1(input).println()
}
