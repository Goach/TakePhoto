package com.goach.takephoto.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.goach.takephoto.R
import kotlinx.android.synthetic.main.view_load_empty_error.view.*

/**
 * @author: Goach.zhong
 * @date: 2021-01-19 21:25
 * @description:
 **/
class ErrorEmptyLoadView : ConstraintLayout {
    private var emptyRes: Drawable? = null
    private var emptyText: String? = ""
    private var errorRes: Drawable? = null
    private var errorText: String? = ""

    constructor(context: Context) : super(context) {
        initView(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs, defStyleAttr)
    }

    private fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.view_load_empty_error, this, true)
        val ta =
            context.obtainStyledAttributes(attrs, R.styleable.ErrorEmptyLoadView, defStyleAttr, 0)
                .also {
                    emptyRes = it.getDrawable(R.styleable.ErrorEmptyLoadView_empty_res)
                    emptyText = it.getString(R.styleable.ErrorEmptyLoadView_empty_text)
                    errorRes = it.getDrawable(R.styleable.ErrorEmptyLoadView_error_res)
                    errorText = it.getString(R.styleable.ErrorEmptyLoadView_error_text)
                }
        ta.recycle()
    }

    fun showLoading() {
        showView()
        pb_load.visibility = View.VISIBLE
        ll_tip.visibility = View.GONE
    }

    fun showEmpty() {
        showView()
        pb_load.visibility = View.GONE
        ll_tip.visibility = View.VISIBLE
        iv_tip.setImageDrawable(emptyRes)
        tv_tip.text = emptyText
    }

    fun showError() {
        showView()
        pb_load.visibility = View.GONE
        ll_tip.visibility = View.VISIBLE
        iv_tip.setImageDrawable(errorRes)
        tv_tip.text = errorText
    }

    fun showView(){
        visibility = View.VISIBLE
    }

    fun hideView(){
        visibility = View.GONE
    }
}