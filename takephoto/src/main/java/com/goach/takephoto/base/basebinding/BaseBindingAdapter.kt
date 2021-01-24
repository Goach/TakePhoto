package com.goach.takephoto.base.basebinding

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File

/**
 * @author: Goach.zhong
 * @date: 2021-01-17 19:50
 * @description:
 **/
@BindingAdapter(value = ["isShow"], requireAll = false)
fun isShow(view: View, isShow: Boolean) {
    view.visibility = if (isShow) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["loadFile", "placeholder", "error"], requireAll = false)
fun loadFile(
    view: ImageView,
    loadFile: String,
    placeholder: Drawable? = null,
    error: Drawable? = null
) {
    Glide.with(view).load(File(loadFile))
        .apply(RequestOptions.placeholderOf(placeholder).error(error)).into(view)
}