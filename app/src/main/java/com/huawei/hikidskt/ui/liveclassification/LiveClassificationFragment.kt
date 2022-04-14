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

package com.huawei.hikidskt.ui.liveclassification

import android.content.ActivityNotFoundException
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.huawei.hikidskt.custom.Permission
import com.huawei.hikidskt.custom.PermissionManager
import com.huawei.hikidskt.databinding.FragmentLiveClassificationBinding
import com.huawei.hikidskt.ui.base.BaseFragment
import com.huawei.hikidskt.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_live_fingers.*

@AndroidEntryPoint
class LiveClassificationFragment : BaseFragment<LiveClassificationViewModel, FragmentLiveClassificationBinding>(){

    private val liveClassificationViewModel: LiveClassificationViewModel by viewModels()

    private val permissionManager = PermissionManager.from(this)

    override fun getFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLiveClassificationBinding {
        return FragmentLiveClassificationBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): LiveClassificationViewModel = liveClassificationViewModel

    override fun setupUi() {
        super.setupUi()

        permissionManager
            .request(Permission.Camera)
            .rationale("We need permission to see objects.")
            .checkPermission { granted ->
                if (granted) {
                    try {
                        liveClassificationViewModel.init(requireContext(), frameLayout)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                } else {
                    Log.i(Constant.LIVE_CLASSIFICATION_TAG,"We couldn't access the camera.")
                }
            }
    }
}