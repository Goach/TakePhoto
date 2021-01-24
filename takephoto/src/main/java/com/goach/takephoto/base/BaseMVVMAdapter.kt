package com.goach.takephoto.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.goach.takephoto.BR
import com.goach.takephoto.callback.BaseItemClickListener

/**
 * @author: Goach.zhong
 * @date: 2021-01-17 18:42
 * @description:
 **/
abstract class BaseMVVMAdapter<T : ViewDataBinding, D>(var mDataList: List<D>?) :
    RecyclerView.Adapter<BaseMVVMAdapter.BaseViewHolder<T>>() {
    var itemClick: BaseItemClickListener<D>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        return BaseViewHolder(
            LayoutInflater.from(parent.context).inflate(getLayoutId(), parent, false)
        )
    }

    override fun getItemCount(): Int = mDataList?.size ?: 0

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.dataBinding?.setVariable(BR.position, position)
        holder.dataBinding?.setVariable(BR.data, mDataList!![position])
        holder.dataBinding?.setVariable(BR.itemClick, getItemClickListener())
        holder.dataBinding?.executePendingBindings()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    private fun getItemClickListener(): BaseItemClickListener<D>? {
        return itemClick
    }

    class BaseViewHolder<T : ViewDataBinding>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dataBinding: T? = null

        init {
            dataBinding = DataBindingUtil.bind(itemView)
        }
    }
}