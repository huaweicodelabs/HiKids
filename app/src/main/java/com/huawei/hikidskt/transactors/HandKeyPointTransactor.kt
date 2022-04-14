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

package com.huawei.hikidskt.transactors

import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.util.Log
import android.view.SurfaceHolder
import androidx.core.util.keyIterator
import androidx.core.util.valueIterator
import com.huawei.hikidskt.data.model.Hand
import com.huawei.hikidskt.utils.Constant
import com.huawei.hms.mlsdk.common.MLAnalyzer
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypoints
import kotlin.collections.ArrayList

class HandKeyPointTransactor(surfaceHolder: SurfaceHolder? = null): MLAnalyzer.MLTransactor<MLHandKeypoints> {

    private var mOverlay = surfaceHolder
    private lateinit var mTypeFace: Typeface

    fun setOverlay(surfaceHolder: SurfaceHolder, typeface: Typeface) {
        mOverlay = surfaceHolder
        mTypeFace = typeface
    }

    override fun transactResult(result: MLAnalyzer.Result<MLHandKeypoints>?) {
        if (result == null)
            return

        val canvas = mOverlay?.lockCanvas() ?: return

        canvas.drawColor(0, PorterDuff.Mode.CLEAR)

        val numberString = analyzeHandsAndGetNumber(result) //Turns finger number

        val centerX = (canvas.width / 2F) - 50
        val centerY = (canvas.height / 2F) - 50

        canvas.drawText(numberString, centerX, centerY, Paint().also {
            it.style = Paint.Style.FILL
            it.textSize = 300F
            it.color = Color.parseColor("#8dc9ff")
            it.typeface = mTypeFace

        })
        mOverlay?.unlockCanvasAndPost(canvas)
    }

    private fun analyzeHandsAndGetNumber(result: MLAnalyzer.Result<MLHandKeypoints>): String {
        val hands = ArrayList<Hand>()
        var number = 0

        for (key in result.analyseList.keyIterator()) {
            hands.add(Hand())

            for (value in result.analyseList.valueIterator()) {
                number += hands.last().createHand(value.handKeypoints).getNumber()
            }
        }
        return number.toString()
    }

    override fun destroy() {
        Log.d(Constant.HAND_TRANSACTOR_TAG, "destroy()")
    }
}