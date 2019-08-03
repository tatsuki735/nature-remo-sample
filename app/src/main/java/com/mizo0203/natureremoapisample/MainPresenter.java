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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mizo0203.natureremoapisample.data.IRSignal;
import com.mizo0203.natureremoapisample.data.source.NatureRemoRepository;

/**
 * メイン画面 ({@link MainActivity}) からユーザー操作を受けて、
 * 必要に応じて Nature Remo との通信やメイン画面の更新をします
 */
public class MainPresenter {

    private static final String TAG = "MainPresenter";

    private final NatureRemoRepository mNatureRemoRepository;

    private final MainActivity mMainView;

    private IRSignal mSignalRecord;

    public MainPresenter(@NonNull NatureRemoRepository natureRemoRepository, @NonNull MainActivity mainView) {
        checkNotNull(natureRemoRepository, "natureRemoRepository cannot be null");
        mNatureRemoRepository = natureRemoRepository;
        checkNotNull(mainView, "mainView cannot be null!");
        mMainView = mainView;
    }

    /**
     * メイン画面でボタンがクリックされました
     *
     * @param irSignalMessage ボタンに対応する IR 信号
     */
    public void sendButtonEvent(@NonNull IRSignal irSignalMessage) {
        mNatureRemoRepository.postMessages(irSignalMessage, new NatureRemoRepository.Callback<Void>() {
            /**
             * Nature Remo から IR 信号を送信することに成功した
             */
            @Override
            public void success(Void signals) {
                Log.d(TAG, "postMessages");
                if (mMainView.isActive()) {
                    mMainView.showSuccess();
                }
            }

            /**
             * Nature Remo から IR 信号を送信することに失敗した
             */
            @Override
            public void failure() {
                // The view may not be able to handle UI updates anymore
                if (mMainView.isActive()) {
                    mMainView.showFailure();
                }
            }
        });
    }

    public void record() {
        mNatureRemoRepository.getMessages(new NatureRemoRepository.Callback<IRSignal>() {
            /**
             * Nature Remo から IR 信号を受信することに成功した
             */
            @Override
            public void success(IRSignal signals) {
                Log.d(TAG, "getMessages");
                mSignalRecord = signals;
                sendButtonEvent(mSignalRecord);
            }

            /**
             * Nature Remo から IR 信号を受信することに失敗した
             */
            @Override
            public void failure() {
                // The view may not be able to handle UI updates anymore
                if (mMainView.isActive()) {
                    if (mSignalRecord != null) {
                        sendButtonEvent(mSignalRecord);
                    } else {
                        mMainView.showFailure();
                    }
                }
            }
        });
    }

    /**
     * 呼び出し元メソッドにパラメーターとして渡されるオブジェクト参照がnullではないことを確認します
     *
     * @param reference    an object reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *                     string using {@link String#valueOf(Object)}
     * @throws NullPointerException if {@code reference} is null
     */
    private void checkNotNull(Object reference, @Nullable String errorMessage) {
        if (reference == null) {
            throw new NullPointerException(errorMessage);
        }
    }
}
