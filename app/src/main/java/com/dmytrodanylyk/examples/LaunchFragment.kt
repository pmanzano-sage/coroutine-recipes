package com.dmytrodanylyk.examples

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dmytrodanylyk.R
import com.dmytrodanylyk.log
import kotlinx.android.synthetic.main.fragment_button.*
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit

class LaunchFragment : Fragment(), LongRunAware {

    private val uiDispatcher: CoroutineDispatcher = Dispatchers.Main
    private val dataProvider = DataProvider()
    private lateinit var job: Job

    companion object {
        const val TAG = "LaunchFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun updateCounter(progress: String) {
        counter.text = progress
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

    private fun loadData() = GlobalScope.launch(uiDispatcher + job) {
        showLoading() // ui thread
        log(TAG, "loadData...")
        val result = dataProvider.loadData() // non ui thread, suspend until finished

        showText(result) // ui thread
        hideLoading() // ui thread
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

    class DataProvider(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

        suspend fun loadData(): String = withContext(dispatcher) {
            log(TAG, "data start")
            delay(TimeUnit.SECONDS.toMillis(2)) // imitate long running operation
            log(TAG, "data end")
            "Data is available: ${Random().nextInt()}"

        }
    }

}