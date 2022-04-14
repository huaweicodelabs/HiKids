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

package com.huawei.hikidskt.ui.livefingers

import android.content.Context
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.huawei.hikidskt.R
import com.huawei.hikidskt.ui.base.BaseViewModel
import com.huawei.hikidskt.transactors.HandKeyPointTransactor
import com.huawei.hms.mlsdk.common.LensEngine
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzer
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzerFactory
import com.huawei.hms.mlsdk.handkeypoint.MLHandKeypointAnalyzerSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LiveFingersViewModel @Inject constructor(

) : BaseViewModel(){
    private lateinit var mSurfaceHolderCamera: SurfaceHolder
    private lateinit var mSurfaceHolderOverlay: SurfaceHolder

    private var mHandKeyPointTransactor = HandKeyPointTransactor()

    private lateinit var mLensEngine: LensEngine

    private lateinit var mAnalyzer: MLHandKeypointAnalyzer

    private lateinit var mContext: Context
    private lateinit var mRootLayout: ViewGroup

    fun init(context: Context, rootLayout: ViewGroup) {
        mContext = context
        mRootLayout = rootLayout
        addSurfaceViews()
    }

    private fun addSurfaceViews() {

        val surfaceViewCamera = SurfaceView(mContext).also {
            it.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            mSurfaceHolderCamera = it.holder
        }

        val surfaceViewOverlay = SurfaceView(mContext).also {
            it.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            mSurfaceHolderOverlay = it.holder
            mSurfaceHolderOverlay.setFormat(PixelFormat.TRANSPARENT)
            val typeface = Typeface.create(ResourcesCompat.getFont(mContext, R.font.from_cartoon_blocks), Typeface.BOLD)
            mHandKeyPointTransactor.setOverlay(mSurfaceHolderOverlay, typeface)
        }

        mRootLayout.addView(surfaceViewCamera)
        mRootLayout.addView(surfaceViewOverlay)

        mSurfaceHolderCamera.addCallback(surfaceHolderCallback)
    }

    private val surfaceHolderCallback = object : SurfaceHolder.Callback {

        override fun surfaceCreated(holder: SurfaceHolder) {
            createAnalyzer()
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            prepareLensEngine(width, height)
            mLensEngine.run(holder)
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            mLensEngine.release()
        }
    }

    private fun createAnalyzer() {
        val settings = MLHandKeypointAnalyzerSetting.Factory()
            .setSceneType(MLHandKeypointAnalyzerSetting.TYPE_ALL)
            .setMaxHandResults(2)
            .create()

        mAnalyzer = MLHandKeypointAnalyzerFactory.getInstance().getHandKeypointAnalyzer(settings)
        mAnalyzer.setTransactor(mHandKeyPointTransactor)
    }

    private fun prepareLensEngine(width: Int, height: Int) {

        val dimen1: Int
        val dimen2: Int

        if (mContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            dimen1 = width
            dimen2 = height
        } else {
            dimen1 = height
            dimen2 = width
        }

        mLensEngine = LensEngine.Creator(mContext, mAnalyzer)
            .setLensType(LensEngine.BACK_LENS)
            .applyDisplayDimension(dimen1, dimen2)
            .applyFps(5F)
            .enableAutomaticFocus(true)
            .create()
    }
}