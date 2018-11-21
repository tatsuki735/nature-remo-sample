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

import com.mizo0203.natureremoapisample.data.IRSignal;
import com.mizo0203.natureremoapisample.util.HttpUtils;
import com.mizo0203.natureremoapisample.util.JsonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

    private static final String PROTOCOL_HTTP_STR = "http";
    private static final String MESSAGES_STR = "/messages";

    /**
     * Nature Remo の IP アドレス
     */
    private final String mRemoIpAddress;

    /**
     * @param remoIpAddress Nature Remo の IP アドレス
     */
    public NatureRemoLocalApiClient(@NonNull String remoIpAddress) {
        mRemoIpAddress = remoIpAddress;
    }

    /**
     * 最新の受信 IR 信号を取得する
     * <p>
     * Fetch the newest received IR signal
     *
     * @return IR信号 / IR signal
     */
    /*package*/ IRSignal getMessages() {
        try {
            final URL url = new URL(PROTOCOL_HTTP_STR, mRemoIpAddress, MESSAGES_STR);
            final Map<String, String> reqProp = new HashMap<>();
            reqProp.put("Content-Type", "application/json");
            reqProp.put("X-Requested-With", "");
            String json = HttpUtils.get(url, reqProp);
            return JsonUtils.fromJson(json, IRSignal.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * リクエストから提供された IR 信号を出力する
     * <p>
     * Emit IR signals provided by request body
     *
     * @param message 赤外線信号を記述する JSON シリアライズオブジェクト。
     *                "data"、 "freq"、 "format" キーが含まれています。
     *                <p>
     *                JSON serialized object describing infrared signals.
     *                Includes "data", “freq” and “format” keys.
     * @return 正常に送信されました / Successfully sent
     */
    /*package*/ boolean postMessages(@NonNull IRSignal message) {
        try {
            final URL url = new URL(PROTOCOL_HTTP_STR, mRemoIpAddress, MESSAGES_STR);
            final Map<String, String> reqProp = new HashMap<>();
            reqProp.put("Content-Type", "application/json");
            reqProp.put("X-Requested-With", "");
            final String body = JsonUtils.toJson(message);
            HttpUtils.post(url, reqProp, body);
            return true; // FIXME: Check HTTP status code is equal to 200
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
