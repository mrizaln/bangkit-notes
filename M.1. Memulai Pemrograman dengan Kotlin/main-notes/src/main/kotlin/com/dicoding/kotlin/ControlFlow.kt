package com.dicoding.kotlin

import kotlin.random.Random

class ControlFlow {
    enum class ColorSimple { RED, GREEN, BLUE }
    enum class Color(val value: Int) {
        RED(0xFF0000),
        GREEN(0x00FF00),
        BLUE(0x0000FF),
    }
    enum class ColorWithAnonymousClass(val value: Int) {
        RED(0xFF0000){
            override fun printValue() {
               println("value of RED is $value")
            }
        },
        GREEN(0x00FF00){
            override fun printValue() {
                println("value of GREEN is $value")
            }
        },
        BLUE(0x0000FF){
            override fun printValue() {
                println("value of BLUE is $value")
            }
        };

        abstract fun printValue()
    }
    fun enumeration() {
        val colorRed = Color.RED
        val colorGreen = Color.GREEN
        val colorBlue = Color.BLUE
        println(colorRed)
        println(colorRed.value.toString(16))
        println("${colorRed} position is ${colorRed.ordinal}")                       // C++ style enum

        val simpleColorRed = ColorSimple.RED
        println(simpleColorRed)

        val notSimpleColorRed = ColorWithAnonymousClass.RED
        notSimpleColorRed.printValue()

        // synthetic methods
        val colors: Array<Color> = Color.values()
        print("Colors: { ")
        colors.forEach { color -> print("$color ") }
        println("}")

        val colorr = Color.valueOf("RED")
        val colorrr: Color = enumValueOf("RED")
        val colorrrr = enumValueOf<Color>("RED")

        val colorss: Array<Color> = enumValues()
        val colorsss = enumValues<Color>()

        // switch case
        val colorrrrr = Color.GREEN

        when (colorrrrr){
            Color.RED   -> println("color is red")
            Color.GREEN -> println("color is green")
            Color.BLUE  -> println("color is blue")
        }
    }


    fun whenExpression() {
        // basically a swith case
        val value = 7

        // as statement
        when (value) {
            6 -> println("value is 6")
            7 -> println("value is 7")
            8 -> println("value is 8")
            else -> println("value cannot be reached")      // default branch
        }

        // as expression (else branch is mandatory here)
        val stringOfValue = when (value) {
            6    -> "value is 6"
            7    -> "value is 7"
            8    -> "value is 8"
            else -> "value cannot be reached"      // default branch
        }
        println(stringOfValue)

        // if any branch needs more than one statement, use curly braces

        // when statement/expression gives us the ability to inspect type of specific instance using [is] or [!is]
        val anyType: Any = 100L
        when (anyType) {
            is Long -> { println("the value has a Long type") }
            is String -> { println("the value has a String type") }
            else -> { println("undefined") }
        }

        // when statement/expression also able to examine range of value or Collection using [in] or [!in]
        val valuee = 27
        val ranges = 10..50     // inclusive

        when (valuee) {
            in ranges -> println("value is in the range")
            !in ranges -> println("value is outside the range")
            else -> println("value undefined")
        }

        // since kotlin 1.3, we can capture the when subject directly inside the when expression/statement
        val registerNumber = when (val regis = Random.nextInt(100)) {
            in 1..50 -> 50 * regis
            in 51..100 -> 100 * regis
            else -> regis
        }
        println(registerNumber)
    }


    fun whileExpression() {
        // while
        var counter = 1
        while (counter <= 7) {
            println("Hello, world!")
            counter++       // or ++counter (same with C++)
        }

        // do while
        var counterr = 1
        do {
            println("Hello, world!2")
            ++counterr
        } while (counterr <= 7)
    }


    fun ranges() {
        // like range() in python3
        val rangeInt = 1..10
        val rangeIntt = 1.rangeTo(10)
        val rangeInttt = 10.downTo(1)

        val rangeIntDoubleStep = 1..10 step 2
        rangeIntDoubleStep.forEach {
            print("$it ")
        }
        println()
        println(rangeIntDoubleStep.step)

        if (8 in rangeIntDoubleStep) {
            println("in range")
        } else {
            println("not in range")
        }
    }


    fun forLoop() {
        val ranges = 1..5
        for (i in ranges) {
            println("value is $i")
        }

        val rangess = 1.rangeTo(10) step 3
        for (i in rangess) {
            println("value is $i")
        }

        val rangesss = 20.downTo(1) step 2
        for ((index, value) in rangesss.withIndex()) {
            println("value $value with index $index")
        }

        val rangessss = 1..9 step 3
        rangessss.forEach { value ->
            println("value is $value")
        }

        val rangesssss = 20.downTo(0) step 4
        rangesssss.forEachIndexed { index, value ->
            println("value $value with index $index")
        }

        // break and continue (can mark from which loop we want to break or continue)
        loop@ for (i in 1..10) {
            println("Outside loop")

            for (j in 1..10) {
                println("Inside loop")
                if (j > 5) break@loop
            }
        }
    }
    fun main() {
        enumeration()
        whenExpression()
        whileExpression()
        ranges()
        forLoop()
    }
}