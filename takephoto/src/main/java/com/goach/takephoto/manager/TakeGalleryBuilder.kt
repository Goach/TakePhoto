package com.goach.takephoto.manager

import android.content.Context
import com.goach.takephoto.SupportTakePhotoFragment
import com.goach.takephoto.callback.TakePhotoResultListener

/**
 * @author: Goach.zhong
 * @date: 2021-01-10 17:34
 * @description:
 **/
class TakeGalleryBuilder(
    private val mCtx: Context,
    private val takePhotoFragment: SupportTakePhotoFragment
) {
    var takePhotoResultListener: TakePhotoResultListener? = null
    var isCrop: Boolean? = false
    var cropConfig: CropConfig? = null
    var multipleConfig: MultipleConfig? = null

    /**
     * 设置选择图片回调
     */
    fun setTakePhotoResultListener(listener: TakePhotoResultListener): TakeGalleryBuilder {
        this.takePhotoResultListener = listener
        return this
    }

    /**
     * 是否需要裁剪
     */
    fun setCrop(isCrop: Boolean): TakeGalleryBuilder {
        this.isCrop = isCrop
        return this
    }

    /**
     * 裁剪配置
     */
    fun setCropConfig(cropConfig: CropConfig): TakeGalleryBuilder {
        this.cropConfig = cropConfig
        return this
    }

    /**
     * 选择多张图片配置
     */
    fun setMultipleConfig(multipleConfig: MultipleConfig): TakeGalleryBuilder {
        this.multipleConfig = multipleConfig
        return this
    }

    /**
     * 开始图库选图
     */
    fun startPickFromGallery() {
        takePhotoFragment.startPickFromGallery(this)
    }

}