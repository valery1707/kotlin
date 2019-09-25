// KJS_WITH_FULL_RUNTIME
// WITH_RUNTIME
import kotlin.test.*

fun box(): String {
    val sb = StringBuilder("1234")
    val result = StringBuilder()
    var ctr = 0
    for (c in sb) {
        if (ctr % 2 == 0)
            sb.deleteCharAt(ctr)
        ctr++
        result.append(c)
    }
    assertEquals("23", sb.toString())
    assertEquals("134", result.toString())

    return "OK"
}