package com.goach.takephoto.viewmodel

import androidx.databinding.ObservableField
import com.goach.takephoto.base.BaseViewModel
import com.goach.takephoto.base.SimplePageAdapter
import com.goach.takephoto.databinding.ItemPreviewBinding

/**
 * @author: Goach.zhong
 * @date: 2021-01-24 10:11
 * @description:
 **/
class PreviewViewModel : BaseViewModel() {
    var currentPageText:ObservableField<String> = ObservableField("")

    var mImageList: List<String>? = null
    var mAdapter: SimplePageAdapter<ItemPreviewBinding, String>?= null
}