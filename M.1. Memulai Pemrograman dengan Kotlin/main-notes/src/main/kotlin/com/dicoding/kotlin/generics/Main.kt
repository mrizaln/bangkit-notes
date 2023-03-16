package com.dicoding.kotlin.generics

// generic class
interface List<T> {
    operator fun get(index: Int) : T
}

class LongList : List<Long> {
    override fun get(index: Int): Long {
        return this[index]
    }
}

class ArrayList<T> : List<T> {
    override fun get(index: Int): T {
        return this[index]
    }
}

// generic function
fun <T> run(input: T) : T = input

// constraint type parameter
class ListNumber<T: Number> : List<T> {
    override fun get(index: Int): T {
        return this[index]
    }
}

// variance
abstract class Vehicle (val wheel: Int)
class Car(val speed: Int) : Vehicle(4)
class MotorCycle(val speed: Int) : Vehicle(2)

    // covariant
interface Listt<out E> : Collection<E> {
    operator fun get(index: Int) : E
}

    // contravariant
interface Comparable<in T> {
    operator fun compareTo(other: T) : Int
}


fun main() {
    val longArrayList = ArrayList<Long>()

    val numbers = ListNumber<Long>()
    val numbers2 = ListNumber<Int>()
    // val numbers3 = ListNumber<String>()      // error

    // variance
    val car = Car(200)
    val motorCycle = MotorCycle(100)
    var vehicle: Vehicle = car
    vehicle = motorCycle

    val carList = listOf<Vehicle>(Car(100), Car(120))
}