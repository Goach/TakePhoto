package com.goach.takephoto.base

import androidx.databinding.ViewDataBinding
import com.goach.takephoto.callback.BaseItemClickListener

/**
 * @author: Goach.zhong
 * @date: 2021-01-17 19:20
 * @description:
 **/
open class SimpleAdapter<T : ViewDataBinding, D>(private var layoutId: Int, mDataList: List<D>?) :
    BaseMVVMAdapter<T, D>(mDataList) {
    constructor(layoutId: Int, dataList: List<D>?, itemClick: BaseItemClickListener<D>?) : this(
        layoutId,
        dataList
    ) {
        this.layoutId = layoutId
        this.mDataList = dataList
        this.itemClick = itemClick
    }

    override fun getLayoutId(): Int = layoutId

}