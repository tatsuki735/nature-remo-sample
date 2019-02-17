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
import com.mizo0203.natureremoapisample.util.AppExecutors;

/**
 * ローカルネットワーク上の Nature Remo と通信します
 */
public class NatureRemoRepository {

    private static volatile NatureRemoRepository INSTANCE;

    /**
     * Local API クライアント
     * <p>
     * Local API: Nature Remo デバイスがローカルネットワークに提供するローカルの API
     * http://local.swagger.nature.global/
     */
    private final NatureRemoLocalApiClient mLocalApiClient;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private NatureRemoRepository(@NonNull AppExecutors appExecutors, @NonNull NatureRemoLocalApiClient localApiClient) {
        mAppExecutors = appExecutors;
        mLocalApiClient = localApiClient;
    }

    /**
     * @param localApiClient Local API クライアント
     *                       Local API: Nature Remo デバイスがローカルネットワークに提供するローカルの API
     */
    public static NatureRemoRepository getInstance(@NonNull AppExecutors appExecutors, @NonNull NatureRemoLocalApiClient localApiClient) {
        if (INSTANCE == null) {
            synchronized (NatureRemoRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NatureRemoRepository(appExecutors, localApiClient);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Nature Remo が受信した IR 信号を取得する
     */
    public void getMessages(@NonNull final Callback<IRSignal> callback) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mLocalApiClient.getMessages(new Callback<IRSignal>() {
                    @Override
                    public void success(final IRSignal message) {
                        mAppExecutors.mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.success(message);
                            }
                        });
                    }

                    @Override
                    public void failure() {
                        mAppExecutors.mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.failure();
                            }
                        });
                    }
                });
            }
        };

        mAppExecutors.networkIO().execute(deleteRunnable);
    }

    /**
     * Nature Remo から IR 信号を送信する
     *
     * @param message IR 信号
     */
    public void postMessages(@NonNull final IRSignal message, @NonNull final Callback<Void> callback) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mLocalApiClient.postMessages(message, new Callback<Void>() {
                    @Override
                    public void success(final Void v) {
                        mAppExecutors.mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.success(v);
                            }
                        });
                    }

                    @Override
                    public void failure() {
                        mAppExecutors.mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.failure();
                            }
                        });
                    }
                });
            }
        };

        mAppExecutors.networkIO().execute(deleteRunnable);
    }

    public interface Callback<T> {

        /**
         * Nature Remo との通信に成功した
         */
        void success(T responses);

        /**
         * Nature Remo との通信に失敗した
         */
        void failure();
    }
}
