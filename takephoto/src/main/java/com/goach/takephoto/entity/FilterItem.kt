package com.goach.takephoto.entity

import java.io.Serializable

/**
 * @author: Goach.zhong
 * @date: 2021-01-22 16:39
 * @description:
 **/
data class FilterItem(
    var parentFileName: String? = "", var total: Int? = 0, var coverFilePath: String? = "",
    var childList: List<String?>
) : Serializable {
}