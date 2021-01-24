package com.goach.takephoto.callback

import android.graphics.Bitmap

/**
 * @author: Goach.zhong
 * @date: 2021-01-16 11:01
 * @description:获取Bitmap图片回调
 **/
interface CropBitmapListener {
    fun onCropBitmap(bitmap: Bitmap)

    fun onCropBitmapFailure()
}