import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Computes the greatest common divisor of two natural numbers
 */
infix fun Int.gcd(other: Int): Int = when {
    other > this -> other gcd this
    other == 0 -> this
    else -> other gcd (this % other)
}

fun Iterable<Int>.product() = this.fold(1, Int::times)
fun Iterable<Long>.product() = this.fold(1, Long::times)
