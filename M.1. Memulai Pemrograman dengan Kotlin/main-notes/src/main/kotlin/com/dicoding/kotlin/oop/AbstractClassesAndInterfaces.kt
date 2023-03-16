package com.dicoding.kotlin.oop

abstract class Animal7(
    var name: String,
    var weight: Double,
    var age: Int,
    var isCarnivore: Boolean,
) {
    fun eat() = println("$name sedang makan")
    fun sleep() = println("$name sedang tidur")
}

interface IFly {
    val numberOfWings: Int
    fun fly()
}

class Bird(
    override val numberOfWings: Int
) : IFly {
    override fun fly() {
        if (numberOfWings > 0)
            println("Flying with $numberOfWings wings")
        else
            println("I'm flying without wings")
    }
}

// anonymouse class
flyWithWings(object : IFly {
    override fun fly() {
        if(numberOfWings > 0) println("Flying with $numberOfWings wings")
        else println("I'm Flying without wings")
    }
    override val numberOfWings: Int
        get() = 2
})
