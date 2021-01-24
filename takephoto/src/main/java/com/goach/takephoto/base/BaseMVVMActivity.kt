package com.goach.takephoto.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.goach.takephoto.BR

/**
 * @author: Goach.zhong
 * @date: 2021-01-17 14:23
 * @description:
 **/
abstract class BaseMVVMActivity<T : ViewDataBinding, M : BaseViewModel> : AppCompatActivity() {
    var dataBinding: T? = null
    var viewModel: M? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        viewModel = createViewModel()
        dataBinding!!.setVariable(BR.viewModel, viewModel)
        initParams()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.onDestroy()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun createViewModel(): M

    open fun initParams(){}
    abstract fun initView()
}