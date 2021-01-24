package com.goach.takephoto.base

import androidx.databinding.BaseObservable

/**
 * @author: Goach.zhong
 * @date: 2021-01-17 14:29
 * @description:
 **/
abstract class BaseViewModel : BaseObservable() {

    open fun onDestroy(){

    }
}