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

package com.mizo0203.natureremoapisample.data;

// TODO: AirCon
public class Appliance {
    public final String id;
    public final DeviceCore device;
    public final ApplianceModel model;
    public final String nickname;
    public final String image;
    public final String type;
    public final AirConParams settings;
    public final AirCon aircon;
    public final Signal[] signals;

    public Appliance(String id, DeviceCore device, ApplianceModel model, String nickname, String image, String type, AirConParams settings, AirCon aircon, Signal[] signals) {
        this.id = id;
        this.device = device;
        this.model = model;
        this.nickname = nickname;
        this.image = image;
        this.type = type;
        this.settings = settings;
        this.aircon = aircon;
        this.signals = signals;
    }

    public static class DeviceCore {
        public final String id;
        public final String name;
        public final Number temperature_offset;
        public final Number humidity_offset;
        public final String created_at;
        public final String updated_at;
        public final String firmware_version;

        public DeviceCore(String id, String name, Number temperature_offset, Number humidity_offset, String created_at, String updated_at, String firmware_version) {
            this.id = id;
            this.name = name;
            this.temperature_offset = temperature_offset;
            this.humidity_offset = humidity_offset;
            this.created_at = created_at;
            this.updated_at = updated_at;
            this.firmware_version = firmware_version;
        }
    }

    public static class ApplianceModel {
        public final String id;
        public final String manufacturer;
        public final String remote_name;
        public final String name;
        public final String image;

        public ApplianceModel(String id, String manufacturer, String remote_name, String name, String image) {
            this.id = id;
            this.manufacturer = manufacturer;
            this.remote_name = remote_name;
            this.name = name;
            this.image = image;
        }
    }

    public static class AirConParams {
        public final String temp;
        public final String mode;
        public final String vol;
        public final String dir;
        public final String button;

        public AirConParams(String temp, String mode, String vol, String dir, String button) {
            this.temp = temp;
            this.mode = mode;
            this.vol = vol;
            this.dir = dir;
            this.button = button;
        }
    }

    // TODO:
    public static class AirCon {

    }
}
