package com.brn2393.assignment.leanagri.data.remote.exceptions

import android.util.Log
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

class RetrofitException private constructor(
    private val _message: String?,
    private val _url: String?,
    private val _response: Response<*>?,
    private val _kind: Kind,
    private val _exception: Throwable?,
    private val _retrofit: Retrofit?
) : RuntimeException(_message, _exception) {

    /**
     * Identifies the event kind which triggered a [RetrofitException].
     */
    enum class Kind {
        NETWORK,
        HTTP,
        HTTP_422_WITH_DATA,
        UNEXPECTED
    }

    companion object {

        private const val TAG = "RetrofitException"
        private const val KEY_MESSAGE = "message"
        private const val NETWORK_ERROR_CONNECTION_TIMEOUT = "connection timeout."
        private const val NETWORK_ERROR_UNKNOWN = "Something went wrong. Please check your internet connection."

        fun httpError(url: String, response: Response<*>, retrofit: Retrofit): RetrofitException {
            return RetrofitException(getErrorMessage(response.errorBody()), url, response, Kind.HTTP, null, retrofit)
        }

        fun httpErrorWithObject(url: String, response: Response<*>, _retrofit: Retrofit): RetrofitException {
            val message = response.code().toString() + " " + response.message()
            val error = RetrofitException(message, url, response, Kind.HTTP_422_WITH_DATA, null, _retrofit)
            error.deserializeServerError()
            return error
        }

        fun networkError(exception: IOException): RetrofitException {
            return RetrofitException(NETWORK_ERROR_UNKNOWN, null, null, Kind.NETWORK, exception, null)
        }

        fun unexpectedError(exception: Throwable): RetrofitException {
            return RetrofitException(NETWORK_ERROR_CONNECTION_TIMEOUT, null, null, Kind.UNEXPECTED, exception, null)
        }

        private fun getErrorMessage(responseBody: ResponseBody?): String {
            var errorMessage = ""
            try {
                if (responseBody != null) {
                    val jsonObject = JSONObject(responseBody.string())
                    errorMessage = jsonObject.getString(KEY_MESSAGE)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return errorMessage
        }
    }

    private fun deserializeServerError() {
        if (_response?.errorBody() != null) {
            try {
//                _errorData = getErrorBodyAs(ServerError::class.java)
            } catch (e: IOException) {
                Log.e(TAG, "error deserialization ${e.localizedMessage}")
            }
        }
    }

    /**
     * HTTP response body converted to specified `type`. `null` if there is no
     * response.
     * @throws IOException if unable to convert the body to the specified `type`.
     */
    @Throws(IOException::class)
    fun <T> getErrorBodyAs(type: Class<T>): T? {
        if (_response?.errorBody() == null || _retrofit == null) {
            return null
        }
        val converter: Converter<ResponseBody, T> =
            _retrofit.responseBodyConverter(type, arrayOfNulls<Annotation>(0))
        return converter.convert(_response.errorBody()!!)
    }
}