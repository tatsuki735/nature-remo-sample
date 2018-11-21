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


import com.mizo0203.natureremoapisample.data.Appliance;
import com.mizo0203.natureremoapisample.data.Signal;
import com.mizo0203.natureremoapisample.data.UserInformation;
import com.mizo0203.natureremoapisample.util.HttpUtils;
import com.mizo0203.natureremoapisample.util.JsonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Nature API v1.0.0
 * <p>
 * http://swagger.nature.global/
 */
public class NatureApiClient {

    private static final String PROTOCOL_HTTPS_STR = "https";
    private static final String BASE_URL_STR = "api.nature.global";
    private static final String USERS_ME_STR = "/1/users/me";
    private static final String APPLIANCES_STR = "/1/appliances";

    private final String mChannelAccessToken;

    public NatureApiClient(String token) {
        mChannelAccessToken = token;
    }

    /*package*/ UserInformation getUsersMe() {
        try {
            final URL url = new URL(PROTOCOL_HTTPS_STR, BASE_URL_STR, USERS_ME_STR);
            final Map<String, String> reqProp = new HashMap<>();
            reqProp.put("Content-Type", "application/json");
            reqProp.put("Authorization", "Bearer " + mChannelAccessToken);
            String json = HttpUtils.get(url, reqProp);
            return JsonUtils.fromJson(json, UserInformation.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserInformation postUsersMe(String nickname) {
        try {
            final URL url = new URL(PROTOCOL_HTTPS_STR, BASE_URL_STR, USERS_ME_STR);
            final Map<String, String> reqProp = new HashMap<>();
            reqProp.put("Content-Type", "application/json");
            reqProp.put("Authorization", "Bearer " + mChannelAccessToken);
            final String body = String.format("nickname=%s", nickname);
            String json = HttpUtils.post(url, reqProp, body);
            return JsonUtils.fromJson(json, UserInformation.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*package*/ Appliance[] getAppliances() {
        try {
            final URL url = new URL(PROTOCOL_HTTPS_STR, BASE_URL_STR, APPLIANCES_STR);
            final Map<String, String> reqProp = new HashMap<>();
            reqProp.put("Content-Type", "application/json");
            reqProp.put("Authorization", "Bearer " + mChannelAccessToken);
            String json = HttpUtils.get(url, reqProp);
            return JsonUtils.fromJson(json, Appliance[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*package*/ Signal[] getSignalsAppliance(String appliance) {
        try {
            final URL url = new URL(PROTOCOL_HTTPS_STR, BASE_URL_STR, String.format(Appliances.SIGNALS_STR, appliance));
            final Map<String, String> reqProp = new HashMap<>();
            reqProp.put("Content-Type", "application/json");
            reqProp.put("Authorization", "Bearer " + mChannelAccessToken);
            String json = HttpUtils.get(url, reqProp);
            return JsonUtils.fromJson(json, Signal[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*package*/ void postSendSignal(String signal) {
        try {
            final URL url = new URL(PROTOCOL_HTTPS_STR, BASE_URL_STR, String.format(Signals.SEND_STR, signal));
            final Map<String, String> reqProp = new HashMap<>();
            reqProp.put("Content-Type", "application/json");
            reqProp.put("Authorization", "Bearer " + mChannelAccessToken);
            HttpUtils.post(url, reqProp, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Appliances {
        private static final String SIGNALS_STR = "/1/appliances/%s/signals";

    }

    private static class Signals {
        private static final String SEND_STR = "/1/signals/%s/send";

    }
}
