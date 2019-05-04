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

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mizo0203.natureremoapisample.panasonic.tv.RemoteControlButtonType

class MainActivity : AppCompatActivity(), MainContract.View, View.OnClickListener {

    override lateinit var presenter: MainContract.Presenter

    override var isActive: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button_1).setOnClickListener(this)
        findViewById<Button>(R.id.button_2).setOnClickListener(this)
        findViewById<Button>(R.id.button_3).setOnClickListener(this)
        findViewById<Button>(R.id.button_4).setOnClickListener(this)
        findViewById<Button>(R.id.button_5).setOnClickListener(this)
        findViewById<Button>(R.id.button_6).setOnClickListener(this)
        findViewById<Button>(R.id.button_7).setOnClickListener(this)
        findViewById<Button>(R.id.button_8).setOnClickListener(this)
        findViewById<Button>(R.id.button_9).setOnClickListener(this)
        findViewById<Button>(R.id.button_10).setOnClickListener(this)
        findViewById<Button>(R.id.button_11).setOnClickListener(this)
        findViewById<Button>(R.id.button_12).setOnClickListener(this)

        findViewById<Button>(R.id.button_ch_up).setOnClickListener(this)
        findViewById<Button>(R.id.button_ch_down).setOnClickListener(this)

        findViewById<Button>(R.id.button_vol_up).setOnClickListener(this)
        findViewById<Button>(R.id.button_vol_down).setOnClickListener(this)

        // Create the presenter
        presenter = MainPresenter(Injection.provideNatureRemoRepository(NATURE_REMO_IP_ADDRESS), this)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
        isActive = true
    }

    override fun onPause() {
        super.onPause()
        isActive = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.result(requestCode, resultCode)
    }

    override fun showSuccess() {
        Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show()
    }

    override fun showFailure() {
        Toast.makeText(getApplicationContext(), R.string.failure, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_1 -> presenter.sendButtonEvent(RemoteControlButtonType.NUM_1)
            R.id.button_2 -> presenter.sendButtonEvent(RemoteControlButtonType.NUM_2)
            R.id.button_3 -> presenter.sendButtonEvent(RemoteControlButtonType.NUM_3)
            R.id.button_4 -> presenter.sendButtonEvent(RemoteControlButtonType.NUM_4)
            R.id.button_5 -> presenter.sendButtonEvent(RemoteControlButtonType.NUM_5)
            R.id.button_6 -> presenter.sendButtonEvent(RemoteControlButtonType.NUM_6)
            R.id.button_7 -> presenter.sendButtonEvent(RemoteControlButtonType.NUM_7)
            R.id.button_8 -> presenter.sendButtonEvent(RemoteControlButtonType.NUM_8)
            R.id.button_9 -> presenter.sendButtonEvent(RemoteControlButtonType.NUM_9)
            R.id.button_10 -> presenter.sendButtonEvent(RemoteControlButtonType.NUM_10)
            R.id.button_11 -> presenter.sendButtonEvent(RemoteControlButtonType.NUM_11)
            R.id.button_12 -> presenter.sendButtonEvent(RemoteControlButtonType.NUM_12)
            R.id.button_ch_up -> presenter.sendButtonEvent(RemoteControlButtonType.CH_UP)
            R.id.button_ch_down -> presenter.sendButtonEvent(RemoteControlButtonType.CH_DOWN)
            R.id.button_vol_up -> presenter.sendButtonEvent(RemoteControlButtonType.VOL_UP)
            R.id.button_vol_down -> presenter.sendButtonEvent(RemoteControlButtonType.VOL_DOWN)
        }
    }

    companion object {

        // TODO: NATURE_REMO_IP_ADDRESS をセット
        private val NATURE_REMO_IP_ADDRESS = "192.168.1.23"
    }
}
