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

package com.huawei.hikidskt.custom

import android.Manifest.permission.*

sealed class Permission(vararg val permissions: String) {
    // Individual permissions
    object Camera : Permission(CAMERA)

    // Bundled permissions
    object MandatoryForFeatureOne : Permission(WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION)

    // Grouped permissions
    object Location : Permission(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
    object Storage : Permission(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)


    companion object {
        fun from(permission: String) = when (permission) {
            ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION -> Location
            WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE -> Storage
            CAMERA -> Camera
            else -> throw IllegalArgumentException("Unknown permission: $permission")
        }
    }
}