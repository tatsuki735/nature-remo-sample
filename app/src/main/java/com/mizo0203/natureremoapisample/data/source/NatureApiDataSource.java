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
import com.mizo0203.natureremoapisample.data.Signal;
import com.mizo0203.natureremoapisample.data.UserInformation;
import com.mizo0203.natureremoapisample.util.AppExecutors;

public class NatureApiDataSource {

    private static volatile NatureApiDataSource INSTANCE;

    private NatureApiClient mClient;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private NatureApiDataSource(@NonNull AppExecutors appExecutors, @NonNull NatureApiClient client) {
        mAppExecutors = appExecutors;
        mClient = client;
    }

    public static NatureApiDataSource getInstance(@NonNull AppExecutors appExecutors, @NonNull NatureApiClient client) {
        if (INSTANCE == null) {
            synchronized (NatureApiDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NatureApiDataSource(appExecutors, client);
                }
            }
        }
        return INSTANCE;
    }

    public void getUsersMe(@NonNull final Callback<UserInformation> callback) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                final UserInformation userInfo = mClient.getUsersMe();
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
                final Appliance[] appliances = mClient.getAppliances();
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
                final Signal[] appliances = mClient.getSignalsAppliance(appliance);
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
                mClient.postSendSignal(signal);
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

    public interface Callback<T> {

        void success(T userInfo);

        void failure();
    }
}
