// KJS_WITH_FULL_RUNTIME
// WITH_RUNTIME
import kotlin.test.*

fun box(): String {
    val sb = StringBuilder("1234")
    val result = StringBuilder()
    var ctr = 0
    for (c in sb) {
        if (ctr % 2 == 0)
            sb.insert(ctr, 'x')
        ctr++
        result.append(c)
    }
    assertEquals("x1x2x3x4", sb.toString())
    assertEquals("11223344", result.toString())

    return "OK"
}