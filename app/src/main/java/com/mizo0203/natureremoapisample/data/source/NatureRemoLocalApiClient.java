/*
 * Copyright 2018, Satoki Mizoguchi
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

package com.mizo0203.natureremoapisample.data.source;


import android.support.annotation.NonNull;
import android.util.Log;

import com.mizo0203.natureremoapisample.data.IRSignal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

/**
 * Nature Remo Local API v1.0.0
 * <p>
 * これは Remo で利用できる Local API です。
 * HTTP クライアントが Remo と同じローカルネットワーク内にある場合、
 * クライアントは Bonjour を使用して Remo の IP を検出して解決し、 IP に HTTP 要求を送信できます。
 * <p>
 * This is the local API available on Remo.
 * When the HTTP client is within the same local network with Remo,
 * the client can discover and resolve IP of Remo using Bonjour,
 * and then send a HTTP request to the IP.
 * <p>
 * http://local.swagger.nature.global/
 */
public class NatureRemoLocalApiClient {
    private static final String TAG = NatureRemoLocalApiClient.class.getSimpleName();
    private static final String X_REQUESTED_WITH = NatureRemoLocalApiClient.class.getSimpleName();
    private final Map<String, String> mHeaders;

    /**
     * Retrofit 用 Nature Remo Local API 定義インターフェイス
     */
    private NatureRemoLocalApiService mNatureRemoLocalApiService;

    /**
     * @param remoIpAddress Nature Remo の IP アドレス
     */
    public NatureRemoLocalApiClient(@NonNull String remoIpAddress) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(remoIpAddress)
                .build();

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(1);

        OkHttpClient localHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .dispatcher(dispatcher)
                .build();

        // Add X-Requested-With header to every request to Nature Remo Local API
        mHeaders = Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("X-Requested-With", X_REQUESTED_WITH);
                put("Content-Type", "application/json");
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .client(localHttpClient)
                .baseUrl(httpUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mNatureRemoLocalApiService = retrofit.create(NatureRemoLocalApiService.class);
    }

    /**
     * 最新の受信 IR 信号を取得する
     * <p>
     * Fetch the newest received IR signal
     *
     * @param callback IR信号 / IR signal
     */
    /*package*/ void getMessages(@NonNull final NatureRemoRepository.Callback<IRSignal> callback) {
        mNatureRemoLocalApiService.getMessages(mHeaders).enqueue(createCallback(callback));
    }

    /**
     * リクエストから提供された IR 信号を出力する
     * <p>
     * Emit IR signals provided by request body
     *
     * @param message  赤外線信号を記述する JSON シリアライズオブジェクト。
     *                 "data"、 "freq"、 "format" キーが含まれています。
     *                 <p>
     *                 JSON serialized object describing infrared signals.
     *                 Includes "data", “freq” and “format” keys.
     * @param callback 正常に送信されました / Successfully sent
     */
    /*package*/ void postMessages(@NonNull IRSignal message, @NonNull final NatureRemoRepository.Callback<Void> callback) {
        mNatureRemoLocalApiService.postMessages(mHeaders, message).enqueue(createCallback(callback));
    }

    private <T> Callback<T> createCallback(final NatureRemoRepository.Callback<T> callback) {
        return new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                if (response.isSuccessful()) {
                    callback.success(response.body());
                } else {
                    Log.e(TAG, "failure: message=" + response.message() + " code=" + response.code());
                    callback.failure();
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                Log.e(TAG, "failure", t);
                callback.failure();
            }
        };
    }

    /**
     * Retrofit 用 Nature Remo Local API 定義インターフェイス
     */
    private interface NatureRemoLocalApiService {

        @GET("/messages")
        Call<IRSignal> getMessages(@HeaderMap Map<String, String> headers);

        @POST("/messages")
        Call<Void> postMessages(@HeaderMap Map<String, String> headers, @Body IRSignal message);
    }
}
