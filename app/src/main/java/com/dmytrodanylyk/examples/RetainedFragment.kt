package com.dmytrodanylyk.examples

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import com.dmytrodanylyk.log
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Use [ExtensionsKt.addTaskFragment] to look for an instance of this Fragment.
 *
 * Retained fragments work fine to keep state while rotating screen. But it wont work if you launch a new activity. Since you'll get a brand new instance of the fragment:
 *
 * 2019-03-15 11:25:49.538 12392-12392/com.dmytrodanylyk D/RetainedFragment: [main] RetainedFragment{d925de1 #1 pepe} attaching callback com.dmytrodanylyk.SecondActivity@9f488ad
 * 2019-03-15 11:25:49.603 12392-12392/com.dmytrodanylyk D/RetainedFragment: [main @coroutine#2] RetainedFragment{4dc1152 #1 pepe} about to call com.dmytrodanylyk.MainActivity@6713033
 */
class RetainedFragment : Fragment(), CoroutineScope {
    var job = Job()
    private var count = 0

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    /**
     * Callback interface through which the fragment will report the
     * task's progress and results back to the Activity.
     */
    interface TaskCallbacks {
        fun onSynchroDone(synchro: Int)
    }

    private var mCallbacks: TaskCallbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }


    fun launchTask() {
        if (count == 0) {
            this.launch {
                repeat(51) {
                    log(TAG, "${this@RetainedFragment} about to call $mCallbacks")
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

    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */
    override fun onAttach(activity: Context) {
        super.onAttach(activity)
        mCallbacks = activity as RetainedFragment.TaskCallbacks
        log(TAG, "${this@RetainedFragment} attaching callback $mCallbacks")
    }

    override fun onDetach() {
        super.onDetach()
        log(TAG, "${this@RetainedFragment} detaching")
        mCallbacks = null
    }

    fun cancelTask() {
        job.cancel()
        job = Job()
        count = 0
    }

    companion object {
        const val TAG = "RetainedFragment"
    }
}