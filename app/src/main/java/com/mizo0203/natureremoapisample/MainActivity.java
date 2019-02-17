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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mizo0203.natureremoapisample.panasonic.tv.RemoteControlButtonType;

/**
 * リモコンアプリのメイン画面 (UI)
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // TODO: NATURE_REMO_IP_ADDRESS をセット
    private static final String NATURE_REMO_IP_ADDRESS = "192.168.1.23";

    private MainPresenter mPresenter;

    /**
     * メイン画面が前面に表示されいるか
     */
    private boolean mActive = false;

    /**
     * メイン画面が起動された直後に呼び出されます
     * <p>
     * ここで、画面に表示する＜1＞〜＜12＞ボタンなどの各種 View や
     * ({@link MainPresenter}) を初期化します
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_1).setOnClickListener(this);
        findViewById(R.id.button_2).setOnClickListener(this);
        findViewById(R.id.button_3).setOnClickListener(this);
        findViewById(R.id.button_4).setOnClickListener(this);
        findViewById(R.id.button_5).setOnClickListener(this);
        findViewById(R.id.button_6).setOnClickListener(this);
        findViewById(R.id.button_7).setOnClickListener(this);
        findViewById(R.id.button_8).setOnClickListener(this);
        findViewById(R.id.button_9).setOnClickListener(this);
        findViewById(R.id.button_10).setOnClickListener(this);
        findViewById(R.id.button_11).setOnClickListener(this);
        findViewById(R.id.button_12).setOnClickListener(this);

        findViewById(R.id.button_ch_up).setOnClickListener(this);
        findViewById(R.id.button_ch_down).setOnClickListener(this);

        findViewById(R.id.button_vol_up).setOnClickListener(this);
        findViewById(R.id.button_vol_down).setOnClickListener(this);

        // Create the presenter
        mPresenter = new MainPresenter(Injection.provideNatureRemoRepository(NATURE_REMO_IP_ADDRESS), this);
    }

    /**
     * メイン画面が前面に表示された直後に呼び出されます
     */
    @Override
    public void onResume() {
        super.onResume();
        mActive = true;
    }

    /**
     * 別の画面を前面に表示する直前に呼び出されます
     */
    @Override
    public void onPause() {
        super.onPause();
        mActive = false;
    }

    /**
     * 成功メッセージを表示します
     */
    public void showSuccess() {
        Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();
    }

    /**
     * 失敗メッセージを表示します
     */
    public void showFailure() {
        Toast.makeText(getApplicationContext(), R.string.failure, Toast.LENGTH_SHORT).show();
    }

    /**
     * メイン画面が前面に表示されいるかを取得します
     *
     * @return メイン画面が前面に表示されていれば true 、そうでないなら false
     */
    public boolean isActive() {
        return mActive;
    }

    /**
     * View がクリックされた直後に呼び出されます
     *
     * @param v クリックされた View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_1:
                mPresenter.sendButtonEvent(RemoteControlButtonType.NUM_1);
                break;
            case R.id.button_2:
                mPresenter.sendButtonEvent(RemoteControlButtonType.NUM_2);
                break;
            case R.id.button_3:
                mPresenter.sendButtonEvent(RemoteControlButtonType.NUM_3);
                break;
            case R.id.button_4:
                mPresenter.sendButtonEvent(RemoteControlButtonType.NUM_4);
                break;
            case R.id.button_5:
                mPresenter.sendButtonEvent(RemoteControlButtonType.NUM_5);
                break;
            case R.id.button_6:
                mPresenter.sendButtonEvent(RemoteControlButtonType.NUM_6);
                break;
            case R.id.button_7:
                mPresenter.sendButtonEvent(RemoteControlButtonType.NUM_7);
                break;
            case R.id.button_8:
                mPresenter.sendButtonEvent(RemoteControlButtonType.NUM_8);
                break;
            case R.id.button_9:
                mPresenter.sendButtonEvent(RemoteControlButtonType.NUM_9);
                break;
            case R.id.button_10:
                mPresenter.sendButtonEvent(RemoteControlButtonType.NUM_10);
                break;
            case R.id.button_11:
                mPresenter.sendButtonEvent(RemoteControlButtonType.NUM_11);
                break;
            case R.id.button_12:
                mPresenter.sendButtonEvent(RemoteControlButtonType.NUM_12);
                break;
            case R.id.button_ch_up:
                mPresenter.sendButtonEvent(RemoteControlButtonType.CH_UP);
                break;
            case R.id.button_ch_down:
                mPresenter.sendButtonEvent(RemoteControlButtonType.CH_DOWN);
                break;
            case R.id.button_vol_up:
                mPresenter.sendButtonEvent(RemoteControlButtonType.VOL_UP);
                break;
            case R.id.button_vol_down:
                mPresenter.sendButtonEvent(RemoteControlButtonType.VOL_DOWN);
                break;
        }
    }
}
