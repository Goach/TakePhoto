package com.goach.takephoto.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.goach.takephoto.PreviewFragment
import com.goach.takephoto.R
import com.goach.takephoto.base.BaseViewModel
import com.goach.takephoto.base.SimpleAdapter
import com.goach.takephoto.callback.BaseItemClickListener
import com.goach.takephoto.callback.MultipleItemClickListener
import com.goach.takephoto.const.CodeConst
import com.goach.takephoto.const.Const
import com.goach.takephoto.databinding.ItemMultipleTakePhotoBinding
import com.goach.takephoto.entity.FilterItem
import com.goach.takephoto.entity.Image
import com.goach.takephoto.manager.MultipleConfig
import java.io.File
import kotlin.concurrent.thread

/**
 * @author: Goach.zhong
 * @date: 2021-01-17 15:27
 * @description:
 **/
class MultipleViewModel : BaseViewModel() {
    val isShow: ObservableBoolean = ObservableBoolean(false)
    val isShowBottom: ObservableBoolean = ObservableBoolean(false)
    val isShowPreview: ObservableBoolean = ObservableBoolean(false)
    val doneStr: ObservableField<String> = ObservableField("")
    val mAllList: ObservableArrayList<Image> = ObservableArrayList()
    val mImageList: ObservableArrayList<Image> = ObservableArrayList()
    var mAdapter: SimpleAdapter<ItemMultipleTakePhotoBinding, Image>? = null
    private var mContext: Context? = null
    private var multipleConfig: MultipleConfig? = null
    var mThread: Thread? = null
    private var mContentObservable: ContentObserver? = null
    var mUIHandler: Handler? = null
    var selectPhotoList: MutableList<String> = mutableListOf()

    var parentFileMap: MutableMap<String, MutableList<String>> = mutableMapOf()
    var mSelectParentFileName: String? = ""

    var previewFragment: PreviewFragment? = null


    init {
        mAdapter = SimpleAdapter(
            R.layout.item_multiple_take_photo,
            mImageList,
            object : MultipleItemClickListener<Image> {
                override fun onItemClick(item: Image, position: Int) {
                    isShowPreview.set(true)
                    previewFragment?.updateList(mutableListOf(item.image?:""))
                }
                override fun selectClick(item: Image, position: Int) {
                    if (TextUtils.isEmpty(item.image)) return
                    var isDelete = false
                    if (selectPhotoList.contains(item.image)) {
                        selectPhotoList.remove(item.image)
                        item.isSelect = false
                        isDelete = true
                    } else {
                        val maxNumber = multipleConfig?.maxNumber ?: Const.DEFAULT_MAX_NUMBER
                        if (selectPhotoList.size >= maxNumber) {
                            mContext?.let {
                                Toast.makeText(
                                    mContext,
                                    String.format(
                                        it.getString(R.string.max_number_tip),
                                        maxNumber
                                    ),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            return
                        }
                        item.selectText = "${selectPhotoList.size + 1}"
                        item.isSelect = true
                        selectPhotoList.add(item.image!!)
                    }
                    if (isDelete) {
                        updateSelect()
                        mAdapter?.notifyItemRangeChanged(0, mImageList.size)
                    } else {
                        mAdapter?.notifyItemChanged(position)
                    }
                    isShowBottom.set(selectPhotoList.size > 0)
                    mContext?.let {
                        doneStr.set(
                            String.format(
                                it.getString(R.string.done),
                                selectPhotoList.size
                            )
                        )
                    }
                }
            })
    }

    fun startWork(context: Context?, multipleConfig: MultipleConfig?) {
        if (context == null) return
        mContext = context
        this.multipleConfig = multipleConfig
        loadPhoto(context, multipleConfig)
        mContentObservable = ChangeContentObserver(mUIHandler) {
            loadPhoto(context, multipleConfig)
        }
        context.contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            false, mContentObservable!!
        )
    }

    private fun loadPhoto(context: Context?, multipleConfig: MultipleConfig?) {
        mThread = thread(true) {
            val messageStart = mUIHandler?.obtainMessage()
            messageStart?.what = CodeConst.HANDLER_START
            messageStart?.sendToTarget()
            if (Thread.currentThread().isInterrupted) {
                val messageCancel = mUIHandler?.obtainMessage()
                messageCancel?.what = CodeConst.HANDLER_CANCEL
                messageCancel?.sendToTarget()
                return@thread
            }
            val sort: String? =
                if (multipleConfig?.photoSort == MultipleConfig.Order.DESC.ordinal) {
                    " DESC"
                } else {
                    " ASC"
                }
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = context?.applicationContext?.contentResolver?.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                arrayOf("image/jpeg", "image/png"), MediaStore.Images.Media.DATE_MODIFIED + sort
            )

            if (cursor == null) {
                val messageCancel = mUIHandler?.obtainMessage()
                messageCancel?.what = CodeConst.HANDLER_EMPTY
                messageCancel?.sendToTarget()
                return@thread
            }

            val temp = ArrayList<Image>(cursor.count)
            val albumSet: MutableSet<String> = mutableSetOf()
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    if (Thread.interrupted()) {
                        val messageCancel = mUIHandler?.obtainMessage()
                        messageCancel?.what = CodeConst.HANDLER_CANCEL
                        messageCancel?.sendToTarget()
                        return@thread
                    }
                    val image = cursor.getString(cursor.getColumnIndex(projection[0]))
                    val file = File(image)
                    if (file.exists() && !albumSet.contains(image)) {
                        temp.add(Image(image))
                        albumSet.add(image)
                        //保存父文件数据
                        val parentFileName = file.parentFile?.name
                        if (!TextUtils.isEmpty(parentFileName)) {
                            var parentList = parentFileMap[parentFileName]
                            if (parentList == null) {
                                parentList = mutableListOf()
                                parentList.add(image)
                            } else if (!parentList.contains(image)) {
                                parentList.add(image)
                            }
                            parentFileMap[parentFileName!!] = parentList
                        }
                    }
                }
            }
            cursor.close()
            if(temp.isEmpty()){
                val messageCancel = mUIHandler?.obtainMessage()
                messageCancel?.what = CodeConst.HANDLER_EMPTY
                messageCancel?.sendToTarget()
                return@thread
            }
            mImageList.clear()
            mImageList.addAll(temp)
            mAllList.clear()
            mAllList.addAll(mImageList)
            if (Thread.currentThread().isInterrupted) {
                val messageCancel = mUIHandler?.obtainMessage()
                messageCancel?.what = CodeConst.HANDLER_CANCEL
                messageCancel?.sendToTarget()
                return@thread
            }
            updateSelect()
            val message = mUIHandler?.obtainMessage()
            message?.what = CodeConst.HANDLER_COMPLETED
            message?.sendToTarget()
            Thread.interrupted()
        }
    }

    private fun updateSelect() {
        selectPhotoList.forEachIndexed { index, s ->
            mImageList.forEachIndexed { _, item ->
                if (TextUtils.equals(item.image, s)) {
                    item.isSelect = true
                    item.selectText = "${index + 1}"
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mContentObservable?.let {
            mContext?.contentResolver?.unregisterContentObserver(it)
        }
        mThread?.interrupt()
        mUIHandler?.removeCallbacksAndMessages(null)
        mUIHandler = null
    }

    fun conversionMap(): List<FilterItem> {
        val resultList = mutableListOf<FilterItem>()
        if (parentFileMap.isEmpty() || mAllList.isEmpty()) {
            return resultList
        }
        resultList.add(
            FilterItem(
                mContext?.getString(R.string.all), mAllList.size,
                mAllList.first().image, mAllList.map { it.image }.toList()
            )
        )
        parentFileMap.entries.forEach {
            if (it.value.size > 0) {
                val item = FilterItem(
                    it.key, it.value.size, it.value[0], it.value
                )
                resultList.add(item)
            }
        }
        if (TextUtils.isEmpty(mSelectParentFileName) && resultList.size > 0) {
            mSelectParentFileName = resultList[0].parentFileName
        }
        return resultList
    }

    fun previewClick() {
        isShowPreview.set(true)
        previewFragment?.updateList(selectPhotoList)
    }

    fun doneSelectClick() {
        (mContext as? Activity)?.let {
            it.setResult(Activity.RESULT_OK, Intent().also { intent ->
                intent.putExtra("selectImage", selectPhotoList.toTypedArray())
            })
            it.finish()
        }
    }


    class ChangeContentObserver(
        handler: Handler?,
        private val changeCallBack: (() -> Unit)? = null
    ) : ContentObserver(handler) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            changeCallBack?.invoke()
        }
    }
}