package com.dmytrodanylyk.examples

import com.dmytrodanylyk.log
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * A coroutine scope that allows you to set a callback that will be run on each step.
 */
class LongRunningJob : CoroutineScope {
    var job = Job()
    private var count = 0

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    interface TaskCallbacks {
        fun onSynchroDone(synchro: Int)
    }

    private var mCallbacks: TaskCallbacks? = null

    fun launchTask() {
        if (count == 0) {
            this.launch {
                repeat(51) {
                    log(TAG, "${this@LongRunningJob} about to call $mCallbacks")
                    mCallbacks?.onSynchroDone(count)
                    count++
                    delay(500L)
                }
            }.invokeOnCompletion {
                count = 0
            }
        }
        else {
            log(TAG, "Not launching anything cos it's already running.")
        }
    }

    fun setCallback(callback: LongRunningJob.TaskCallbacks?) {
        mCallbacks = callback
        if( callback != null ) {
            log(TAG, "${this@LongRunningJob} attaching callback $mCallbacks")
        }
        else {
            log(TAG, "${this@LongRunningJob} detaching")
        }
    }

    fun cancelTask() {
        job.cancel()
        job = Job()
        count = 0
    }

    companion object {
        const val TAG = "LongRunningJob"
    }
}