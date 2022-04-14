/*
 *
 * *********************************************************************************
 * |                                                                                |
 * | Copyright 2022. Huawei Technologies Co., Ltd. All rights reserved.             |
 * |                                                                                |
 * | Licensed under the Apache License, Version 2.0 (the "License");                |
 * | you may not use this file except in compliance with the License.               |
 * | You may obtain a copy of the License at                                        |
 * |                                                                                |
 * | http://www.apache.org/licenses/LICENSE-2.0                                     |
 * |                                                                                |
 * | Unless required by applicable law or agreed to in writing, software            |
 * | distributed under the License is distributed on an "AS IS" BASIS,              |
 * | WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.       |
 * | See the License for the specific language governing permissions and            |
 * | limitations under the License.                                                 |
 * |                                                                                |
 * *********************************************************************************
 * /
 */

package com.huawei.hikidskt.utils

import android.app.Application
import com.huawei.hikidskt.BuildConfig
import com.huawei.hms.mlsdk.common.MLApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiKidsApplication  : Application() {
    companion object {
        lateinit var instance: HiKidsApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MLApplication.getInstance().setApiKey(BuildConfig.API_KEY)
    }
}