package com.dmytrodanylyk.examples

interface LongRunAware {
    fun updateCounter( progress: String )
}