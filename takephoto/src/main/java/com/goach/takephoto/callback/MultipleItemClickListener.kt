package com.goach.takephoto.callback

/**
 * @author: Goach.zhong
 * @date: 2021-01-24 11:36
 * @description:
 **/
interface MultipleItemClickListener<D> : BaseItemClickListener<D> {
    fun selectClick(item:D,position:Int)
}