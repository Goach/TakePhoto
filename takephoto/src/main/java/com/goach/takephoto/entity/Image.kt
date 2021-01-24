package com.goach.takephoto.entity

import java.io.Serializable

/**
 * @author: Goach.zhong
 * @date: 2021-01-17 15:36
 * @description:
 **/
data class Image(
    var image: String? = "",
    var isSelect: Boolean? = false,
    var selectText: String? = ""
) :
    Serializable {
}