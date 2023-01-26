package ml.zedlabs.data.util

import ml.zedlabs.tbd.model.Resource
import retrofit2.Response

/**
 * function to handle api request errors & parse the response to a Resource object
 */
suspend fun <T: Any> handleRequest(requestFunc: suspend () -> Response<T>): Resource<T> {

    try {
        val response = requestFunc.invoke()
        response.body()?.let { res ->
            if(response.isSuccessful) {
                return Resource.Success(res)
            }
        }
        return Resource.Error(Throwable(response.message()))
    } catch (exception: Exception) {
        return Resource.Error(exception)
    }
}