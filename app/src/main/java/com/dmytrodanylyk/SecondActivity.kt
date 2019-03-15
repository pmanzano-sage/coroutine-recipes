package com.dmytrodanylyk

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dmytrodanylyk.examples.LongRunningJob
import kotlinx.android.synthetic.main.include_long_run.*

class SecondActivity : AppCompatActivity(), LongRunningJob.TaskCallbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }

    override fun onSynchroDone(synchro: Int) {
        log("second", "synchDone: $synchro")
        updateCounter("$synchro")
    }

    fun updateCounter(progress: String) {
        longRunCounter.text = progress
    }

    override fun onResume(){
        super.onResume()
        val app = application as MyApplication
        app.longRunningJob.setCallback(this@SecondActivity)
    }

    private fun launchLongRunningTask(){
        val app = application as MyApplication
        with(app.longRunningJob){
            setCallback(this@SecondActivity)
            launchTask()
        }
    }

    private fun cancelLongRunningTask(){
        val app = application as MyApplication
        app.longRunningJob.cancelTask()
    }

}
