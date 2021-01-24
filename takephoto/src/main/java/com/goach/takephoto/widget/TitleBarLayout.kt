package com.goach.takephoto.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.goach.takephoto.R
import kotlinx.android.synthetic.main.view_title_bar.view.*

/**
 * @author: Goach.zhong
 * @date: 2021-01-17 14:32
 * @description:
 **/
class TitleBarLayout : ConstraintLayout {
    private var titleText: String? = ""
    private var titleRightText: String? = ""
    private var titleBarClickListener: TitleBarClickListener? = null

    constructor(context: Context) : super(context) {
        initAttrs(context, null, 0)
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs, 0)
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttrs(context, attrs, defStyleAttr)
        initView(context)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBarLayout, defStyleAttr, 0)
            .also {
                titleText = it.getString(R.styleable.TitleBarLayout_title_text)
                titleRightText = it.getString(R.styleable.TitleBarLayout_title_right_text)
            }
        ta.recycle()
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_title_bar, this, true)
        tv_title.text = titleText
        tv_right.text = titleRightText
        iv_back.setOnClickListener {
            titleBarClickListener?.onBackClick(it)
        }
        tv_right.setOnClickListener {
            titleBarClickListener?.onRightClick(it)
        }
    }

    fun setBackClick(backClick: TitleBarClickListener?) {
        titleBarClickListener = backClick
    }


    interface TitleBarClickListener {
        fun onBackClick(view: View)
        fun onRightClick(view: View)
    }
}