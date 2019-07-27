
// description: `val foo = 1; val bar = foo`, so `bar` refers to `foo`
//              error has to appear after swapping the lines
// RUNTIME_WITH_FULL_JDK
// ACTIONS: 'MoveLineUp'

class <info descr="null" textAttributesKey="KOTLIN_CLASS">PartialAnalysis1A</info> {
    fun <info descr="null" textAttributesKey="KOTLIN_FUNCTION_DECLARATION">someFun1</info>(<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>: <info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info>, <info descr="null" textAttributesKey="KOTLIN_PARAMETER">separator</info>: <info descr="null" textAttributesKey="KOTLIN_CLASS">String</info> = ", ") : <info descr="null" textAttributesKey="KOTLIN_CLASS">String</info> {
        return (1..<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>).<info descr="null" textAttributesKey="KOTLIN_EXTENSION_FUNCTION_CALL">toList</info>().<info descr="null" textAttributesKey="KOTLIN_EXTENSION_FUNCTION_CALL">joinToString</info>(<info descr="null" textAttributesKey="KOTLIN_PARAMETER">separator</info>) { <info descr="null" textAttributesKey="KOTLIN_PARAMETER"><info descr="Automatically declared based on the expected type" textAttributesKey="KOTLIN_CLOSURE_DEFAULT_PARAMETER">it</info></info>.<info descr="null" textAttributesKey="KOTLIN_FUNCTION_CALL">toString</info>() }
    }

    fun <info descr="null" textAttributesKey="KOTLIN_FUNCTION_DECLARATION">someFun1</info>(<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>: <info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info>) : <info descr="null" textAttributesKey="KOTLIN_CLASS">String</info> {
        return <info descr="null" textAttributesKey="KOTLIN_FUNCTION_CALL">someFun1</info>(<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>, ",")
    }

    fun <info descr="null" textAttributesKey="KOTLIN_FUNCTION_DECLARATION">someFun2</info>(<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>: <info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info>) : <info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info> {
        return (1..<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>).<info descr="null" textAttributesKey="KOTLIN_EXTENSION_FUNCTION_CALL">sum</info>()
    }

    fun <info descr="null" textAttributesKey="KOTLIN_FUNCTION_DECLARATION">someFun3</info>(<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>: <info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info>) : <info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info> {
        return (1..<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>).<info descr="null" textAttributesKey="KOTLIN_EXTENSION_FUNCTION_CALL">fold</info>(1,  { <info descr="null" textAttributesKey="KOTLIN_PARAMETER">mul</info>, <info descr="null" textAttributesKey="KOTLIN_PARAMETER">next</info> -> <info descr="null" textAttributesKey="KOTLIN_PARAMETER">mul</info> * <info descr="null" textAttributesKey="KOTLIN_PARAMETER">next</info>})
    }

    fun <info descr="null" textAttributesKey="KOTLIN_FUNCTION_DECLARATION">someFun4</info>(<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>: <info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info>) : <info descr="null" textAttributesKey="KOTLIN_TRAIT">Map</info><<info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info>, <info descr="null" textAttributesKey="KOTLIN_CLASS">String</info>> {
        val <info descr="null" textAttributesKey="KOTLIN_LOCAL_VARIABLE">map</info> = <info descr="null" textAttributesKey="KOTLIN_PACKAGE_FUNCTION_CALL">mutableMapOf</info><<info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info>, <info descr="null" textAttributesKey="KOTLIN_CLASS">String</info>>()
        (1 .. <info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>).<info descr="null" textAttributesKey="KOTLIN_EXTENSION_FUNCTION_CALL">forEach</info> { <info descr="null" textAttributesKey="KOTLIN_PARAMETER">v</info> ->
            <info descr="null" textAttributesKey="KOTLIN_LOCAL_VARIABLE">map</info>[<info descr="null" textAttributesKey="KOTLIN_PARAMETER">v</info>] = <info descr="null" textAttributesKey="KOTLIN_PARAMETER">v</info>.<info descr="null" textAttributesKey="KOTLIN_FUNCTION_CALL">toString</info>()
        }
        return <info descr="null" textAttributesKey="KOTLIN_LOCAL_VARIABLE">map</info>.<info descr="null" textAttributesKey="KOTLIN_EXTENSION_FUNCTION_CALL">toMap</info>()
    }
}

class <info descr="null" textAttributesKey="KOTLIN_CLASS">PartialAnalysis1B</info> {
    fun <info descr="null" textAttributesKey="KOTLIN_FUNCTION_DECLARATION">someFun1</info>(<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>: <info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info>, <info descr="null" textAttributesKey="KOTLIN_PARAMETER">separator</info>: <info descr="null" textAttributesKey="KOTLIN_CLASS">String</info> = ", ") : <info descr="null" textAttributesKey="KOTLIN_CLASS">String</info> {
        return (1..<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>).<info descr="null" textAttributesKey="KOTLIN_EXTENSION_FUNCTION_CALL">toList</info>().<info descr="null" textAttributesKey="KOTLIN_EXTENSION_FUNCTION_CALL">joinToString</info>(<info descr="null" textAttributesKey="KOTLIN_PARAMETER">separator</info>) { <info descr="null" textAttributesKey="KOTLIN_PARAMETER"><info descr="Automatically declared based on the expected type" textAttributesKey="KOTLIN_CLOSURE_DEFAULT_PARAMETER">it</info></info>.<info descr="null" textAttributesKey="KOTLIN_FUNCTION_CALL">toString</info>() }
    }

    fun <info descr="null" textAttributesKey="KOTLIN_FUNCTION_DECLARATION">someFun1</info>(<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>: <info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info>) : <info descr="null" textAttributesKey="KOTLIN_CLASS">String</info> {
        return <info descr="null" textAttributesKey="KOTLIN_FUNCTION_CALL">someFun1</info>(<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>, ",")
    }

    fun <info descr="null" textAttributesKey="KOTLIN_FUNCTION_DECLARATION">someFun2</info>(<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>: <info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info>) : <info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info> {
        return (1..<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>).<info descr="null" textAttributesKey="KOTLIN_EXTENSION_FUNCTION_CALL">sum</info>()
    }

    fun <info descr="null" textAttributesKey="KOTLIN_FUNCTION_DECLARATION">someFun3</info>(<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>: <info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info>) : <info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info> {
        return (1..<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>).<info descr="null" textAttributesKey="KOTLIN_EXTENSION_FUNCTION_CALL">fold</info>(1,  { <info descr="null" textAttributesKey="KOTLIN_PARAMETER">mul</info>, <info descr="null" textAttributesKey="KOTLIN_PARAMETER">next</info> -> <info descr="null" textAttributesKey="KOTLIN_PARAMETER">mul</info> * <info descr="null" textAttributesKey="KOTLIN_PARAMETER">next</info>})
    }

    fun <info descr="null" textAttributesKey="KOTLIN_FUNCTION_DECLARATION">someFun4</info>(<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>: <info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info>) : <info descr="null" textAttributesKey="KOTLIN_TRAIT">Map</info><<info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info>, <info descr="null" textAttributesKey="KOTLIN_CLASS">String</info>> {
        val <info descr="null" textAttributesKey="KOTLIN_LOCAL_VARIABLE">map</info> = <info descr="null" textAttributesKey="KOTLIN_PACKAGE_FUNCTION_CALL">mutableMapOf</info><<info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info>, <info descr="null" textAttributesKey="KOTLIN_CLASS">String</info>>()
        (1 .. <info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>).<info descr="null" textAttributesKey="KOTLIN_EXTENSION_FUNCTION_CALL">forEach</info> { <info descr="null" textAttributesKey="KOTLIN_PARAMETER">v</info> ->
            <info descr="null" textAttributesKey="KOTLIN_LOCAL_VARIABLE">map</info>[<info descr="null" textAttributesKey="KOTLIN_PARAMETER">v</info>] = <info descr="null" textAttributesKey="KOTLIN_PARAMETER">v</info>.<info descr="null" textAttributesKey="KOTLIN_FUNCTION_CALL">toString</info>()
        }
        return <info descr="null" textAttributesKey="KOTLIN_LOCAL_VARIABLE">map</info>.<info descr="null" textAttributesKey="KOTLIN_EXTENSION_FUNCTION_CALL">toMap</info>()
    }

    fun <info descr="null" textAttributesKey="KOTLIN_FUNCTION_DECLARATION">someFunN</info>(<info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>: <info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info>) : <info descr="null" textAttributesKey="KOTLIN_TRAIT">Map</info><<info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info>, <info descr="null" textAttributesKey="KOTLIN_CLASS">String</info>> {
        val <info descr="null" textAttributesKey="KOTLIN_LOCAL_VARIABLE">foo</info> = 1<caret>
        <selection>val <warning descr="[UNUSED_VARIABLE] Variable 'bar' is never used" textAttributesKey="NOT_USED_ELEMENT_ATTRIBUTES"><info descr="null" textAttributesKey="KOTLIN_LOCAL_VARIABLE">bar</info></warning> = <info descr="null" textAttributesKey="KOTLIN_LOCAL_VARIABLE">foo</info></selection>
        val <info descr="null" textAttributesKey="KOTLIN_LOCAL_VARIABLE">map</info> = <info descr="null" textAttributesKey="KOTLIN_PACKAGE_FUNCTION_CALL">mutableMapOf</info><<info descr="null" textAttributesKey="KOTLIN_CLASS">Int</info>, <info descr="null" textAttributesKey="KOTLIN_CLASS">String</info>>()
        (1 .. <info descr="null" textAttributesKey="KOTLIN_PARAMETER">count</info>).<info descr="null" textAttributesKey="KOTLIN_EXTENSION_FUNCTION_CALL">forEach</info> { <info descr="null" textAttributesKey="KOTLIN_PARAMETER">v</info> ->
            <info descr="null" textAttributesKey="KOTLIN_LOCAL_VARIABLE">map</info>[<info descr="null" textAttributesKey="KOTLIN_PARAMETER">v</info>] = <info descr="null" textAttributesKey="KOTLIN_PARAMETER">v</info>.<info descr="null" textAttributesKey="KOTLIN_FUNCTION_CALL">toString</info>()
        }
        return <info descr="null" textAttributesKey="KOTLIN_LOCAL_VARIABLE">map</info>.<info descr="null" textAttributesKey="KOTLIN_EXTENSION_FUNCTION_CALL">toMap</info>()
    }

}

val <info descr="null" textAttributesKey="KOTLIN_CLASS">Long</info>.<info descr="null" textAttributesKey="KOTLIN_EXTENSION_PROPERTY">nsToMs</info> <info descr="null" textAttributesKey="KOTLIN_KEYWORD">get</info>() = (this * 1e-6).<info descr="null" textAttributesKey="KOTLIN_FUNCTION_CALL">toLong</info>()

val <info descr="null" textAttributesKey="KOTLIN_CLASS">Long</info>.<info descr="null" textAttributesKey="KOTLIN_EXTENSION_PROPERTY">asString</info> <info descr="null" textAttributesKey="KOTLIN_KEYWORD">get</info>() = <info descr="null" textAttributesKey="KOTLIN_FUNCTION_CALL">toString</info>()
