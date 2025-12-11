fun main() {
    fun countNeighbors(grid: List<List<Char>>, row: Int, column: Int) = (row - 1..row + 1).sumOf { i ->
        (column - 1..column + 1).count { j ->
            i in 0..<grid.size && j in 0..<grid[i].size &&
            grid[i][j] == '@' && (i != row || j != column)
        }
    }

    fun solution(input: List<String>, maxSteps: Int): Int {
        var grid = input.map { it.toList() }
        var totalRemoved = 0

        repeat(maxSteps) {
            val cellsToRemove = grid.flatMapIndexed { row, line ->
                line.mapIndexedNotNull { column, cell ->
                    (row to column).takeIf { cell == '@' && countNeighbors(grid, row, column) < 4 }
                }
            }

            if (cellsToRemove.isEmpty()) return totalRemoved

            grid = grid.mapIndexed { row, line ->
                line.mapIndexed { column, cell ->
                    if ((row to column) in cellsToRemove) '.' else cell
                }
            }

            totalRemoved += cellsToRemove.size
        }

        return totalRemoved
    }

    fun part1(input: List<String>) = solution(input, 1)

    fun part2(input: List<String>) = solution(input, Int.MAX_VALUE)

    check(part1(readInput("Day04_test")) == 13)
    check(part2(readInput("Day04_test")) == 43)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
