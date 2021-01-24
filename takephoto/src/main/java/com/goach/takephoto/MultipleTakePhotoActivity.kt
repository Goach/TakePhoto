package com.goach.takephoto

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.goach.takephoto.base.BaseMVVMActivity
import com.goach.takephoto.const.CodeConst
import com.goach.takephoto.databinding.FragmentMultipleTakePhotoBinding
import com.goach.takephoto.entity.Image
import com.goach.takephoto.manager.MultipleConfig
import com.goach.takephoto.viewmodel.MultipleViewModel
import com.goach.takephoto.widget.TitleBarLayout
import kotlinx.android.synthetic.main.fragment_multiple_take_photo.*


/**
 * @author: Goach.zhong
 * @date: 2021-01-17 14:12
 * @description:选择多张图片
 **/
class MultipleTakePhotoActivity :
    BaseMVVMActivity<FragmentMultipleTakePhotoBinding, MultipleViewModel>() {

    private var multipleConfig: MultipleConfig? = null

    private var mUIHandler: Handler? = Handler(Looper.getMainLooper(), Handler.Callback {
        when (it.what) {
            CodeConst.HANDLER_START -> {
                eel_view.showLoading()
            }
            CodeConst.HANDLER_COMPLETED -> {
                eel_view.hideView()
                viewModel?.isShow?.set(true)
                viewModel?.mAdapter?.notifyDataSetChanged()
            }
            CodeConst.HANDLER_EMPTY -> {
                eel_view.showEmpty()
            }
            else -> {

            }
        }
        return@Callback false
    })

    override fun getLayoutId(): Int = R.layout.fragment_multiple_take_photo

    override fun createViewModel(): MultipleViewModel = MultipleViewModel()

    override fun initParams() {
        super.initParams()
        intent.extras?.let {
            multipleConfig = it.getParcelable("multipleConfig")
        }
    }

    override fun initView() {
        dataBinding?.rvList?.adapter = viewModel?.mAdapter
        dataBinding?.rvList?.itemAnimator?.changeDuration = 0
        viewModel?.mUIHandler = mUIHandler
        viewModel?.startWork(this, multipleConfig)
        eel_view.visibility = View.GONE
        eel_view.showLoading()
        viewModel?.previewFragment = PreviewFragment.newInstance(viewModel?.selectPhotoList,object:TitleBarLayout.TitleBarClickListener{
            override fun onBackClick(view: View) {
                viewModel?.isShowPreview?.set(false)
            }

            override fun onRightClick(view: View) {
            }

        })
        viewModel?.previewFragment?.let {
            supportFragmentManager.beginTransaction().replace(R.id.fl_preview, it)
                .commitAllowingStateLoss()
        }
        title_bar.setBackClick(object : TitleBarLayout.TitleBarClickListener {
            override fun onBackClick(view: View) {
                onBackPressed()
            }

            override fun onRightClick(view: View) {
                if(viewModel?.conversionMap()?.isEmpty() == true){
                    Toast.makeText(this@MultipleTakePhotoActivity,getString(R.string.empty_text),Toast.LENGTH_SHORT).show()
                    return
                }
                FilterBottomSheetFragment.newInstance(viewModel?.conversionMap() ?: mutableListOf(),
                    viewModel?.mSelectParentFileName,
                    object : FilterBottomSheetFragment.FilterResultListener {
                        override fun onFilterResult(parentFileName: String) {
                            viewModel?.mSelectParentFileName = parentFileName
                            viewModel?.mImageList?.clear()
                            viewModel?.selectPhotoList?.clear()
                            if (TextUtils.equals(
                                    viewModel?.mSelectParentFileName,
                                    getString(R.string.all)
                                )
                            ) {
                                viewModel?.mAllList?.let {
                                    viewModel?.mImageList?.addAll(it.map { item ->
                                        Image(item.image)
                                    })
                                }
                            } else {
                                viewModel?.parentFileMap?.get(parentFileName)?.let {
                                    viewModel?.mImageList?.addAll(it.map { path ->
                                        Image(path)
                                    })
                                }
                            }
                            viewModel?.isShowBottom?.set(viewModel?.selectPhotoList?.size ?: 0 > 0)
                            viewModel?.mAdapter?.notifyDataSetChanged()
                        }
                    })
                    .show(supportFragmentManager, FilterBottomSheetFragment::class.java.simpleName)
            }
        })
    }

    override fun onBackPressed() {
        if(viewModel?.isShowPreview?.get() == true){
            viewModel?.isShowPreview?.set(false)
        }else{
            super.onBackPressed()
        }
    }

}