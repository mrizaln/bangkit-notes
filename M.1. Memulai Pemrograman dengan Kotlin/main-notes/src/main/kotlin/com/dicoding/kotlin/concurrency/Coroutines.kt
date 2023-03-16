package com.dicoding.kotlin.concurrency

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

// coroutines builder
    // runBlocking  menjembatani blocking code
    // launch       no return
    // async        with return

    // launch
fun firstTry() = runBlocking {
    launch {
        delay(1000L)
        println("Coroutines!")
    }
    println("Hello,")
//    delay(2000L)
}

    // async
suspend fun getCapital() : Int {
    delay(1000L)
    println("Capital called")
    return 50000
}

suspend fun getIncome() : Int {
    delay(1000L)
    println("Income called")
    return 75000
}

fun secondTry() = runBlocking {
    val capital: Deferred<Int> = async { getCapital() }
    val income: Deferred<Int> = async { getIncome() }
    println("Please wait")
    println("Your profit is ${income.await() - capital.await()}")
}


// job dan deferred
    // job
        // hasil dari perintah asynchronous yang dijalankan
        // merepresentasikan coroutine yang sebenarnya
        // property
            // isActive
            // isCompleted
            // isCancelled
        // state
            // new - start()  - active - completed
            //     |                   - cancel()   - cancelling - cancelled
            //     - cancel() - cancelling - cancelled
            // ------------------------------------------------------------------------
            // new       : not started yet
            // active    : running
            // completed : complete
            // cancelling: time window between calling cancel() and actually cancelled
            // cancelled : complete (cancel)
        // creating new job
fun creatingNewJob() {
    // immediately started
    fun usingLaunch() = runBlocking {
        val job = launch { /*something*/ }
    }

    // immediately started
    fun usingJob() = runBlocking {
        val job = Job()
    }

    // not immediately started
    fun usingLaunchNotImmediatelyStarted() = runBlocking {
        val job = launch(start = CoroutineStart.LAZY) {
            println("Start new job!")
            delay(1000L)
            println("Job complete")
        }
        val job2 = launch(start = CoroutineStart.LAZY) {
            println("Start new job2!")
            delay(1000L)
            println("Job2 complete")
        }
        job.start()
        println("Other task")
        job2.join()
        println("Other task2")
    }
    usingLaunchNotImmediatelyStarted()
}

        // cancelling job
fun cancellingJob() = runBlocking {
    val job = launch {
        println("Start new job")
        delay(5000)
        println("Job completed")
    }
    delay(2000)
    job.cancel(cause = CancellationException("Time is up!"))
    println("Cancelling job...")
    if (job.isCancelled) {
//        println("Job is cancelled because ${job.getCancellationException().message}")
        println("Job is cancelled")
    }
}

    // deferred
        // nilai tangguhan yang dihasilkan dari proses coroutines
        // life cycle sama dengan Job
        // hasil deferred akan tersedia ketika mencapai state completed dan dapat diakses dengan fungsi await
        // async punya parameter optional seperti CoroutineStart.LAZY
        // join(), start(), await() available


// coroutine dispatcher
    // Dispatchers.Default
        // menggunakan kumpulan thread yang ada pada JVM [jumlah thread = jumlah max core]
        // digunakan oleh semua standard builders seperty launch, async, dll
    // Dispatchers.IO
        // dispatcher yang dapat digunakan untuk membongkar pemblokiran operasi I/O
        // akan menggunakan kumpulan thread yang dibuat berdasarkan permintaan
fun usingDispatcherIO() = runBlocking {
    val result = launch(Dispatchers.IO){
        TODO("To be implemented")
    }
}
    // Dispatchers.Unconfined
        // akan menjalankan coroutines pada thread yang sedang berjalan sampai mencapai titik penangguhan
        // setelah penangguhan, coroutines akan dilanjutkan pada thread di mana komputasi penangguhan yang dipanggil
fun usingDispatcherUnconfined() = runBlocking<Unit> {
    launch(Dispatchers.Unconfined) {
        println("Starting in ${Thread.currentThread().name}")
        delay(1000)
        println("Resuming in ${Thread.currentThread().name}")
    }.start()
}

    // other
        // single thread context
            // dispatcher ini menjamin bahwa setiap saat coroutine akan dijalankan pada thread yang anda tentukan
fun usingSingleThreadContext() = runBlocking<Unit> {
    val dispatcher = newSingleThreadContext("myThread")
    launch(dispatcher){
        println("Starting in ${Thread.currentThread().name}")
        delay(1000)
        println("Resuming in ${Thread.currentThread().name}")
    }.start()
}

        // thread pool
            // sebuah dispatcher yang memiliki kumpulan thread
            // ia akan memulai dan melanjutkan coroutine di salah satu thread yang tersedia pada kumpulan tersebut
            // runtime akan menentukan thread mana yang tersedia dan juga menentukan bagaimana proses distribusi bebannya
fun usingThreadPool() = runBlocking {
    val dispatcher = newFixedThreadPoolContext(3, "myPool")
    launch(dispatcher) {
        println("Starting in ${Thread.currentThread().name}")
        delay(1000)
        println("Resuming in ${Thread.currentThread().name}")
    }.start()
}


// channels
    // a way of coroutines to communicate at each other
    // channels adalah nilai deferred yang menyediakan cara mudah untuk mentransfer nilai tunggal antara coroutine
    // alih-alih memblokir sebuah thread, channels menangguhkan sebuah coroutine yang jauh lebih ringan
fun usingChannels() = runBlocking {
    val channel = Channel<Int>()
    launch(CoroutineName("v1coroutine")){
        println("Sending from ${Thread.currentThread().name}")
        for (x in 1..5)
            channel.send(x*x)
    }

    repeat(5) { println(channel.receive()) }
    println("received in ${Thread.currentThread().name}")
}

fun main() {
    firstTry()
    secondTry()

    creatingNewJob()
    cancellingJob()

    usingDispatcherUnconfined()
    usingSingleThreadContext()
    usingThreadPool()

    usingChannels()
}