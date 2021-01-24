package com.goach.takephoto.manager

import android.content.Context
import com.goach.takephoto.SupportTakePhotoFragment

/**
 * @author: Goach.zhong
 * @date: 2021-01-10 14:19
 * @description:
 **/
class TakePhotoManager(
    private val mCtx: Context,
    private val takePhotoFragment: SupportTakePhotoFragment
) {

    fun asCaptureBuilder(): TakeCaptureBuilder {
        return TakeCaptureBuilder(mCtx, takePhotoFragment)
    }

    fun asGalleryBuilder(): TakeGalleryBuilder {
        return TakeGalleryBuilder(mCtx, takePhotoFragment)
    }
}