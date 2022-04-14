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

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.huawei.hikidskt.custom.Permission
import com.huawei.hikidskt.custom.PermissionManager
import com.huawei.hikidskt.databinding.FragmentFingerCountBinding
import com.huawei.hikidskt.ui.base.BaseFragment
import com.huawei.hikidskt.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_finger_count.*
import java.io.FileNotFoundException
import java.io.InputStream
import javax.annotation.Nullable
import androidx.lifecycle.Observer

@AndroidEntryPoint
class FingerCountFragment  : BaseFragment<FingerCountViewModel, FragmentFingerCountBinding>() {

    private val fingerCountViewModel: FingerCountViewModel by viewModels()

    val REQUEST_IMAGE_CAPTURE = 1
    val SELECT_IMAGE_CODE = 2
    var bitmap: Bitmap? = null
    private val permissionManager = PermissionManager.from(this)

    override fun getFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFingerCountBinding {
        return FragmentFingerCountBinding.inflate(inflater, container, false)
    }

    override fun getViewModel(): FingerCountViewModel = fingerCountViewModel

    override fun setupUi() {
        super.setupUi()

        btnChoosePhoto2.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            try {
                startActivityForResult(Intent.createChooser(intent, "img"), SELECT_IMAGE_CODE)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }

        btnClassify2.setOnClickListener {
            if (bitmap != null) {
                fingerCountViewModel.createAnalyzer(bitmap!!)
            } else {
                Toast.makeText(context, "You have to select or take a photo", Toast.LENGTH_SHORT).show();
            }
        }

        btnTakePhoto2.setOnClickListener {
            permissionManager
                .request(Permission.Camera)
                .rationale("We need permission to see your fingers.")
                .checkPermission { granted ->
                    if (granted) {
                        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        try {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                        }
                    } else {
                        Log.i(Constant.IMAGE_CLASSIFICATION_TAG,"We couldn't access the camera.")
                    }
                }
        }

        listenEnglish.setOnClickListener {
            fingerCountViewModel.callTTS(textView_EnResult.text.toString())
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        fingerCountViewModel.getAnalayzeResultLiveData().observe(viewLifecycleOwner, Observer {
            relativeLayout_EN.visibility = View.VISIBLE
            if (it != null){
                textView_EnResult.text = it
            }else{
                textView_EnResult.text = "Error"
            }
        })

        fingerCountViewModel.getTranslatedAnalayzeResultLiveData().observe(viewLifecycleOwner, Observer {
            relativeLayout_TR.visibility = View.VISIBLE
            if (it != null){
                textView_TrResult.text = it
            }else{
                textView_TrResult.text = "Hata"
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_IMAGE_CODE) {
            try {
                val inputStream: InputStream = context?.getContentResolver()?.openInputStream(data?.data!!)!!
                bitmap = BitmapFactory.decodeStream(inputStream)
                viewImage2.setImageBitmap(bitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            bitmap = extras!!["data"] as Bitmap?
            viewImage2.setImageBitmap(bitmap)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}