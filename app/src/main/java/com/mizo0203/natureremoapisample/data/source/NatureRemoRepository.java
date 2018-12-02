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

import com.mizo0203.natureremoapisample.data.Appliance;
import com.mizo0203.natureremoapisample.data.IRSignal;
import com.mizo0203.natureremoapisample.data.Signal;
import com.mizo0203.natureremoapisample.data.UserInformation;
import com.mizo0203.natureremoapisample.util.AppExecutors;

public class NatureRemoRepository {

    private static volatile NatureRemoRepository INSTANCE;

    /**
     * Cloud API クライアント
     * <p>
     * Cloud API: Nature Remo クラウドがインターネットに向けて提供する API
     * http://swagger.nature.global/
     */
    private final NatureApiClient mCloudApiClient;

    /**
     * Local API クライアント
     * <p>
     * Local API: Nature Remo デバイスがローカルネットワークに提供するローカルの API
     * http://local.swagger.nature.global/
     */
    private final NatureRemoLocalApiClient mLocalApiClient;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private NatureRemoRepository(@NonNull AppExecutors appExecutors, @NonNull NatureApiClient cloudApiClient, @NonNull NatureRemoLocalApiClient localApiClient) {
        mAppExecutors = appExecutors;
        mCloudApiClient = cloudApiClient;
        mLocalApiClient = localApiClient;
    }

    /**
     * @param cloudApiClient Cloud API クライアント
     *                       Cloud API: Nature Remo クラウドがインターネットに向けて提供する API
     * @param localApiClient Local API クライアント
     *                       Local API: Nature Remo デバイスがローカルネットワークに提供するローカルの API
     */
    public static NatureRemoRepository getInstance(@NonNull AppExecutors appExecutors, @NonNull NatureApiClient cloudApiClient, @NonNull NatureRemoLocalApiClient localApiClient) {
        if (INSTANCE == null) {
            synchronized (NatureRemoRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NatureRemoRepository(appExecutors, cloudApiClient, localApiClient);
                }
            }
        }
        return INSTANCE;
    }

    public void getUsersMe(@NonNull final Callback<UserInformation> callback) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                final UserInformation userInfo = mCloudApiClient.getUsersMe();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (userInfo != null) {
                            callback.success(userInfo);
                        } else {
                            callback.failure();
                        }
                    }
                });
            }
        };

        mAppExecutors.networkIO().execute(deleteRunnable);
    }

    public void getAppliances(@NonNull final Callback<Appliance[]> callback) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                final Appliance[] appliances = mCloudApiClient.getAppliances();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (appliances != null) {
                            callback.success(appliances);
                        } else {
                            callback.failure();
                        }
                    }
                });
            }
        };

        mAppExecutors.networkIO().execute(deleteRunnable);
    }

    public void getSignalsAppliance(@NonNull final String appliance, @NonNull final Callback<Signal[]> callback) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                final Signal[] appliances = mCloudApiClient.getSignalsAppliance(appliance);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (appliances != null) {
                            callback.success(appliances);
                        } else {
                            callback.failure();
                        }
                    }
                });
            }
        };

        mAppExecutors.networkIO().execute(deleteRunnable);
    }

    public void postSendSignal(@NonNull final String signal, @NonNull final Callback<Void> callback) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mCloudApiClient.postSendSignal(signal);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.success(null);
                    }
                });
            }
        };

        mAppExecutors.networkIO().execute(deleteRunnable);
    }

    public void getMessages(@NonNull final Callback<IRSignal> callback) {
        Runnable deleteRunnable = () -> mLocalApiClient.getMessages(new Callback<IRSignal>() {
            @Override
            public void success(IRSignal message) {
                mAppExecutors.mainThread().execute(() -> callback.success(message));
            }

            @Override
            public void failure() {
                mAppExecutors.mainThread().execute(callback::failure);
            }
        });

        mAppExecutors.networkIO().execute(deleteRunnable);
    }

    public void postMessages(@NonNull final IRSignal message, @NonNull final Callback<Void> callback) {
        Runnable deleteRunnable = () -> mLocalApiClient.postMessages(message, new Callback<Void>() {
            @Override
            public void success(Void v) {
                mAppExecutors.mainThread().execute(() -> callback.success(v));
            }

            @Override
            public void failure() {
                mAppExecutors.mainThread().execute(callback::failure);
            }
        });

        mAppExecutors.networkIO().execute(deleteRunnable);
    }

    public interface Callback<T> {

        void success(T responses);

        void failure();
    }
}
