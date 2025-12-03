fun main() {
    fun solution(input: List<String>, digits: Int) = input.sumOf { line ->
        (digits downTo 1).fold(line to 0L) { (remaining, joltage), index ->
            val digit = remaining.dropLast(index - 1).max()
            val newRemaining = remaining.drop(remaining.indexOf(digit) + 1)
            newRemaining to joltage * 10 + digit.digitToInt()
        }.second
    }

    fun part1(input: List<String>) = solution(input, 2)

    fun part2(input: List<String>) = solution(input, 12)

    check(part1(readInput("Day03_test")) == 357L)
    check(part2(readInput("Day03_test")) == 3121910778619L)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
