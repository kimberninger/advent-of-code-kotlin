import kotlin.math.pow

fun main() {
    fun solution(input: List<String>, digits: Int) = input.flatMap { line ->
        var remaining = line
        digits.downTo(1).map { index ->
            val digit = remaining.dropLast(index - 1).max()
            remaining = remaining.drop(remaining.indexOf(digit) + 1)
            10.0.pow(index - 1).toLong() * digit.digitToInt()
        }
    }.sum()

    fun part1(input: List<String>) = solution(input, 2)

    fun part2(input: List<String>) = solution(input, 12)

    check(part1(readInput("Day03_test")) == 357L)
    check(part2(readInput("Day03_test")) == 3121910778619L)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
