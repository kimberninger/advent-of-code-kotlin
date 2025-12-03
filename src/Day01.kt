fun interface PasswordGenerator {
    operator fun invoke(zeroHits: Int, zeroPasses: Int): Int
}

fun main() {
    fun updatePosition(dialSize: Int, position: Int, direction: Char, amount: Int): Triple<Int, Int, Int> {
        val turningFactor = if (direction == 'L') -1 else 1
        val newPosition = (position + turningFactor * amount).mod(dialSize)

        val zeroHits = when (direction) {
            'L' if position == amount -> 1
            'R' if position + amount == dialSize -> 1
            else -> 0
        }

        val zeroPasses = when (direction) {
            'L' if position in 1..<amount -> 1
            'R' if position + amount > dialSize -> 1
            else -> 0
        }

        return Triple(newPosition, zeroHits, zeroPasses)
    }

    fun solution(input: List<String>, initialPosition: Int = 50, dialSize: Int = 100, passwordGenerator: PasswordGenerator) =
        input.fold(initialPosition to 0) { (position, password), line ->
            val direction = line[0]
            val amount = line.drop(1).toInt()

            val fullRotations = amount / dialSize
            val remainingAmount = amount % dialSize

            val (newPosition, zeroHits, zeroPasses) = updatePosition(dialSize, position, direction, remainingAmount)

            newPosition to password + passwordGenerator(zeroHits, fullRotations + zeroPasses)
        }.second

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
