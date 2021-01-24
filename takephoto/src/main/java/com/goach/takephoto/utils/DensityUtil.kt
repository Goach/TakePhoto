package com.goach.takephoto.utils

import android.content.res.Resources

/**
 * @author: Goach.zhong
 * @date: 2021-01-22 15:51
 * @description:
 **/
object DensityUtil {

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}