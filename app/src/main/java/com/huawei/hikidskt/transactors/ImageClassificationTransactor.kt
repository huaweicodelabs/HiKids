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
import com.huawei.hikidskt.utils.Constant
import com.huawei.hms.mlsdk.classification.MLImageClassification
import com.huawei.hms.mlsdk.common.MLAnalyzer

class ImageClassificationTransactor(surfaceHolder: SurfaceHolder? = null) :
    MLAnalyzer.MLTransactor<MLImageClassification> {

    private var mOverlay = surfaceHolder
    private lateinit var mTypeFace: Typeface

    fun setOverlay(surfaceHolder: SurfaceHolder, typeface: Typeface) {
        mOverlay = surfaceHolder
        mTypeFace = typeface
    }

    override fun transactResult(result: MLAnalyzer.Result<MLImageClassification?>) {
        if (result == null)
            return

        val canvas = mOverlay?.lockCanvas() ?: return

        Thread.sleep(500)
        canvas.drawColor(0, PorterDuff.Mode.CLEAR)

        val resultString = analyzeImageAndGetResult(result)

        canvas.drawText(resultString, 0F, 150F, Paint().also {
            it.style = Paint.Style.FILL
            it.textSize = 100F
            it.color = Color.parseColor("#8dc9ff")
            it.typeface = mTypeFace

        })
        mOverlay?.unlockCanvasAndPost(canvas)

    }

    private fun analyzeImageAndGetResult(result: MLAnalyzer.Result<MLImageClassification?>): String {
        var classifiedImage = "Object"

        if (result.analyseList[0]!!.name != null) {
            classifiedImage = result.analyseList[0]!!.name
            if (classifiedImage == result.analyseList[0]!!.name) {
                classifiedImage = classifiedImage
            }
        } else {
            classifiedImage = "Image"
        }

        return classifiedImage
    }

    override fun destroy() {
        Log.d(Constant.IMAGE_TRANSACTOR_TAG, "destroy()")
    }
}