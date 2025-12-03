import java.util.regex.Pattern

fun main() {
    fun solution(input: List<String>, pattern: Pattern): Long {
        val matcher = pattern.asMatchPredicate()

        return input.first().split(",").sumOf { range ->
            val (first, second) = range.split("-").map { it.toLong() }

            (first..second).filter { matcher.test(it.toString()) }.sum()
        }
    }

    fun part1(input: List<String>) = solution(input, Pattern.compile("^(.*)\\1$"))

    fun part2(input: List<String>) = solution(input, Pattern.compile("^(.*)\\1+$"))

    check(part1(readInput("Day02_test")) == 1227775554L)
    check(part2(readInput("Day02_test")) == 4174379265L)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
