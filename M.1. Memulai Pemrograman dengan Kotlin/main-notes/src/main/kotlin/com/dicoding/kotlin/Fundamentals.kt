package com.dicoding.kotlin

class Fundamentals {
    // data types
    fun dataTypes() {
        /****[ var vs val ]****/
        var company: String = "Dicoding"        // mutable
        val program: String = "Bangkit"         // immutable


        /****[ char ]****/
        var vocal: Char = 'A'           // use single quotes

        // use of increment and decrement operator is allowed on Char
        println("Vocal " + vocal++)
        println("Vocal " + vocal++)
        println("Vocal " + vocal--)
        println("Vocal " + vocal--)
        println("Vocal " + vocal--)


        /****[ String ]****/
        val aString = "dsfkjhadsfp98ewhf"
        println(aString)

        // indexing is possible
        println(aString[3])

        // raw string (multi line string, kinda)
        val line = """
            line 1
            line 2
            line 3
            line 4
        """.trimIndent()
        println(line)
    }


    // functions
    class Functions {
        var user: String = "None"
        var age: Int = 0

        constructor(name: String, age: Int) {
            this.user = name
            this.age = age
        }

        fun set(name: String, age: Int) {
            user = name
        }

        fun get(): String {
            return "$user is $age year old"
        }

        fun idk() = "Random lol"    // function with single expression (returns only), return type can be inferred
    }


    // if expressions
    fun ifExpression() {
        val openHours = 21
        val now = 20

        // if expression returns a value, so it can be assigned (this is new), there's no ?: ternary operator then
        val office: String = if (now > openHours) {
            "Office already open"
        } else if (now == openHours) {
            "Wait a minute, office will be open"
        } else {
            "Office is closed"
        }

        println(office)
    }


    fun boolean() {
        // operator&&, etc
        val some = 23
        val somee = 353
        val someee = 45

        if (some > somee && !(someee == some || somee <= some)) {
            println("fdakdsjhfkadsjhf")
        }
    }


    fun numbers() {
        // int 32 bit (default for literal)
        val intNumber = 100

        // long 64 bit (has literal)
        val longNumber: Long = 2344232387623487362
        val longNumberr = 2344232387623487362L

        // short 16 bit (no literal)
        val shortNumber = 3434

        // byte 8 bit (no literal)
        val byteNumber: Byte = 123
        val byteNumberr: Byte = 0b00101000
        val byteNumberrr: Byte = 0x1f
        // val byteNumberrrr: Byte = 'c'   // can't

        // every integral type have its own unsigned counterparts
        val unsignedInt: UInt = 232334U

        // double 64 bit (default literal) [15-16 significant values]
        val doubleNumber = 233434343.4343
        val doubleNumberr = 1.27496e-120

        // float 32 bit (has literal) [6-7 significant values
        // val floatNumber: Float = 239.232    // can't
        val floatNumberr: Float = 73.124f
        val floatNumberrr = 1.23275e+24f

        // to query minimal and maximal values of every type:
        val maxInt = Int.MAX_VALUE
        val minInt = Int.MIN_VALUE

        // type conversion can be done using a respective class method of each type
        val byteNumberrrrr: Byte = 10
        val intNumberr: Int = byteNumberr.toInt()

        // readable integer
        val readableNumber: Long = 320_246_847_470_121
    }


    fun arrays() {
        // array of integer
        val array = arrayOf(1, 3, 5, 5, 75, 7, 567, 30345)
        
        // can be mixed
        val mixedArray = arrayOf(26, "owieurtg2pq3kfbj", 'c', 238UL, 1.340e-129)
        
        /* or specific type
            intArrayOf()        : IntArray
            booleanArrayOf()    : BooleanArray
            charArrayOf()       : CharArray
            longArrayOf()       : LongArray
            shortArrayOf()      : ShortArray
            byteArrayOf()       : ByteArray
         */

        // val intArray: Array<Int> = [232783, 23, 23, 23]     // can't
        val intArray  = intArrayOf(232783, 23, 23, 23)

        // indexing
        println(mixedArray[1])
        println(mixedArray.get(3))
        mixedArray[3] = 2323
    }


    fun nullableTypes() {
        // nullable types is marked using ?
        var text: String? = null
        if (text == null) {     // can't use !text
            text = "dakfjhdsafkgsaoue4f"
        }
        println(text)

        // safe calls will execute only if nullable type value is not null
        var textt: String? = null
        println(textt?.length)
        textt = "daf98hf"
        println(textt?.length)

        // elvis operator can set default value if object value is null
        val texttt: String? = null
        val textLength = texttt?.length ?: 7

        // non-null assertion
        val textttt: String? = null
        // val textlengthh = textttt!!.length      // NullPointerException if value is null
    }


    fun stringTemplate() {
        val name = "Kotlin"
        val old = 7

        // contains variable
        println("$name is $old year old now")

        val hour = 7
        // contains expression
        println("Office ${if (hour > 7) "already close" else "is open"}")
    }


    fun main() {
        dataTypes()

        val func = Functions("Maiuna", 22)
        println(func.get())
        println(func.idk())

        ifExpression()

        boolean()

        numbers()

        arrays()

        nullableTypes()

        stringTemplate()
    }
}