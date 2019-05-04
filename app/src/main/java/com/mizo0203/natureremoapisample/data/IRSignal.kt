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
package com.mizo0203.natureremoapisample.data

/**
 * Listens to user actions from the UI ([TaskDetailFragment]), retrieves the data and updates
 * the UI as required.
 */
class IRSignal(
        /**
         * Freq
         *
         * minimum: 30
         * maximum: 80
         * default: 38
         *
         * IR sub carrier frequency.
         */
        private val freq: Int,

        /**
         * IR signal consists of on/off of sub carrier frequency.
         * When receiving IR, Remo measures on to off, off to on time intervals and records the time interval sequence.
         * When sending IR, Remo turns on/off the sub carrier frequency with the provided time interval sequence.
         */
        private val data: IntArray,

        /**
         * Format
         *
         * default: us
         *
         * Format and unit of values in the data array.
         * Fixed to us, which means each integer of data array is in microseconds.
         */
        private val format: String
)