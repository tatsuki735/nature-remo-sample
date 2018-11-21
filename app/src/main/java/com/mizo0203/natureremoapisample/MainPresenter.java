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

import com.mizo0203.natureremoapisample.data.Appliance;
import com.mizo0203.natureremoapisample.data.IRSignal;
import com.mizo0203.natureremoapisample.data.Signal;
import com.mizo0203.natureremoapisample.data.UserInformation;
import com.mizo0203.natureremoapisample.data.source.NatureRemoRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link MainActivity}), retrieves the data and updates the
 * UI as required.
 */
public class MainPresenter implements MainContract.Presenter {

    private static final String TAG = "MainPresenter";

    private final NatureRemoRepository mNatureRemoRepository;

    private final MainContract.View mMainView;

    private IRSignal mIRSignalMessage = null;

    public MainPresenter(@NonNull NatureRemoRepository natureRemoRepository, @NonNull MainContract.View mainView) {
        mNatureRemoRepository = checkNotNull(natureRemoRepository, "natureRemoRepository cannot be null");
        mMainView = checkNotNull(mainView, "mainView cannot be null!");
        mMainView.setPresenter(this);

        HandlerThread handlerThread = new HandlerThread("MainPresenter");
        handlerThread.start();
    }

    @Override
    public void start() {
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a task was successfully added, show snackbar
        mMainView.showSuccess();
    }

    @Override
    public void sendButtonEvent(ButtonType buttonType) {
        switch (buttonType) {
            case NUM_1:
                mNatureRemoRepository.getUsersMe(new NatureRemoRepository.Callback<UserInformation>() {
                    @Override
                    public void success(UserInformation userInfo) {
                        Log.d(TAG, "userInfo.id: \t" + userInfo.id);
                        Log.d(TAG, "userInfo.nickname: \t" + userInfo.nickname);
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
                break;
            case NUM_2:
                mNatureRemoRepository.getAppliances(new NatureRemoRepository.Callback<Appliance[]>() {
                    @Override
                    public void success(Appliance[] appliances) {
                        Log.d(TAG, "appliances.length: \t" + appliances.length);
                        for (Appliance appliance : appliances) {
                            Log.d(TAG, "appliance.nickname: \t" + appliance.nickname);
                            Log.d(TAG, "appliance.id: \t" + appliance.id);
                        }
                        if (mMainView.isActive()) {
                            mMainView.showFailure();
                        }
                    }

                    @Override
                    public void failure() {
                        // The view may not be able to handle UI updates anymore
                        if (!mMainView.isActive()) {
                            return;
                        }
                        if (mMainView.isActive()) {
                            mMainView.showFailure();
                        }
                    }
                });
                break;
            case NUM_3:
                mNatureRemoRepository.getSignalsAppliance(MainActivity.APPLIANCE_ID, new NatureRemoRepository.Callback<Signal[]>() {
                    @Override
                    public void success(Signal[] signals) {
                        Log.d(TAG, "signals.length: \t" + signals.length);
                        for (Signal signal : signals) {
                            Log.d(TAG, "signal.name: \t" + signal.name);
                            Log.d(TAG, "signal.id: \t" + signal.id);
                        }
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
                break;
            case NUM_4:
                mNatureRemoRepository.getSignalsAppliance(MainActivity.APPLIANCE_ID, new NatureRemoRepository.Callback<Signal[]>() {
                    @Override
                    public void success(Signal[] signals) {
                        Log.d(TAG, "signals.length: \t" + signals.length);
                        for (Signal signal : signals) {
                            Log.d(TAG, "signal.name: \t" + signal.name);
                            Log.d(TAG, "signal.id: \t" + signal.id);
                        }
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
                break;
            case NUM_5:
                mNatureRemoRepository.postSendSignal(MainActivity.SIGNAL_ID, new NatureRemoRepository.Callback<Void>() {
                    @Override
                    public void success(Void signals) {
                        Log.d(TAG, "postSendSignal");
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
                break;
            case NUM_6:
                mNatureRemoRepository.getMessages(new NatureRemoRepository.Callback<IRSignal>() {
                    @Override
                    public void success(IRSignal message) {
                        Log.d(TAG, "getMessages");
                        mIRSignalMessage = message;
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
                break;
            case NUM_7:
                if (mIRSignalMessage == null) {
                    Log.e(TAG, "Press the 6 button");
                    mMainView.showFailure();
                    break;
                }
                mNatureRemoRepository.postMessages(mIRSignalMessage, new NatureRemoRepository.Callback<Void>() {
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
                break;
            default:
                throw new UnsupportedOperationException("未実装");

        }
    }

}
