package com.learn.focus.pomodoro.app.utils

open class Event<out T>(private val content: T) {

    var eventTime = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (eventTime) {
            null
        } else {
            eventTime = true
            content
        }
    }

    fun peekContent(): T = content
}