package com.dmytrodanylyk


import android.support.v4.app.FragmentActivity

fun FragmentActivity.getCurrentFragment() : android.support.v4.app.Fragment? {
    val index = supportFragmentManager.backStackEntryCount - 1
    return if( index >= 0 ) {
        val backEntry = supportFragmentManager.getBackStackEntryAt(index)
        val tag = backEntry.name
        supportFragmentManager.findFragmentByTag(tag)
    }
    else null
}
