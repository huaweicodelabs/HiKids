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

package com.huawei.hikidskt.ui.fingers

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.huawei.hikidskt.custom.CustomTextToSpeechService
import com.huawei.hikidskt.custom.CustomTranslateService
import com.huawei.hikidskt.data.model.Hand
import com.huawei.hikidskt.ui.base.BaseViewModel
import com.huawei.hikidskt.utils.Constant
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzer
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzerFactory
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzerSetting
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypoints
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FingerCountViewModel @Inject constructor(

) : BaseViewModel(){

    private lateinit var mAnalyzer: MLHandKeypointAnalyzer

    private val analayzeResultLiveData = MutableLiveData<String?>()
    private val translatedAnalayzeResultLiveData = MutableLiveData<String?>()

    private var customTranslateService: CustomTranslateService? = null
    private var customTextToSpeechService: CustomTextToSpeechService = CustomTextToSpeechService()

    fun createAnalyzer(bitmap: Bitmap) {
        customTranslateService = CustomTranslateService()
        val settings = MLHandKeypointAnalyzerSetting.Factory()
            .setSceneType(MLHandKeypointAnalyzerSetting.TYPE_ALL)
            .setMaxHandResults(2)
            .create()

        mAnalyzer = MLHandKeypointAnalyzerFactory.getInstance().getHandKeypointAnalyzer(settings)

        var frame = MLFrame.fromBitmap(bitmap)

        val task = mAnalyzer!!.asyncAnalyseFrame(frame)
        task.addOnSuccessListener {
            analayzeResultLiveData.postValue(analyzeHandsAndGetNumber(it))
            translateFingerCount(analyzeHandsAndGetNumber(it))
        }.addOnFailureListener {
            // Detection failure.
        }
        if (mAnalyzer != null) {
            mAnalyzer!!.stop()
        }
    }


    fun analyzeHandsAndGetNumber(result: MutableList<MLHandKeypoints>): String {
        val hands = ArrayList<Hand>()
        var number = 0
        var numberString = "Zero"

        for (key in result) {
            hands.add(Hand())

            for (value in result) {
                number += hands.last().createHand(value.handKeypoints).getNumber()
            }
        }

        Log.i(Constant.FINGER_COUNT_VM_TAG, "analyzeHandsAndGetNumber: " + number.toString())
        if(number == 0){
            numberString = "Zero"
        }else if(number == 1){
            numberString = "One"
        }else if(number == 2){
            numberString = "Two"
        }else if(number == 3){
            numberString = "Three"
        }else if(number == 4){
            numberString = "Four"
        }else if(number == 5){
            numberString = "Five"
        }
        return numberString
    }

    fun translateFingerCount(text: String){
        customTranslateService?.translateText(text){
            translatedAnalayzeResultLiveData.postValue(it)
        }
    }

    fun callTTS(text: String){
        customTextToSpeechService.startTtsService(text)
    }

    fun getAnalayzeResultLiveData() = analayzeResultLiveData

    fun getTranslatedAnalayzeResultLiveData() = translatedAnalayzeResultLiveData
}