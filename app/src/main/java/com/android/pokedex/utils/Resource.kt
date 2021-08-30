package com.android.pokedex.utils

data class Resource<T>(val status: Status, var data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> reset(): Resource<T> {
            return Resource(Status.RESET, null, null)
        }

        fun <T> forceUpdate(data: T?): Resource<T> {
            return Resource(Status.FORCE_UPDATE, data, null)
        }
    }

    /* private var hasBeenHandled = false
         private set // Allow external read but not write

     */
    /**
     * Returns the content and prevents its use again.
     *//*
    fun <T> getContentIfNotHandled(content: Resource<T>): Resource<T>? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    */
    /**
     * Returns the content, even if it's already been handled.
     *//*
    fun <T> peekContent(content: Resource<T>): Resource<T> = content*/
}