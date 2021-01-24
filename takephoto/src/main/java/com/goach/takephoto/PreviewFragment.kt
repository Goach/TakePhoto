package com.goach.takephoto

import androidx.core.os.bundleOf
import androidx.viewpager.widget.ViewPager
import com.goach.takephoto.base.BaseMVVMFragment
import com.goach.takephoto.base.SimplePageAdapter
import com.goach.takephoto.callback.BaseItemClickListener
import com.goach.takephoto.databinding.FragmentPreviewBinding
import com.goach.takephoto.viewmodel.PreviewViewModel
import com.goach.takephoto.widget.TitleBarLayout
import kotlinx.android.synthetic.main.fragment_preview.*

/**
 * @author: Goach.zhong
 * @date: 2021-01-24 10:04
 * @description:预览图片
 **/
class PreviewFragment : BaseMVVMFragment<FragmentPreviewBinding, PreviewViewModel>() {
    var mTitleBarClickListener: TitleBarLayout.TitleBarClickListener ?= null


    companion object {
        fun newInstance(imageList: List<String>?,titleBarClickListener: TitleBarLayout.TitleBarClickListener? = null): PreviewFragment {
            return PreviewFragment().also {
                it.arguments = bundleOf(Pair("imageList", imageList?.toTypedArray()))
                it.mTitleBarClickListener = titleBarClickListener
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_preview

    override fun createViewModel(): PreviewViewModel = PreviewViewModel()
    override fun initParams() {
        super.initParams()
        viewModel?.mImageList = arguments?.getStringArray("imageList")?.toList()
    }

    override fun initView() {
        viewModel?.mAdapter = SimplePageAdapter(
            R.layout.item_preview,
            viewModel?.mImageList,
            object : BaseItemClickListener<String> {
                override fun onItemClick(item: String, position: Int) {

                }
            })
        vp_image.adapter = viewModel?.mAdapter
        viewModel?.currentPageText?.set("1/${viewModel?.mImageList?.size ?: 0}")
        vp_image.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                viewModel?.currentPageText?.set("${position + 1}/${viewModel?.mImageList?.size ?: 0}")
            }

        })
        title_bar.setBackClick(mTitleBarClickListener)
    }

    fun updateList(imageList: List<String>?) {
        viewModel?.mImageList = imageList
        viewModel?.currentPageText?.set("1/${viewModel?.mImageList?.size ?: 0}")
        initView()
    }
}