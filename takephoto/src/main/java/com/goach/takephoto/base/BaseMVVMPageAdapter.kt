package com.goach.takephoto.base

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewpager.widget.PagerAdapter
import com.goach.takephoto.BR
import com.goach.takephoto.callback.BaseItemClickListener


/**
 * @author: Goach.zhong
 * @date: 2021-01-24 10:23
 * @description:
 **/
abstract class BaseMVVMPageAdapter<T : ViewDataBinding, D>(var mDataList: List<D>? = mutableListOf()) :
    PagerAdapter() {
    var mSparseArray: SparseArray<View> = SparseArray()
    var dataBinding: T? = null
    var itemClick: BaseItemClickListener<D>? = null
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int = mDataList!!.size
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view = mSparseArray[position]
        if (view == null) {
            view = LayoutInflater.from(container.context).inflate(getLayoutId(), container, false)
            mSparseArray.put(position,view)
        }
        dataBinding = DataBindingUtil.bind(view)
        dataBinding?.setVariable(BR.position, position)
        dataBinding?.setVariable(BR.data, mDataList!![position])
        dataBinding?.setVariable(BR.itemClick, getItemClickListener())
        dataBinding?.executePendingBindings()
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    private fun getItemClickListener(): BaseItemClickListener<D>? {
        return itemClick
    }

    fun update(){
        mSparseArray.forEach { key, _ ->
            dataBinding?.setVariable(BR.position, key)
            dataBinding?.setVariable(BR.data, mDataList!![key])
        }
    }
}