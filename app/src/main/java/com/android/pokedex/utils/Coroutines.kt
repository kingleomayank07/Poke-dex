package com.android.pokedex.utils

import kotlinx.coroutines.*

object Coroutines {

    private val job: Job = Job()

    fun io(func: suspend () -> Unit): Job =
        CoroutineScope(Dispatchers.IO + job).launch { func() }

    fun main(func: suspend () -> Unit): Job =
        CoroutineScope(Dispatchers.Main + job).launch { func() }

    fun <T> async(func: suspend () -> T): T {
        return runBlocking {
            val response = async {
                func()
            }
            response.await()
        }
    }

    fun cancelCoroutine(job: Job) {
        job.cancel()
    }

}