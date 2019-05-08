package com.brn2393.assignment.leanagri.data.remote.factories

import android.annotation.SuppressLint
import android.util.Log
import com.brn2393.assignment.leanagri.data.remote.exceptions.RetrofitException
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.lang.reflect.Type

class RxErrorCallAdapterFactory : CallAdapter.Factory() {
    private val _original by lazy {
        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
    }

    companion object {
        fun create(): CallAdapter.Factory = RxErrorCallAdapterFactory()
    }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *> {
        val wrapped = _original.get(returnType, annotations, retrofit) as CallAdapter<out Any, *>
        return RxCallAdapterWrapper(retrofit, wrapped)
    }

    private class RxCallAdapterWrapper<R>(
        val _retrofit: Retrofit,
        val _wrappedCallAdapter: CallAdapter<R, *>
    ) : CallAdapter<R, Observable<R>> {

        override fun responseType(): Type = _wrappedCallAdapter.responseType()

        @SuppressLint("CheckResult")
        @Suppress("UNCHECKED_CAST")
        override fun adapt(call: Call<R>): Observable<R> {
            return (_wrappedCallAdapter.adapt(call) as Observable<R>)
                .onErrorResumeNext { throwable: Throwable ->
                    Observable.error(asRetrofitException(throwable))
                }
        }

        private fun asRetrofitException(throwable: Throwable): RetrofitException {
            Log.e("#", throwable.localizedMessage)
            // We had non-200 http error
            if (throwable is HttpException) {
                val response = throwable.response()

                return if (throwable.code() == 422) {
                    // on out api 422's get metadata in the response. Adjust logic here based on your needs
                    RetrofitException.httpErrorWithObject(
                        response.raw().request().url().toString(),
                        response,
                        _retrofit
                    )
                } else {
                    RetrofitException.httpError(response.raw().request().url().toString(), response, _retrofit)
                }
            }

            // A network error happened
            if (throwable is IOException) {
                return RetrofitException.networkError(throwable)
            }

            // We don't know what happened. We need to simply convert to an unknown error
            return RetrofitException.unexpectedError(throwable)
        }

    }
}