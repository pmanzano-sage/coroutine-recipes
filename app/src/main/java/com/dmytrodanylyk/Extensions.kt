package com.dmytrodanylyk


import android.app.Activity
import android.os.Looper
import android.util.Log
import com.dmytrodanylyk.examples.RetainedFragment


fun logd(message: String) = Log.d("Coroutine Recipes", message)

fun getThreadMessage() = " [Is main thread ${Looper.myLooper() == Looper.getMainLooper()}] "

fun log (tag: String, msg: String) = Log.d(tag, "[${Thread.currentThread().name}] $msg")


fun Activity.addRetainableTask(fragmentTag: String) {

    var mTaskFragment = fragmentManager.findFragmentByTag(fragmentTag) as RetainedFragment?
    // If the Fragment is non-null, then it is currently being retained across a configuration change.
    if (mTaskFragment == null) {
        mTaskFragment = RetainedFragment()
        fragmentManager.beginTransaction().add(mTaskFragment, fragmentTag).commit()
    }
}

fun Activity.cancelRetainableTask(fragmentTag: String){
    (fragmentManager.findFragmentByTag(fragmentTag) as RetainedFragment?)?.cancelTask()
}

fun Activity.launchRetainableTask(fragmentTag: String){
    (fragmentManager.findFragmentByTag(fragmentTag) as RetainedFragment?)?.launchTask()
}

