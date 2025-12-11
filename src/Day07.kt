fun main() {
    fun part1(input: List<String>): Int {
        val beams = input.first()
            .map { symbol -> if (symbol == 'S') 1 else 0 }
            .toMutableList()

        val rows = input.drop(1).map { line ->
            line.mapIndexedNotNull { index, symbol -> index.takeIf { symbol == '^' } }
        }

        var hits = 0
        for (splitters in rows) {
            for (splitter in splitters) {
                hits += beams[splitter]
                beams[splitter - 1] = 1
                beams[splitter + 1] = 1
                beams[splitter] = 0
            }
        }

        return hits
    }

    fun part2(input: List<String>): Long {
        val initialBeam = input.first().map { symbol -> if (symbol == 'S') 1L else 0L }

        val rows = input.drop(1).map { line ->
            line.mapIndexedNotNull { index, symbol -> index.takeIf { symbol == '^' } }
        }

        val beams = rows.fold(initialBeam) { currentBeams, splitters ->
            val newBeams = currentBeams.toMutableList()
            for (splitter in splitters) {
                newBeams[splitter - 1] += currentBeams[splitter]
                newBeams[splitter + 1] += currentBeams[splitter]
                newBeams[splitter] = 0
            }
            newBeams
        }

        return beams.sum()
    }

    check(part1(readInput("Day07_test")) == 21)
    check(part2(readInput("Day07_test")) == 40L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
