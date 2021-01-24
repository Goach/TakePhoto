package com.goach.takephoto.manager

import android.content.Context
import com.goach.takephoto.SupportTakePhotoFragment
import com.goach.takephoto.callback.TakePhotoResultListener
import java.io.File

/**
 * @author: Goach.zhong
 * @date: 2021-01-10 17:34
 * @description:
 **/
class TakeCaptureBuilder(
    private val mCtx: Context,
    private val takePhotoFragment: SupportTakePhotoFragment
) {

    var outFile: File? = null
    var takePhotoResultListener: TakePhotoResultListener? = null

    var isCrop: Boolean? = false
    var cropConfig: CropConfig? = null

    /**
     * 拍完输出的File
     */
    fun setOutFile(outFile: File): TakeCaptureBuilder {
        this.outFile = outFile
        return this
    }

    /**
     * 设置选择图片回调
     */
    fun setTakePhotoResultListener(listener: TakePhotoResultListener): TakeCaptureBuilder {
        this.takePhotoResultListener = listener
        return this
    }

    /**
     * 是否需要裁剪
     */
    fun setCrop(isCrop: Boolean): TakeCaptureBuilder {
        this.isCrop = isCrop
        return this
    }

    /**
     * 裁剪配置
     */
    fun setCropConfig(cropConfig: CropConfig): TakeCaptureBuilder {
        this.cropConfig = cropConfig
        return this
    }


    /**
     * 开始拍照选图
     */
    fun startPickFromCapture() {
        takePhotoFragment.startPickFromCapture(this)
    }

}