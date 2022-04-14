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

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.huawei.agconnect.remoteconfig.AGConnectConfig
import com.huawei.hikidskt.ui.base.BaseViewModel
import com.huawei.hikidskt.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : BaseViewModel(){

    val remoteTextColor = MutableLiveData<String?>()

    fun getRemoteTextColor(){
        val config = AGConnectConfig.getInstance()

        config.fetch().addOnSuccessListener {
            config.apply(it)
            remoteTextColor.postValue(it.getValueAsString("textColor"))
        }.addOnFailureListener {
            Log.i(Constant.HOME_VIWEMODEL_TAG, "getRemoteTextColor addOnFailureListener : " + it.message)
        }
    }

    fun setRemoteTextColorLiveData() = remoteTextColor
}