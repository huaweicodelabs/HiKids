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
import android.util.SparseArray
import android.view.SurfaceHolder
import androidx.core.util.valueIterator
import com.huawei.hms.mlsdk.common.MLAnalyzer
import com.huawei.hms.mlsdk.face.MLFace
import java.util.*

class EmotionAnalyzerTransactor: MLAnalyzer.MLTransactor<MLFace> {

    private var mOverlay: SurfaceHolder? = null
    private lateinit var mTypeFace: Typeface

    fun setOverlay(surfaceHolder: SurfaceHolder, typeface: Typeface) {
        mOverlay = surfaceHolder
        mTypeFace = typeface
    }

    override fun transactResult(result: MLAnalyzer.Result<MLFace>?) {
        draw(result?.analyseList)
    }

    private fun draw(faces: SparseArray<MLFace>?) {
        val canvas = mOverlay?.lockCanvas()

        if (canvas != null && faces != null) {

            canvas.drawColor(0, PorterDuff.Mode.CLEAR)

            for (face in faces.valueIterator()) {

                val emotions = HashMap<String, Float>()
                emotions["Smiling"] = face.emotions.smilingProbability
                emotions["Neutral"] = face.emotions.neutralProbability
                emotions["Angry"] = face.emotions.angryProbability
                emotions["Fear"] = face.emotions.fearProbability
                emotions["Sad"] = face.emotions.sadProbability
                emotions["Disgust"] = face.emotions.disgustProbability
                emotions["Surprise"] = face.emotions.surpriseProbability
                val result = sortHashMap(emotions)

                Paint().also {
                    it.color = Color.parseColor("#8dc9ff")
                    it.textSize = 100F
                    it.textAlign = Paint.Align.CENTER
                    it.typeface = mTypeFace
                    canvas.drawText(result[0], face.border.exactCenterX(), face.border.exactCenterY(), it)
                }

            }

            mOverlay?.unlockCanvasAndPost(canvas)
        }
    }

    fun sortHashMap(map: Map<String, Float>): List<String> {
        val entey = map.entries
        val list: List<Map.Entry<String, Float>> = ArrayList(entey)
        Collections.sort(
            list
        ) { o1: Map.Entry<String, Float>, o2: Map.Entry<String, Float> ->
            if (o2.value - o1.value >= 0) {
                return@sort 1
            } else {
                return@sort -1
            }
        }
        val emotions: MutableList<String> = ArrayList()
        for (i in 0..1) {
            emotions.add(list[i].key)
        }
        return emotions
    }

    override fun destroy() {

    }
}