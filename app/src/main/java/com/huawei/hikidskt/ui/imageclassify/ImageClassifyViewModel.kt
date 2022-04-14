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

package com.huawei.hikidskt.ui.imageclassify

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.huawei.hikidskt.BuildConfig
import com.huawei.hikidskt.custom.CustomTextToSpeechService
import com.huawei.hikidskt.custom.CustomTranslateService
import com.huawei.hikidskt.ui.base.BaseViewModel
import com.huawei.hikidskt.utils.Constant
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.classification.MLRemoteClassificationAnalyzerSetting
import com.huawei.hms.mlsdk.common.MLApplication
import com.huawei.hms.mlsdk.common.MLException
import com.huawei.hms.mlsdk.common.MLFrame
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ImageClassifyViewModel @Inject constructor(

) : BaseViewModel(){

    var customTranslateService: CustomTranslateService = CustomTranslateService()
    private var customTextToSpeechService: CustomTextToSpeechService = CustomTextToSpeechService()

    val translatedTextResultLiveData = MutableLiveData<String?>()
    val analayzeTextLiveData = MutableLiveData<String?>()

    fun startClassification(bitmap: Bitmap) {
        MLApplication.getInstance().setApiKey(BuildConfig.API_KEY)
        val setting = MLRemoteClassificationAnalyzerSetting.Factory()
            .setMinAcceptablePossibility(0.8f)
            .create()
        val analyzer = MLAnalyzerFactory.getInstance().getRemoteImageClassificationAnalyzer(setting)

        val frame = MLFrame.fromBitmap(bitmap)

        val task = analyzer!!.asyncAnalyseFrame(frame)
        task.addOnSuccessListener {
            analayzeTextLiveData.postValue(it.get(0).name)
            translateObjectName(it.get(0).name)
        }.addOnFailureListener { e ->
            analayzeTextLiveData.postValue(e.message.toString())
            translateObjectName(e.message.toString())
            Log.i(Constant.IMAGE_CLASSIFICATION_TAG, "StartClassificationOnFailureListener : " + e.message.toString())
            try {
                val mlException = e as MLException
                val errorCode = mlException.errCode
                val errorMessage = mlException.message
            } catch (error: Exception) {
                analayzeTextLiveData.postValue(error.message)
                translateObjectName(error.message.toString())
                Log.i(Constant.IMAGE_CLASSIFY_VM_TAG, "StartClassificationCatchError : " + error.message)
            }
        }

        try {
            if (analyzer != null) {
                analyzer.stop()
            }
        } catch (e: IOException) {
            // Exception handling.
        }
    }

    fun translateObjectName(text: String){
        customTranslateService?.translateText(text){
            translatedTextResultLiveData.postValue(it)
        }
    }

    fun callTTS(text: String){
        customTextToSpeechService.startTtsService(text)
    }

    fun getAnalayzeResultLiveData() = analayzeTextLiveData

    fun getTranslatedAnalayzeResultLiveData() = translatedTextResultLiveData
}

