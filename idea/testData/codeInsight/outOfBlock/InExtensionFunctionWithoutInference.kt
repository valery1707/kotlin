// TRUE

class A {
    fun foo(): Int = 12
}

fun A.bar(): Int = foo() + <caret>

// TYPE: 1
// TODO
// SKIP_ANALYZE_CHECK