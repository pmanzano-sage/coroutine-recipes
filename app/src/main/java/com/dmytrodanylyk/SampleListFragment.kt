package com.dmytrodanylyk

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.ListFragment
import android.widget.ArrayAdapter
import com.dmytrodanylyk.examples.*

class SampleListFragment : ListFragment() {

    companion object {
        const val TAG = "SampleListFragment"
        private const val SAMPLE_LAUNCH = "1. Launch Coroutine"
        private const val SAMPLE_SEQUENTIALLY = "2. Launch Coroutine Sequentially"
        private const val SAMPLE_PARALLEL = "3. Launch Coroutine Parallel"
        private const val SAMPLE_TIMEOUT = "4. Timeout"
        private const val SAMPLE_CANCEL = "5. Cancel"
        private const val SAMPLE_EXCEPTION = "6. Exception Handling (try/catch)"
        private const val SAMPLE_EXCEPTION_HANDLER = "6. Exception Handling (handler)"
        private const val SAMPLE_LIFECYCLE = "7. Lifecycle Awareness (LifecycleObserver)"
        private const val SAMPLE_SCOPED_FRAGMENT = "8. Lifecycle Awareness (ScopedFragment)"
        private const val SAMPLE_SINGLE_THREAD = "9. Single thread"
        private const val SAMPLE_NEW_ACTIVITY = "10. New activity"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val list = arrayListOf(
                SAMPLE_LAUNCH,
                SAMPLE_SEQUENTIALLY,
                SAMPLE_PARALLEL,
                SAMPLE_TIMEOUT,
                SAMPLE_CANCEL,
                SAMPLE_EXCEPTION,
                SAMPLE_EXCEPTION_HANDLER,
                SAMPLE_LIFECYCLE,
                SAMPLE_SCOPED_FRAGMENT,
                SAMPLE_SINGLE_THREAD,
                SAMPLE_NEW_ACTIVITY
        )
        listAdapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list)
        listView.setOnItemClickListener { _, _, position, _ ->
            when (list[position]) {
                SAMPLE_LAUNCH -> showFragment(LaunchFragment(), LaunchFragment.TAG)
                SAMPLE_SEQUENTIALLY -> showFragment(LaunchSequentiallyFragment(), LaunchSequentiallyFragment.TAG)
                SAMPLE_PARALLEL -> showFragment(LaunchParallelFragment(), LaunchParallelFragment.TAG)
                SAMPLE_TIMEOUT -> showFragment(LaunchTimeoutFragment(), LaunchTimeoutFragment.TAG)
                SAMPLE_CANCEL -> showFragment(CancelFragment(), CancelFragment.TAG)
                SAMPLE_EXCEPTION -> showFragment(ExceptionFragment(), ExceptionFragment.TAG)
                SAMPLE_EXCEPTION_HANDLER -> showFragment(ExceptionHandlerFragment(), ExceptionHandlerFragment.TAG)
                SAMPLE_LIFECYCLE -> showFragment(LifecycleAwareFragment(), LifecycleAwareFragment.TAG)
                SAMPLE_SCOPED_FRAGMENT -> showFragment(AndroidScopedFragment(), AndroidScopedFragment.TAG)
                SAMPLE_SINGLE_THREAD -> showFragment(SingleThreadFragment(), SingleThreadFragment.TAG)
                SAMPLE_NEW_ACTIVITY -> launchActivity()
            }
        }
    }


    private fun showFragment(fragment: Fragment, tag: String) {
        activity.supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }

    private fun launchActivity(){
        startActivity(Intent(activity, SecondActivity::class.java))
    }
}

