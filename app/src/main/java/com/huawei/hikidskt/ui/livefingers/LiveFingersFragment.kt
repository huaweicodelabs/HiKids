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

import android.content.ActivityNotFoundException
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.huawei.hikidskt.custom.Permission
import com.huawei.hikidskt.custom.PermissionManager
import com.huawei.hikidskt.databinding.FragmentLiveFingersBinding
import com.huawei.hikidskt.ui.base.BaseFragment
import com.huawei.hikidskt.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_live_fingers.*

@AndroidEntryPoint
class LiveFingersFragment   : BaseFragment<LiveFingersViewModel, FragmentLiveFingersBinding>(){

    private val liveFingersViewModel: LiveFingersViewModel by viewModels()

    private val permissionManager = PermissionManager.from(this)

    override fun getFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLiveFingersBinding {
        return FragmentLiveFingersBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): LiveFingersViewModel = liveFingersViewModel

    override fun setupUi() {
        super.setupUi()

        permissionManager
            .request(Permission.Camera)
            .rationale("We need permission to see your fingers.")
            .checkPermission { granted ->
                if (granted) {
                    try {
                        liveFingersViewModel.init(requireContext(), frameLayout)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                } else {
                    Log.i(Constant.LIVE_FINGERS_TAG,"We couldn't access the camera.")
                }
            }
    }
}