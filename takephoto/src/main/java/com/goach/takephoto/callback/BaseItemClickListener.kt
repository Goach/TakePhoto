package com.goach.takephoto.callback

/**
 * @author: Goach.zhong
 * @date: 2021-01-17 18:57
 * @description:
 **/
interface BaseItemClickListener<D> {
    fun onItemClick(item:D,position:Int)
}