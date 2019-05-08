package com.brn2393.assignment.leanagri.data.remote.factories

import com.brn2393.assignment.leanagri.BuildConfig
import com.brn2393.assignment.leanagri.data.remote.RemoteConstants
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Bhushan on 11/22/2018.
 */

object RetrofitFactory {

    val okHttpClient: OkHttpClient
        get() {
            val logging = HttpLoggingInterceptor()
            logging.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

            val builder = OkHttpClient.Builder()
                .addInterceptor(logging)
                .writeTimeout(RemoteConstants.REMOTE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(RemoteConstants.REMOTE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(RemoteConstants.REMOTE_TIMEOUT, TimeUnit.SECONDS)
            return builder.build()
        }

    val objectMapper: ObjectMapper
        get() = ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun getRetrofit(): Retrofit {
        return getRetrofit(okHttpClient, objectMapper)
    }

    fun getRetrofit(okHttpClient: OkHttpClient, objectMapper: ObjectMapper): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RemoteConstants.TMDB_ENDPOINT)
            .client(okHttpClient)
            .addCallAdapterFactory(RxErrorCallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
    }
}