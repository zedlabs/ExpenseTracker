package ml.zedlabs.expenseButler.model

/**
 * Base Class for all network response
 * error = null for Success & data = null for Error
 * Throwable is the base class for exception so network exceptions can be casted & handled
 */
sealed class Resource<T>(val data: T? = null, val error: Throwable? = null) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(error: Throwable) : Resource<T>(error = error)
    class Loading<T>: Resource<T>()
    class Uninitialised<T>: Resource<T>()
}
