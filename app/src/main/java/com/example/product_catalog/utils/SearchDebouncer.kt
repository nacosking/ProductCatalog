package com.example.product_catalog.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchDebouncer(
    private val delayMillis: Long = 300,
    private val scope: CoroutineScope
) {
    private var searchJob: Job? = null

    fun debounce(action: suspend () -> Unit) {
        searchJob?.cancel()
        searchJob = scope.launch {
            delay(delayMillis)
            action()
        }
    }
}