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

package com.mizo0203.natureremoapisample.data.source

import com.example.android.architecture.blueprints.todoapp.util.AppExecutors
import com.mizo0203.natureremoapisample.data.IRSignal

class NatureRemoRepository// Prevent direct instantiation.
private constructor(
        private val mAppExecutors: AppExecutors,

        /**
         * Local API クライアント
         *
         *
         * Local API: Nature Remo デバイスがローカルネットワークに提供するローカルの API
         * http://local.swagger.nature.global/
         */
        private val mLocalApiClient: NatureRemoLocalApiClient) {

    fun getMessages(callback: Callback<IRSignal>) {
        val deleteRunnable = Runnable {
            mLocalApiClient.getMessages(object : Callback<IRSignal> {
                override fun success(responses: IRSignal?) {
                    mAppExecutors.mainThread.execute { callback.success(responses) }
                }

                override fun failure() {
                    mAppExecutors.mainThread.execute { callback.failure() }
                }
            })
        }

        mAppExecutors.networkIO.execute(deleteRunnable)
    }

    fun postMessages(message: IRSignal, callback: Callback<Void>) {
        val deleteRunnable = Runnable {
            mLocalApiClient.postMessages(message, object : Callback<Void> {
                override fun success(responses: Void?) {
                    mAppExecutors.mainThread.execute { callback.success(responses) }
                }

                override fun failure() {
                    mAppExecutors.mainThread.execute { callback.failure() }
                }
            })
        }

        mAppExecutors.networkIO.execute(deleteRunnable)
    }

    interface Callback<T> {

        fun success(responses: T?)

        fun failure()
    }

    companion object {

        private var INSTANCE: NatureRemoRepository? = null

        /**
         * @param localApiClient Local API クライアント
         * Local API: Nature Remo デバイスがローカルネットワークに提供するローカルの API
         */
        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, localApiClient: NatureRemoLocalApiClient): NatureRemoRepository {
            return INSTANCE ?: NatureRemoRepository(appExecutors, localApiClient)
                    .apply { INSTANCE = this }
        }
    }
}