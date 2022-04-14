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

import android.text.TextUtils
import android.util.Log
import com.huawei.hikidskt.utils.Constant
import com.huawei.hmf.tasks.Task
import com.huawei.hms.mlsdk.translate.MLTranslatorFactory
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslateSetting
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslator

class CustomTranslateService {

    fun translateText(sourceText: String, translateResult:(String)-> Unit) {
        val setting: MLRemoteTranslateSetting =
            MLRemoteTranslateSetting.Factory()
                .setSourceLangCode("en")
                .setTargetLangCode("tr")
                .create()
        val mlRemoteTranslator: MLRemoteTranslator =
            MLTranslatorFactory.getInstance().getRemoteTranslator(setting)

        if(!TextUtils.isEmpty(sourceText)){
            val task: Task<String> =
                mlRemoteTranslator.asyncTranslate(sourceText.toString())
            task.addOnSuccessListener {
                Log.i(Constant.CUSTOM_TRANSLATE_SERVICE_TAG, it)
                translateResult(it)
            }.addOnFailureListener {
                Log.i(Constant.CUSTOM_TRANSLATE_SERVICE_TAG, it!!.toString())
                translateResult(it!!.toString())
            }
        } else {
            Log.i(Constant.CUSTOM_TRANSLATE_SERVICE_TAG, "null")
            translateResult("null")

        }
    }
}