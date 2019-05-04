/*
 * Copyright 2019, Satoki Mizoguchi
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

package com.mizo0203.natureremoapisample

import android.os.HandlerThread
import android.util.Log
import com.google.common.base.Preconditions.checkNotNull
import com.mizo0203.natureremoapisample.data.IRSignal
import com.mizo0203.natureremoapisample.data.source.NatureRemoRepository

/**
 * Listens to user actions from the UI ([MainActivity]), retrieves the data and updates the
 * UI as required.
 */
class MainPresenter(natureRemoRepository: NatureRemoRepository, mainView: MainContract.View) : MainContract.Presenter {

    private val mNatureRemoRepository: NatureRemoRepository

    private val mMainView: MainContract.View

    init {
        mNatureRemoRepository = checkNotNull(natureRemoRepository, "natureRemoRepository cannot be null")
        mMainView = checkNotNull(mainView, "mainView cannot be null!")
        mMainView.presenter = this

        val handlerThread = HandlerThread("MainPresenter")
        handlerThread.start()
    }

    override fun start() {}

    override fun result(requestCode: Int, resultCode: Int) {
        // If a task was successfully added, show snackbar
        mMainView.showSuccess()
    }

    override fun sendButtonEvent(irSignalMessage: IRSignal) {
        mNatureRemoRepository.postMessages(irSignalMessage, object : NatureRemoRepository.Callback<Void> {
            override fun success(signals: Void?) {
                Log.d(TAG, "postMessages")
                if (mMainView.isActive) {
                    mMainView.showSuccess()
                }
            }

            override fun failure() {
                // The view may not be able to handle UI updates anymore
                if (mMainView.isActive) {
                    mMainView.showFailure()
                }
            }
        })
    }

    companion object {

        private val TAG = "MainPresenter"
    }
}
