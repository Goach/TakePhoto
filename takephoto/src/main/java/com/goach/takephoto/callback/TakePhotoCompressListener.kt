package com.goach.takephoto.callback

/**
 * @author: Goach.zhong
 * @date: 2021-01-12 21:31
 * @description: 压缩获取压缩结果回调
 **/
interface TakePhotoCompressListener : TakePhotoResultListener{
    /**
     * 压缩成功回调
     */
    fun onTakeCompressSuccess()

    /**
     * 压缩失败回调
     */
    fun onTakeCompressFailure()
}