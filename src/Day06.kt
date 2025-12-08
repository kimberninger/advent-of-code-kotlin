enum class Operator {
    PLUS,
    TIMES;

    companion object {
        fun parse(symbol: Char) = parse(symbol.toString())

        fun parse(symbol: String) = when (symbol) {
            "+" -> PLUS
            "*" -> TIMES
            else -> null
        }
    }

    operator fun invoke(operand: Long): (Long) -> Long = { n ->
        when (this) {
            PLUS -> operand + n
            TIMES -> operand * n
        }
    }
}

fun main() {fun part1(input: List<String>): Long {
        val operands = input.dropLast(1).map { line -> line.trim().split(Regex("\\s+")).map { it.toLong() } }
        val operators = input.last().trim().split(Regex("\\s+")).map(Operator::parse).requireNoNulls()

        val results = operands.reduce { result, row ->
            row
                .zip(operators) { operand, operator -> operator(operand)}
                .zip(result) { operation, operand -> operation(operand)}
        }

        return results.sum()
    }

    fun part2(input: List<String>): Long {
        val width = input.first().length

        val grid = input.map { line ->
            val destination = CharArray(width) { ' ' }
            line.toCharArray(destination)
        }.toTypedArray()

        val results = mutableListOf<Long>()
        val operands = mutableListOf<Long>()

        for (column in grid[0].size - 1 downTo 0) {
            var operand = 0L
            for (row in 0..<grid.size - 1) {
                grid[row][column].takeIf { it.isDigit() }?.also {
                    operand = 10 * operand + it.digitToInt()
                }
            }
            operands.add(operand)

            Operator.parse(grid.last()[column])?.also { operator ->
                val result = when (operator) {
                    Operator.PLUS -> operands.dropWhile { it == 0L }.sum()
                    Operator.TIMES -> operands.dropWhile { it == 0L }.product()
                }

                results.add(result)
                operands.clear()
            }
        }
        return results.sum()
    }

    check(part1(readInput("Day06_test")) == 4277556L)
    check(part2(readInput("Day06_test")) == 3263827L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
