fun main() {
    fun readRanges(input: List<String>) = input.takeWhile { line -> line.isNotEmpty() }
        .map { line ->
            val (start, end) = line.split("-").map { it.toLong() }
            start..end
        }

    fun <T : Comparable<T>> addRange(ranges: List<T>, newRange: ClosedRange<T>): List<T> {
        val leftIndex = ranges.indexOfFirst { it >= newRange.start }
        val rightIndex = ranges.indexOfLast { it <= newRange.endInclusive }

        if (leftIndex < 0) return ranges + listOf(newRange.start, newRange.endInclusive)
        if (rightIndex < 0) return listOf(newRange.start, newRange.endInclusive) + ranges

        val middle = buildList {
            if (leftIndex % 2 == 0) add(newRange.start)
            if (rightIndex % 2 != 0) add(newRange.endInclusive)
        }

        return ranges.take(leftIndex) + middle + ranges.drop(rightIndex + 1)
    }

    fun part1(input: List<String>): Int {
        val ranges = readRanges(input)
        return input.dropWhile { line -> line.isNotEmpty() }
            .drop(1)
            .count { id -> ranges.any { id.toLong() in it } }
    }

    fun part2(input: List<String>) = readRanges(input)
        .fold(emptyList(), ::addRange)
        .chunked(2) { it.last() - it.first() + 1 }
        .sum()

    check(part1(readInput("Day05_test")) == 3)
    check(part2(readInput("Day05_test")) == 14L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
