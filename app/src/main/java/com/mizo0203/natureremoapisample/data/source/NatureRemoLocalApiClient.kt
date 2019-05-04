/*
 * Copyright 2019, Satoki Mizoguchi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mizo0203.natureremoapisample.data.source


import android.util.Log
import androidx.annotation.NonNull
import com.mizo0203.natureremoapisample.data.IRSignal
import okhttp3.Dispatcher
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Nature Remo Local API v1.0.0
 *
 *
 * これは Remo で利用できる Local API です。
 * HTTP クライアントが Remo と同じローカルネットワーク内にある場合、
 * クライアントは Bonjour を使用して Remo の IP を検出して解決し、 IP に HTTP 要求を送信できます。
 *
 *
 * This is the local API available on Remo.
 * When the HTTP client is within the same local network with Remo,
 * the client can discover and resolve IP of Remo using Bonjour,
 * and then send a HTTP request to the IP.
 *
 *
 * http://local.swagger.nature.global/
 */
class NatureRemoLocalApiClient
/**
 * @param remoIpAddress Nature Remo の IP アドレス
 */
(@NonNull remoIpAddress: String) {
    private val mHeaders: Map<String, String>

    /**
     * Retrofit 用 Nature Remo Local API 定義インターフェイス
     */
    private val mNatureRemoLocalApiService: NatureRemoLocalApiService

    init {
        val httpUrl = HttpUrl.Builder()
                .scheme("http")
                .host(remoIpAddress)
                .build()

        val dispatcher = Dispatcher()
        dispatcher.maxRequestsPerHost = 1

        val localHttpClient = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .dispatcher(dispatcher)
                .build()

        // Add X-Requested-With header to every request to Nature Remo Local API
        mHeaders = Collections.unmodifiableMap(object : HashMap<String, String>() {
            init {
                put("X-Requested-With", X_REQUESTED_WITH)
                put("Content-Type", "application/json")
            }
        })

        val retrofit = Retrofit.Builder()
                .client(localHttpClient)
                .baseUrl(httpUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        mNatureRemoLocalApiService = retrofit.create(NatureRemoLocalApiService::class.java)
    }

    /**
     * 最新の受信 IR 信号を取得する
     *
     *
     * Fetch the newest received IR signal
     *
     * @param callback IR信号 / IR signal
     */
    /*package*/ fun getMessages(@NonNull callback: NatureRemoRepository.Callback<IRSignal>) {
        mNatureRemoLocalApiService.getMessages(mHeaders).enqueue(createCallback(callback))
    }

    /**
     * リクエストから提供された IR 信号を出力する
     *
     *
     * Emit IR signals provided by request body
     *
     * @param message  赤外線信号を記述する JSON シリアライズオブジェクト。
     * "data"、 "freq"、 "format" キーが含まれています。
     *
     *
     * JSON serialized object describing infrared signals.
     * Includes "data", “freq” and “format” keys.
     * @param callback 正常に送信されました / Successfully sent
     */
    /*package*/ fun postMessages(@NonNull message: IRSignal, @NonNull callback: NatureRemoRepository.Callback<Void>) {
        mNatureRemoLocalApiService.postMessages(mHeaders, message).enqueue(createCallback(callback))
    }

    private fun <T> createCallback(callback: NatureRemoRepository.Callback<T>): Callback<T> {
        return object : Callback<T> {
            override fun onResponse(@NonNull call: Call<T>, @NonNull response: Response<T>) {
                if (response.isSuccessful) {
                    callback.success(response.body())
                } else {
                    Log.e(TAG, "failure: message=" + response.message() + " code=" + response.code())
                    callback.failure()
                }
            }

            override fun onFailure(@NonNull call: Call<T>, @NonNull t: Throwable) {
                Log.e(TAG, "failure", t)
                callback.failure()
            }
        }
    }

    /**
     * Retrofit 用 Nature Remo Local API 定義インターフェイス
     */
    private interface NatureRemoLocalApiService {

        @GET("/messages")
        fun getMessages(@HeaderMap headers: Map<String, String>): Call<IRSignal>

        @POST("/messages")
        fun postMessages(@HeaderMap headers: Map<String, String>, @Body message: IRSignal): Call<Void>
    }

    companion object {
        private val TAG = NatureRemoLocalApiClient::class.java.simpleName
        private val X_REQUESTED_WITH = NatureRemoLocalApiClient::class.java.simpleName
    }
}
