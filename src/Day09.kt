import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Point(val x: Long, val y: Long) {
    companion object {
        fun parse(coordinates: String): Point {
            val (x, y) = coordinates.split(",").map { it.toLong() }
            return Point(x, y)
        }
    }
}

sealed interface Line {
    infix fun intersects(other: Line): Boolean
    operator fun contains(point: Point): Boolean

    companion object {
        fun connect(start: Point, end: Point) = when {
            start.x == end.x -> VerticalLine(start.x, min(start.y, end.y), max(start.y, end.y))
            start.y == end.y -> HorizontalLine(start.y, min(start.x, end.x), max(start.x, end.x))
            else -> throw IllegalArgumentException("points are connected by a diagonal line $start, $end")
        }
    }
}

data class HorizontalLine(val y: Long, val startX: Long, val endX: Long) : Line {
    override infix fun intersects(other: Line) = when (other) {
        is HorizontalLine -> y == other.y && startX <= other.endX && other.startX <= endX
        is VerticalLine -> y in other.startY..other.endY && other.x in startX..endX
    }

    override fun contains(point: Point) = point.y == y && point.x in startX..endX
}

data class VerticalLine(val x: Long, val startY: Long, val endY: Long) : Line {
    override infix fun intersects(other: Line) = when (other) {
        is HorizontalLine -> x in other.startX..other.endX && other.y in startY..endY
        is VerticalLine -> x == other.x && startY <= other.endY && other.startY <= endY
    }

    override fun contains(point: Point) = point.x == x && point.y in startY..endY
}

data class Rectangle(val corner1: Point, val corner2: Point) {
    val corners = listOf(
        corner1,
        corner2,
        Point(corner1.x, corner2.y),
        Point(corner2.x, corner1.y),
    )

    val borders = listOf(
        Line.connect(corners[0], corners[2]),
        Line.connect(corners[0], corners[3]),
        Line.connect(corners[1], corners[2]),
        Line.connect(corners[1], corners[3]),
    )

    val area = (abs(corner1.x - corner2.x) + 1) * (abs(corner1.y - corner2.y) + 1)

    operator fun contains(point: Point) = point.x in min(corner1.x, corner2.x)..max(corner1.x, corner2.x) &&
            point.y in min(corner1.y, corner2.y)..max(corner1.y, corner2.y)
}

data class Polygon(val vertices: List<Point>) {
    operator fun contains(point: Point) = lines.any { point in it } || horizontalIntersections(point) % 2 == 1

    operator fun contains(rectangle: Rectangle) =
        rectangle.corners.all(::contains) && rectangle.borders.all { border ->
            lines.all { line ->
                !(border intersects line) || border.corners.any { it in line } || line.corners.any { it in border }
            }
        }

    private val Line.corners: List<Point>
        get() = when (this) {
            is HorizontalLine -> listOf(Point(startX, y), Point(endX, y))
            is VerticalLine -> listOf(Point(x, startY), Point(x, endY))
        }

    private fun horizontalIntersections(point: Point): Int {
        val horizontalLine = HorizontalLine(point.y, point.x, boundingBox.corner2.x)
        return lines.count { line ->
            line is VerticalLine && horizontalLine intersects line && line.startY != horizontalLine.y
        }
    }

    val boundingBox = Rectangle(
        Point(vertices.minOf { it.x }, vertices.minOf { it.y }),
        Point(vertices.maxOf { it.x }, vertices.maxOf { it.y }),
    )

    val lines = (vertices + listOf(vertices[0]))
        .windowed(2)
        .map { (start, end) -> Line.connect(start, end) }
}

fun main() {
    fun part1(input: List<String>): Long {
        val corners = input.map(Point::parse)

        val rectangles = corners.flatMap { (x1, y1) ->
            corners.map { (x2, y2) ->
                Rectangle(Point(x1, y1), Point(x2, y2))
            }
        }

        return rectangles.maxOf { it.area }
    }

    fun part2(input: List<String>): Long {
        val corners = input.map(Point::parse)

        val polygon = Polygon(corners)

        val rectangles = corners.flatMap { (x1, y1) ->
            corners.map { (x2, y2) ->
                Rectangle(Point(x1, y1), Point(x2, y2))
            }
        }

        return rectangles.filter { it in polygon }.maxOf { it.area }
    }

    check(part1(readInput("Day09_test")) == 50L)
    check(part2(readInput("Day09_test")) == 24L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
