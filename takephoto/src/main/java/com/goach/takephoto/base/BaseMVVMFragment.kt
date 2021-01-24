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
abstract class BaseMVVMFragment<T : ViewDataBinding, M : BaseViewModel> : Fragment() {
    var dataBinding: T? = null
    var viewModel: M? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutId(),container,false)
        viewModel = createViewModel()
        dataBinding!!.setVariable(BR.viewModel, viewModel)
        return dataBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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