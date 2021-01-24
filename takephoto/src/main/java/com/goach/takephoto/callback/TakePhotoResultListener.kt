package com.goach.takephoto.callback

/**
 * @author: Goach.zhong
 * @date: 2021-01-12 21:24
 * @description:获取结果接口
 **/
interface TakePhotoResultListener {
    /**
     * 取消选择图片回调
     */
    fun onTakeCancel()

    /**
     * 获取图片成功
     */
    fun onTakeSuccess(takePhotoPath:List<String>)

    /**
     * 获取图片失败
     */
    fun onTakeFailure()
}