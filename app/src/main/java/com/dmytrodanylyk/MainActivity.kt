package com.dmytrodanylyk

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dmytrodanylyk.examples.LongRunAware
import com.dmytrodanylyk.examples.LongRunningJob

class MainActivity : AppCompatActivity(), LongRunningJob.TaskCallbacks {

    override fun onSynchroDone(synchro: Int) {
        log("main", "synchDone: $synchro")
        val fragment = getCurrentFragment() as LongRunAware?
        fragment?.updateCounter("$synchro")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, SampleListFragment(), SampleListFragment.TAG)
                    .commitNow()
        }
    }

    override fun onResume(){
        super.onResume()
        val app = application as MyApplication
        app.longRunningJob.setCallback(this@MainActivity)
    }

    override fun onBackPressed() {
        if (!supportFragmentManager.popBackStackImmediate()) super.onBackPressed()
    }

    private fun launchLongRunningTask(){
        val app = application as MyApplication
        with(app.longRunningJob){
            setCallback(this@MainActivity)
            launchTask()
        }
    }

    private fun cancelLongRunningTask(){
        val app = application as MyApplication
        app.longRunningJob.cancelTask()
    }

}