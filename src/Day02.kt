fun main() {
    fun solution(input: List<String>, maxRepetitions: Int = 2): Long = input.first().split(",").sumOf { range ->
        val (first, second) = range.split("-").map { it.toLong() }

        (first..second).filter {
            val searchString = "$it$it".dropLast(1)
            val numberString = it.toString()
            val index = searchString.lastIndexOf(numberString, numberString.length / 2)
            index > 0 && numberString.length / (numberString.length gcd index) in 2..maxRepetitions
        }.sum()
    }

    fun part1(input: List<String>) = solution(input, 2)

    fun part2(input: List<String>) = solution(input, Int.MAX_VALUE)

    check(part1(readInput("Day02_test")) == 1227775554L)
    check(part2(readInput("Day02_test")) == 4174379265L)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
