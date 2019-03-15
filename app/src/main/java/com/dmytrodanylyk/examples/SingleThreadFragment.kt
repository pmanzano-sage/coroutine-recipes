package com.dmytrodanylyk.examples

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dmytrodanylyk.R
import com.dmytrodanylyk.launchRetainableTask
import com.dmytrodanylyk.log
import kotlinx.android.synthetic.main.fragment_button.*
import kotlinx.android.synthetic.main.include_long_run.*
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext


/**
 * Puedo obtener
 */
class SingleThreadFragment : Fragment(), LongRunAware {

    private val uiDispatcher: CoroutineDispatcher = Dispatchers.Main

    private val bgDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val dbDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    private lateinit var job: Job

    companion object {
        const val TAG = "SingleThreadFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun updateCounter(progress: String) {
        longRunCounter.text = progress
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_button, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button.setOnClickListener { loadData() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
    }

    private val exceptionHandler: CoroutineContext = CoroutineExceptionHandler { _, throwable ->
        showText(throwable.message ?: "")
        hideLoading()
        job = Job() // exception handler cancels job
    }

    // we can attach CoroutineExceptionHandler to parent context
    private fun loadData() = GlobalScope.launch(uiDispatcher + exceptionHandler + job) {
        showLoading()

        val result = singleThreadDispatching(bgDispatcher, dbDispatcher)
        showText(result)

        activity.launchRetainableTask("pepe")

        hideLoading()
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    private fun showText(data: String) {
        textView.text = data
    }

    private suspend fun singleThreadDispatching(ctx1: CoroutineDispatcher, ctx2: CoroutineDispatcher ) : String =
        withContext(ctx1 + CoroutineName("network")) {
            log(TAG,"Started in ctx1 - My job is ${coroutineContext[Job]}")
            withContext(ctx2 + CoroutineName("database")) {
                delay(TimeUnit.SECONDS.toMillis(1))
                log(TAG,"Working in ctx2 - My job is ${coroutineContext[Job]}")
                mayThrowException()
            }
            delay(TimeUnit.SECONDS.toMillis(1))
            log(TAG,"Back to ctx1 - My job is ${coroutineContext[Job]}")
            "returned"
        }

    private fun mayThrowException() {
        if (Random().nextBoolean()) {
            throw IllegalArgumentException("Ooops exception occurred")
        }
    }
}