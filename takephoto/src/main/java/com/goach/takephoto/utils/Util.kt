package com.goach.takephoto.utils

import android.os.Looper

/**
 * @author: Goach.zhong
 * @date: 2021-01-10 14:22
 * @description:
 **/
object Util {
    fun isOnMainThread():Boolean{
        return Looper.myLooper() == Looper.getMainLooper()
    }
    fun isOnBackgroundThread():Boolean{
        return !isOnMainThread()
    }
}