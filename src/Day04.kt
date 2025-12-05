fun main() {
    fun solution(input: List<String>, maxSteps: Int): Int {
        var step = 0

        val mutableInput = input.toMutableList()
        var result = 0
        var canBeRemoved = 1

        while (step++ < maxSteps && canBeRemoved > 0) {
            val cellsToRemove = mutableListOf<Pair<Int, Int>>()
            val height = mutableInput.size
            for ((row, line) in mutableInput.withIndex()) {
                val width = line.length
                for ((column, cell) in line.withIndex()) {
                    var neighbors = 0
                    for (i in row - 1..row + 1) {
                        for (j in column - 1..column + 1) {
                            if (i in 0..<height) {
                                if (j in 0..<width) {
                                    if (mutableInput[i][j] == '@' && (i != row || j != column)) {
                                        neighbors++
                                    }
                                }
                            }
                        }
                    }
                    if (cell == '@' && neighbors < 4) {
                        cellsToRemove.add(row to column)
                    }
                }
            }
            canBeRemoved = cellsToRemove.size
            for ((row, column) in cellsToRemove) {
                mutableInput[row] = mutableInput[row].replaceRange(column, column+1, ".")
            }

            cellsToRemove.clear()
            result += canBeRemoved
        }
        return result
    }

    fun part1(input: List<String>) = solution(input, 1)

    fun part2(input: List<String>) = solution(input, Int.MAX_VALUE)

    check(part1(readInput("Day04_test")) == 13)
    check(part2(readInput("Day04_test")) == 43)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
