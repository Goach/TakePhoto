package com.goach.takephoto

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import com.goach.takephoto.base.SimpleAdapter
import com.goach.takephoto.callback.BaseItemClickListener
import com.goach.takephoto.databinding.ItemFilterBinding
import com.goach.takephoto.entity.FilterItem
import com.goach.takephoto.utils.DensityUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_filter_button_sheet.*

/**
 * @author: Goach.zhong
 * @date: 2021-01-22 15:32
 * @description: 选择照片过滤列表
 **/
class FilterBottomSheetFragment : BottomSheetDialogFragment() {
    private var mDataList: MutableList<FilterItem> = mutableListOf()
    private var mSelectParentFileName: String? = ""
    private var mFilterResultListener: FilterResultListener? = null

    companion object {
        fun newInstance(
            filterList: List<FilterItem>,
            selectParentFileName: String? = "",
            filterResultListener: FilterResultListener? = null
        ): FilterBottomSheetFragment {
            return FilterBottomSheetFragment().also {
                it.arguments = bundleOf(
                    Pair("filterList", filterList),
                    Pair("selectParentFileName", selectParentFileName)
                )
                it.setFilterResultListener(filterResultListener)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.let {
            configWindow(it)
        }
        return LayoutInflater.from(context)
            .inflate(R.layout.dialog_filter_button_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initParams()
        initView()
    }

    private fun configWindow(window: Window) {
        window.attributes.let {
            it.width = WindowManager.LayoutParams.MATCH_PARENT
            it.height = (DensityUtil.getScreenHeight() * 0.6f).toInt()
        }
    }

    private fun initParams() {
        mDataList = (arguments?.getSerializable("filterList") as? MutableList<FilterItem>)
            ?: mutableListOf()
        mSelectParentFileName = arguments?.getString("selectParentFileName")
    }

    private fun initView() {
        rv_filter.adapter = object : SimpleAdapter<ItemFilterBinding, FilterItem>(
            R.layout.item_filter,
            mDataList,
            object : BaseItemClickListener<FilterItem> {
                override fun onItemClick(item: FilterItem, position: Int) {
                    mSelectParentFileName = item.parentFileName
                    rv_filter.adapter?.notifyDataSetChanged()
                    mFilterResultListener?.onFilterResult(mSelectParentFileName!!)
                    dismissAllowingStateLoss()
                }
            }) {
            override fun onBindViewHolder(
                holder: BaseViewHolder<ItemFilterBinding>,
                position: Int
            ) {
                super.onBindViewHolder(holder, position)
                holder.dataBinding?.selectParentFileName = mSelectParentFileName
            }
        }
    }

    fun setFilterResultListener(filterResultListener: FilterResultListener?) {
        this.mFilterResultListener = filterResultListener
    }

    interface FilterResultListener {
        fun onFilterResult(parentFileName: String)
    }
}