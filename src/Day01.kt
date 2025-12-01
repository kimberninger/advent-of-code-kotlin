fun main() {
    fun solution(input: List<String>, initialPosition: Int = 50, dialSize: Int = 100, chunkSize: (Int) -> Int): Int {
        var position = initialPosition
        var password = 0

        for (line in input) {
            val direction = line.take(1)
            val amount = line.drop(1).toInt()

            (1..amount).chunked(chunkSize(amount)).forEach { chunk ->
                when (direction) {
                    "L" -> position = (position - chunk.size) % dialSize
                    "R" -> position = (position + chunk.size) % dialSize
                }

                if (position == 0) {
                    password += 1
                }
            }
        }
        return password
    }

    fun part1(input: List<String>): Int {
        return solution(input) { it }
    }

    fun part2(input: List<String>): Int {
        return solution(input) { 1 }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 6)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
