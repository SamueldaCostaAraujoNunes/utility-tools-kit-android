package com.samuelnunes.utility_tool_kit.events

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    fun resetEvent() {
        hasBeenHandled = false
    }
    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

open class LiveEvent<T>: LiveData<Event<T>>()

open class MutableLiveEvent<T>: LiveEvent<T>() {
    fun resetEvent() = value?.resetEvent()
    fun peekContent(): T? = value?.peekContent()

    var event: T?
        get() = value?.peekContent()
        set(value) {
            if(value != null) {
                postValue(Event(value))
            } else {
                postValue(null)
            }
        }
}

fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, observer: Observer<T>) =
    observe(owner, EventObserver { observer.onChanged(it) })

class EventObserver<T>(private val onEventUnhandled: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let { value -> onEventUnhandled(value) }
    }
}