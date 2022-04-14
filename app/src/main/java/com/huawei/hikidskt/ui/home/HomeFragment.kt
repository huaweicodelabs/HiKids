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

package com.huawei.hikidskt.ui.home

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.huawei.hikidskt.R
import com.huawei.hikidskt.databinding.FragmentHomeBinding
import com.huawei.hikidskt.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun getFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): HomeViewModel = homeViewModel

    override fun setupUi() {
        super.setupUi()

        homeViewModel.getRemoteTextColor()

        layout_imageClassification.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_imageClassifyFragment)
        }

        layout_handGesture.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_fingerCountFragment)
        }

        layout_liveFingerCount.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_liveFingersFragment)
        }

        layout_liveObjects.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_liveClassificationFragment)
        }

        layout_faceDetection.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_emotionsFragment)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setupObservers() {
        super.setupObservers()

        homeViewModel.setRemoteTextColorLiveData().observe(viewLifecycleOwner, Observer {
            appNameText.setTextColor(Integer.parseUnsignedInt(it,16))
        })
    }
}