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

package com.huawei.hikidskt.custom

import android.os.Bundle
import android.util.Log
import android.util.Pair
import com.huawei.hikidskt.utils.Constant
import com.huawei.hms.mlsdk.tts.*

class CustomTextToSpeechService {

    private lateinit var mlTtsEngine: MLTtsEngine
    private lateinit var mlConfigs: MLTtsConfig

    fun startTtsService(text: String) {
        mlConfigs = MLTtsConfig()
            .setLanguage(MLTtsConstants.TTS_EN_US)
            .setPerson(MLTtsConstants.TTS_SPEAKER_FEMALE_EN)
            .setSpeed(1.0f)
            .setVolume(1.0f)
        mlTtsEngine = MLTtsEngine(mlConfigs)
        mlTtsEngine.setTtsCallback(callback)
        val id = mlTtsEngine.speak(text, MLTtsEngine.QUEUE_APPEND)
        Log.i(Constant.CUSTOM_TTS_SERVICE_TAG, id)
    }

    private var callback: MLTtsCallback = object : MLTtsCallback {
        override fun onError(taskId: String, err: MLTtsError) {
            Log.i(Constant.CUSTOM_TTS_SERVICE_TAG, "CallBack onError : " + err)
        }

        override fun onWarn(taskId: String, warn: MLTtsWarn) {
            Log.i(Constant.CUSTOM_TTS_SERVICE_TAG, "CallBack onWarn : " + warn)
        }

        override fun onRangeStart(taskId: String, start: Int, end: Int) {
            Log.d(Constant.CUSTOM_TTS_SERVICE_TAG,start.toString())
        }

        override fun onAudioAvailable(
            p0: String?,
            p1: MLTtsAudioFragment?,
            p2: Int,
            p3: Pair<Int, Int>?,
            p4: Bundle?
        ) {
            TODO("Not yet implemented")
        }

        override fun onEvent(taskId: String, eventName: Int, bundle: Bundle?) {
            if (eventName == MLTtsConstants.EVENT_PLAY_STOP) {
                Log.i(Constant.CUSTOM_TTS_SERVICE_TAG, "CallBack onEvent : Service Stopped" )
            }
        }
    }
}