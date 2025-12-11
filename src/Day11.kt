fun main() {
    fun parseGraph(input: List<String>): Map<String, List<String>> = input.associate { line ->
        val (start, destinations) = line.split(": ")
        start to destinations.split(" ")
    }

    fun <T> Map<T, List<T>>.countPaths(
        from: T,
        to: T,
        visiting: Set<T> = emptySet(),
        memoizedPaths: MutableMap<Pair<T, Set<T>>, Long> = mutableMapOf(),
    ): Long = memoizedPaths[from to visiting] ?: when (from) {
        to if visiting.isEmpty() -> 1
        to -> 0
        else -> this[from]?.sumOf { nextNode ->
            this.countPaths(nextNode, to, visiting - from, memoizedPaths)
        } ?: 0
    }.also { memoizedPaths[from to visiting] = it }

    fun part1(input: List<String>) =
        parseGraph(input).countPaths(from = "you", to = "out")

    fun part2(input: List<String>) =
        parseGraph(input).countPaths(from = "svr", to = "out", visiting = setOf("fft", "dac"))

    check(part1(readInput("Day11_test_part1")) == 5L)
    check(part2(readInput("Day11_test_part2")) == 2L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
