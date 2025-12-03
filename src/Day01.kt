fun interface PasswordGenerator {
    operator fun invoke(zeroHits: Int, zeroPasses: Int): Int
}

fun main() {
    fun solution(input: List<String>, initialPosition: Int = 50, dialSize: Int = 100, passwordGenerator: PasswordGenerator): Int {
        var position = initialPosition
        var password = 0

        input.forEach { line ->
            val direction = line.take(1)
            val amount = line.drop(1).toInt()

            var zeroHits = 0
            var zeroPasses = amount / dialSize

            val remainingAmount = amount % dialSize

            position = if (direction == "L") {
                if (position == remainingAmount) zeroHits++
                if (position in 1..<remainingAmount) zeroPasses++
                (position - remainingAmount).mod(dialSize)
            } else {
                if (position + remainingAmount == dialSize) zeroHits++
                if (position + remainingAmount > dialSize) zeroPasses++
                (position + remainingAmount).mod(dialSize)
            }

            password += passwordGenerator(zeroHits, zeroPasses)
        }

        return password
    }

    fun part1(input: List<String>): Int {
        return solution(input) { zeroHits, _ -> zeroHits }
    }

    fun part2(input: List<String>): Int {
        return solution(input) { zeroHits, zeroPasses -> zeroHits + zeroPasses }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 6)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
