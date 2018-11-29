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

package com.mizo0203.natureremoapisample;

import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mizo0203.natureremoapisample.data.IRSignal;
import com.mizo0203.natureremoapisample.data.source.NatureRemoRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link MainActivity}), retrieves the data and updates the
 * UI as required.
 */
public class MainPresenter {

    private static final String TAG = "MainPresenter";

    private final NatureRemoRepository mNatureRemoRepository;

    private final MainActivity mMainView;

    public MainPresenter(@NonNull NatureRemoRepository natureRemoRepository, @NonNull MainActivity mainView) {
        mNatureRemoRepository = checkNotNull(natureRemoRepository, "natureRemoRepository cannot be null");
        mMainView = checkNotNull(mainView, "mainView cannot be null!");
        mMainView.setPresenter(this);

        HandlerThread handlerThread = new HandlerThread("MainPresenter");
        handlerThread.start();
    }

    public void sendButtonEvent(@NonNull IRSignal irSignalMessage) {
        mNatureRemoRepository.postMessages(irSignalMessage, new NatureRemoRepository.Callback<Void>() {
            @Override
            public void success(Void signals) {
                Log.d(TAG, "postMessages");
                if (mMainView.isActive()) {
                    mMainView.showSuccess();
                }
            }

            @Override
            public void failure() {
                // The view may not be able to handle UI updates anymore
                if (mMainView.isActive()) {
                    mMainView.showFailure();
                }
            }
        });
    }
}
