package com.dicoding.kotlin

typealias Arithmetic = (Int, Int) -> Int
typealias NullableArithmetic = ((Int, Int) -> Int)?

fun Int.g_isEvenNumber() = this % 2 == 0
var g_message = "Kotlin"

class FunctionalProgramming {
    // vararg (variable argument)
        // every argument passed to these functions will be represented as Array<out T>
        // it is not allowed to have more than one vararg in one function
        // vararg parameter/arguments better off to be typed last
    fun sumNumbers(vararg number: Int): Int {
        return number.sum()
    }
    fun <T> asList(vararg input: T): List<T> {
        val result = ArrayList<T>()
        for (item in input) result.add(item)
        return result
    }


    // extension functions
    fun Int.printInt() {
        print("value $this")
    }
    fun Int.plusThree(): Int {
        return this + 3
    }

    // extension properties
    val Int.slice: Int
        get() = this / 2


    // mullable receiver
    val Int?.slice3: Int
        get() = this?.div(3) ?: 0


    // function type
    fun functionType() {
        // function with two parameter of Int and return type of Int
        // typealias Arithmetic = (Int, Int) -> Int    // nested type aliases not supported ( see top )
        val sum: Arithmetic = { valueA, valueB -> valueA + valueB }
        val multiply: Arithmetic = { valueA, valueB -> valueA * valueB }

        val sumResult = sum.invoke(10, 10)
        val multiplyResult = multiply.invoke(20, 20)
        val sumResultt = sum(10, 10)
        val multiplyResultt = multiply(20, 20)

        val nullableSum: NullableArithmetic = { valueA, valueB -> valueA + valueB }
        val nullableSumResult = nullableSum?.invoke(10,20)
        // nullableSum(12,23)      // can't
    }


    // lambda
    fun lambda() {
        val message = { println("Hello from lambda!") }
        message()

        val messagee = { message: String -> println(message) }
        messagee("dsalfhsapuef")

        val messageLength = { message: String -> message.length }
        messageLength("f803nfd")
    }


    // higher-order function
        // inline: allows you to include the entire code of a function or lambda at compile time
    inline fun printResult(value: Int, sum: (Int) -> Int) {
        val result = sum(value)
        println(result)
    }


    // lambda with receiver
        // Domain Specific Languages (DSL)
        // in a nutshell, lambda with receiver is similar to extension functions
    fun buildString(action: StringBuilder.() -> Unit): String {
        val stringBuilder = StringBuilder()
        stringBuilder.action()
        return stringBuilder.toString()
    }


    // kotlin standard library
    fun standardLibrary() {
        // standard function library: extension functions

        // scope function : let, run, with, apply, also

        // context object
            // lambda receiver (this) or lambda argument (it)

        // run [this]
        val textt = "Hello"
        val result = textt.run {
            val from = this
            val to = this.replace("Hello", "Kotlin")
            "replace text from $from to $to"
        }
        println("result: $result")

        // let [it] (test on nullable type)
        val text = "Hello"
        text.let {
            val message = "$it Kotlin"
            println(message)
        }
        text.let { value ->
            val message = "$value Kotlin"
            println(message)
        }
        val messagee: String? = null
        messagee?.let {
            val length = it.length * 2
            val text = "text length: $length"
            println(text)
        } ?: run {
            val text = "message is null"
            println(text)
        }

        // with [this] (no return is recommended?)
        val message = "Hello kotlin"
        val resultt = with(message) {
            println("value is $this")
            println("with length ${this.length}")
            "First character is ${this[0]}" +
                " and last character is ${this[this.length - 1]}"
        }
        println(resultt)

        // apply [this] (modifying)
        val buildString = StringBuilder().apply {
            // using this
            append("Hello ")
            append("Kotlin ")
        }

        // also [it] (non modifying)
        val texttt = "Hello kotlin"
        val resulttt = texttt.also {
            println("value length -> ${it.length}")
        }
        println("text -> $resulttt")
    }


    // member references
    fun count(valueA: Int, valueB: Int): Int {
        return valueA + valueB
    }
    val sum: (Int, Int) -> Int = ::count
    fun isEvenNumber(number: Int) = number % 2 == 0

    // function references
    fun functionReferences() {
        val numbers = 1..10
        val evenNumbers = numbers.filter(::isEvenNumber)
        val evenNumberss = numbers.filter(Int::g_isEvenNumber)
    }

    // property references
    fun propertyReferences() {
        println(::g_message.name)
        println(::g_message.get())

        ::g_message.set("Kotlin academy")
        println(::g_message.get())
    }


    // function inside function
    fun functionInsideFunction() {
        fun setWord(message: String) {
            fun printMessage(text: String) {
                println(text)
            }
            printMessage(message)
        }
        setWord("232ehofd78r")
    }


    // fold, drop, take, slice, distinct, chunked
    fun otherFunctions() {
        // fold         // similar to reduce? in C++
        val numbers = listOf(1, 2, 3)
        val fold = numbers.fold(10) { current, item ->
            println("current $current")
            println("item $item")
            current + item
        }
        val foldRight = numbers.foldRight(10) { item, current ->
            println("current $current")
            println("item $item")
            item + current
        }

        // drop
        val numberss = listOf(1, 2, 3, 4, 5, 6)
        val drop = numberss.drop(3)
        println(drop)
        val dropLast = numberss.dropLast(3)
        println(dropLast)

        // take -> inverse of drop

        // slice
        val total = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val slice = total .slice(3..6 step 2)
        println(slice)

        val index = listOf(2, 3, 5, 8)
        val slicee = total.slice(index)
        println(slicee)

        // distinct
        val totall = listOf(1, 2, 1, 4, 5, 3, 5, 6, 3, 1, 3, 6, 8, 3)
        val distinct = totall.distinct()
        println(distinct)

        data class Item(val key: String, val value: Any)
        val items = listOf(
            Item("1", "Kotlin"),
            Item("2", "is"),
            Item("3", "Awesome"),
            Item("3", "as"),
            Item("3", "Programming"),
            Item("3", "Language")
        )
        val distinctItems = items.distinctBy { it.key }
        distinctItems.forEach { println("${it.key} with value ${it.value}")}

        // chunked
            // sister: split() -> use regex
        val word = "QWERTYUIOP"
        val chunked = word.chunked(3) { it.toString().lowercase() }
        println(chunked)
    }


    // recursion
        // tail call recursion
    tailrec fun factorial(n: Int, result: Int = 1): Int {
        val newResult = n * result
        return if (n == 1) {
            newResult
        } else {
            factorial(n-1, newResult)
        }
    }

    fun main() {
        val number = sumNumbers(2,3,5,7,5,3,2,5,7,7,5,3,2,4,6,8,0)
        println(number)

        val list = asList(2,47,7,5,3,2,3,5,7,8,6,4,3)
        println(list)

        val numberr = sumNumbers(12,34,23, *list.toIntArray())   // spread operator
        println(numberr)

        12.printInt()
        println()

        23.plusThree().slice.printInt()
        println()

        var value: Int? = null
        println(value.slice3)
        value = 237623
        println(value.slice3)

        lambda()

        var sum: (Int) -> Int = { value -> value + value }
        printResult(121, sum)
        printResult(34){ value ->
            value + value
        }

        val message = buildString {
            append("Hello ")
            append("from ")
            append("lambda2")
        }
        println(message)

        standardLibrary()

        functionReferences()
        propertyReferences()

        functionInsideFunction()

        otherFunctions()

        println(factorial(12))
    }
}