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

package com.mizo0203.natureremoapisample.util;

import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Logger;

/**
 * FIXME: HTTP Status Code
 *
 * @deprecated Retrofit に置き換えを予定
 */
public class HttpUtils {

    private static final Logger LOG = Logger.getLogger(HttpUtils.class.getName());

    public static String post(URL url, Map<String, String> reqProp, String body) {
        LOG.info("post url:     " + url);
        LOG.info("post reqProp: " + reqProp);
        LOG.info("post body:    " + body);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            for (String key : reqProp.keySet()) {
                connection.setRequestProperty(key, reqProp.get(key));
            }
            BufferedWriter writer =
                    new BufferedWriter(
                            new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
            writer.write(body);
            writer.flush();
            LOG.info("getResponseCode():    " + connection.getResponseCode());
            LOG.info("getResponseMessage(): " + connection.getResponseMessage());
            if (connection.getErrorStream() != null) {
                LOG.severe("getErrorStream(): " + IOUtils.toString(connection.getErrorStream(), StandardCharsets.UTF_8));
            }
            return IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String get(URL url, Map<String, String> reqProp) {
        LOG.info("get url:     " + url);
        LOG.info("get reqProp: " + reqProp);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            for (String key : reqProp.keySet()) {
                connection.setRequestProperty(key, reqProp.get(key));
            }
            LOG.info("getResponseCode():    " + connection.getResponseCode());
            LOG.info("getResponseMessage(): " + connection.getResponseMessage());
            if (connection.getErrorStream() != null) {
                LOG.severe("getErrorStream(): " + IOUtils.toString(connection.getErrorStream(), StandardCharsets.UTF_8));
            }
            return IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public interface Callback {

        void response(HttpURLConnection connection);
    }
}
