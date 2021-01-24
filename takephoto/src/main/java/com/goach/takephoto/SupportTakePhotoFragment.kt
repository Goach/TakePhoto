package com.goach.takephoto

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Parcelable
import android.provider.MediaStore
import android.text.TextUtils
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.goach.takephoto.const.CodeConst
import com.goach.takephoto.manager.TakeCaptureBuilder
import com.goach.takephoto.manager.TakeGalleryBuilder
import com.goach.takephoto.manager.TakePhotoManager
import com.goach.takephoto.utils.FileUtils
import com.goach.takephoto.utils.IntentUtils
import java.io.File

/**
 * @author: Goach.zhong
 * @date: 2021-01-10 12:18
 * @description:
 **/
class SupportTakePhotoFragment : Fragment() {
    private var takePhotoManager: TakePhotoManager? = null
    private var takeCaptureBuilder: TakeCaptureBuilder? = null
    private var takeGalleryBuilder: TakeGalleryBuilder? = null
    private var isStartCapture: Boolean = false
    private var isStartGallery: Boolean = false
    private var targetUri: Uri? = null
    private var mSelectArr:Array<String>? = arrayOf()
    private var mCropList:MutableList<String> = mutableListOf()//裁剪多张图片存储
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (isStartCapture) {
            takeCaptureBuilder?.let {
                startPickFromCapture(it)
            }
        }
        if (isStartGallery) {
            takeGalleryBuilder?.let {
                startPickFromGallery(it)
            }
        }
    }

    fun getTakePhotoManager(): TakePhotoManager? {
        return takePhotoManager
    }

    fun setTakePhotoManager(takePhotoManager: TakePhotoManager) {
        this.takePhotoManager = takePhotoManager
    }

    fun startPickFromCapture(builder: TakeCaptureBuilder) {
        this.takeCaptureBuilder = builder
        isStartCapture = true
        context?.let {
            isStartCapture = false
            if (!it.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                return
            }
            var outputFile = takeCaptureBuilder?.outFile
            if (outputFile == null) {
                outputFile = FileUtils.createFileDirFile(
                    it,
                    FileUtils.FILE_NAME_PREFIX + System.currentTimeMillis() + ".jpg"
                )
                takeCaptureBuilder?.setOutFile(outputFile)
            }
            val intent = IntentUtils.getCaptureIntent(it as Activity, outputFile)
            targetUri = intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT)
            startActivityForResult(intent, CodeConst.REQUEST_CODE_CAPTURE)
        }
    }

    fun startPickFromGallery(builder: TakeGalleryBuilder) {
        this.takeGalleryBuilder = builder
        isStartGallery = true
        context?.let {
            isStartGallery = false
            if (builder.multipleConfig != null) {//选择多张图片
                startActivityForResult(
                    Intent(
                        it,
                        MultipleTakePhotoActivity::class.java
                    ).also { intent ->
                        intent.putExtra("multipleConfig", builder.multipleConfig!! as Parcelable)
                    }, CodeConst.REQUEST_CODE_GALLERY_MULTIPLE
                )
                return@let
            }
            startActivityForResult(IntentUtils.geGalleryIntent(), CodeConst.REQUEST_CODE_GALLERY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {//取消选择
            if (requestCode == CodeConst.REQUEST_CODE_CAPTURE_CROP) {
                takeCaptureBuilder?.cropConfig?.let {
                    it.getCropBitmapListener()?.onCropBitmapFailure()
                }
            }
            if (requestCode == CodeConst.REQUEST_CODE_CAPTURE || requestCode == CodeConst.REQUEST_CODE_CAPTURE_CROP) {
                takeCaptureBuilder?.let {
                    it.takePhotoResultListener?.onTakeCancel()
                }
            }
            if (requestCode == CodeConst.REQUEST_CODE_GALLERY_CROP) {
                takeGalleryBuilder?.cropConfig?.let {
                    it.getCropBitmapListener()?.onCropBitmapFailure()
                }
            }
            if (requestCode == CodeConst.REQUEST_CODE_GALLERY || requestCode == CodeConst.REQUEST_CODE_GALLERY_CROP) {//取消从图库选择照片
                takeGalleryBuilder?.let {
                    it.takePhotoResultListener?.onTakeCancel()
                }
            }
            return
        }
        if (requestCode == CodeConst.REQUEST_CODE_CAPTURE) {//拍摄成功结果
            takeCaptureBuilder?.let {
                when {
                    it.isCrop == true -> {
                        if (targetUri == null || activity == null) {
                            it.cropConfig?.getCropBitmapListener()?.onCropBitmapFailure()
                            it.takePhotoResultListener?.onTakeFailure()
                        } else {//去裁剪
                            var outCropFile = it.cropConfig?.getOutCropFile()
                            if (outCropFile == null) {
                                outCropFile = FileUtils.createFileDirFile(
                                    activity!!,
                                    FileUtils.FILE_CROP_NAME_PREFIX + System.currentTimeMillis() + ".jpg"
                                )
                                it.cropConfig?.setOutCropFile(outCropFile)
                            }
                            startActivityForResult(
                                IntentUtils.getCropIntent(
                                    activity!!,
                                    targetUri!!,
                                    outCropFile,
                                    it.cropConfig
                                ), CodeConst.REQUEST_CODE_CAPTURE_CROP
                            )
                        }
                    }
                    it.outFile?.exists() == true -> {
                        it.takePhotoResultListener?.onTakeSuccess(mutableListOf(it.outFile!!.absolutePath))
                    }
                    else -> {
                        it.takePhotoResultListener?.onTakeFailure()
                    }
                }
            }
            return
        }
        if (requestCode == CodeConst.REQUEST_CODE_GALLERY) {//图库选择照片成功
            takeGalleryBuilder?.let {
                if (it.isCrop == true) {
                    if (data?.data == null || activity == null) {
                        it.cropConfig?.getCropBitmapListener()?.onCropBitmapFailure()
                        it.takePhotoResultListener?.onTakeFailure()
                    } else {//去裁剪
                        var outCropFile = it.cropConfig?.getOutCropFile()
                        if (outCropFile == null) {
                            outCropFile = FileUtils.createFileDirFile(
                                activity!!,
                                FileUtils.FILE_CROP_NAME_PREFIX + System.currentTimeMillis() + ".jpg"
                            )
                            it.cropConfig?.setOutCropFile(outCropFile)
                        }
                        startActivityForResult(
                            IntentUtils.getCropIntent(
                                activity!!,
                                data.data!!,
                                outCropFile!!,
                                it.cropConfig
                            ), CodeConst.REQUEST_CODE_GALLERY_CROP
                        )
                    }
                    return
                }
                val outFilePath = FileUtils.getFileForUri(activity, data?.data)
                if (!TextUtils.isEmpty(outFilePath) && File(outFilePath!!).exists()) {
                    it.takePhotoResultListener?.onTakeSuccess(mutableListOf(outFilePath))
                } else {
                    it.takePhotoResultListener?.onTakeFailure()
                }
            }
            return
        }
        if (requestCode == CodeConst.REQUEST_CODE_CAPTURE_CROP) {//拍照裁剪结果
            takeCaptureBuilder?.let {
                if (it.cropConfig?.getOutCropFile()?.exists() == true) {
                    it.cropConfig?.getCropBitmapListener()
                        ?.onCropBitmap(BitmapFactory.decodeFile(it.cropConfig!!.getOutCropFile()!!.absolutePath))
                    it.takePhotoResultListener?.onTakeSuccess(mutableListOf(it.cropConfig!!.getOutCropFile()!!.absolutePath))
                } else {
                    it.cropConfig?.getCropBitmapListener()?.onCropBitmapFailure()
                    it.takePhotoResultListener?.onTakeFailure()
                }
            }
            return
        }
        if (requestCode == CodeConst.REQUEST_CODE_GALLERY_CROP) {//图库图片裁剪结果
            takeGalleryBuilder?.let {
                if (it.cropConfig?.getOutCropFile()?.exists() == true) {
                    it.cropConfig?.getCropBitmapListener()
                        ?.onCropBitmap(BitmapFactory.decodeFile(it.cropConfig!!.getOutCropFile()!!.absolutePath))
                    it.takePhotoResultListener?.onTakeSuccess(mutableListOf(it.cropConfig!!.getOutCropFile()!!.absolutePath))
                } else {
                    it.cropConfig?.getCropBitmapListener()?.onCropBitmapFailure()
                    it.takePhotoResultListener?.onTakeFailure()
                }
            }
            return
        }

        if (requestCode == CodeConst.REQUEST_CODE_GALLERY_MULTIPLE) {//获取多张图片
            mSelectArr = data?.getStringArrayExtra("selectImage")
            if (mSelectArr == null || mSelectArr!!.isEmpty()) {
                takeGalleryBuilder?.let {
                    it.takePhotoResultListener?.onTakeFailure()
                }
            } else {
                takeGalleryBuilder?.let {
                    if (it.isCrop == true) {
                        if (activity == null) {
                            it.cropConfig?.getCropBitmapListener()?.onCropBitmapFailure()
                            it.takePhotoResultListener?.onTakeFailure()
                        } else {//去裁剪
                            mCropList.clear()
                            var outCropFile:File? = null
                            if(mCropList.size < it.cropConfig?.getMultipleCropFile()?.size?:0){
                                outCropFile = it.cropConfig?.getMultipleCropFile()?.get(mCropList.size)
                            }
                            if (outCropFile == null) {
                                outCropFile = FileUtils.createFileDirFile(
                                    activity!!,
                                    FileUtils.FILE_CROP_NAME_PREFIX + System.currentTimeMillis() + ".jpg"
                                )
                            }
                            it.cropConfig?.setOutCropFile(outCropFile)
                            startActivityForResult(
                                IntentUtils.getCropIntent(
                                    activity!!,
                                    FileUtils.getUriForFile(activity!!,File(mSelectArr!![mCropList.size])),
                                    outCropFile!!,
                                    it.cropConfig
                                ), CodeConst.REQUEST_CODE_GALLERY_MULTIPLE_CROP
                            )
                        }
                        return
                    }
                    it.takePhotoResultListener?.onTakeSuccess(mSelectArr!!.toList())
                }
            }
            return
        }
        if (requestCode == CodeConst.REQUEST_CODE_GALLERY_MULTIPLE_CROP) {//获取多张图片裁剪结果
            takeGalleryBuilder?.let {
                if (it.cropConfig?.getOutCropFile()?.exists() == true) {
                    it.cropConfig?.getCropBitmapListener()
                        ?.onCropBitmap(BitmapFactory.decodeFile(it.cropConfig!!.getOutCropFile()!!.absolutePath))
                    mCropList.add(it.cropConfig!!.getOutCropFile()!!.absolutePath)
                    if(mCropList.size >= mSelectArr?.size?:0){
                        it.takePhotoResultListener?.onTakeSuccess(mCropList)
                    }else{//继续去裁剪
                        var outCropFile:File? = null
                        if(mCropList.size < it.cropConfig?.getMultipleCropFile()?.size?:0){
                            outCropFile = it.cropConfig?.getMultipleCropFile()?.get(mCropList.size)
                        }
                        if (outCropFile == null) {
                            outCropFile = FileUtils.createFileDirFile(
                                activity!!,
                                FileUtils.FILE_CROP_NAME_PREFIX + System.currentTimeMillis() + ".jpg"
                            )
                        }
                        it.cropConfig?.setOutCropFile(outCropFile)
                        startActivityForResult(
                            IntentUtils.getCropIntent(
                                activity!!,
                                FileUtils.getUriForFile(activity!!,File(mSelectArr!![mCropList.size])),
                                outCropFile!!,
                                it.cropConfig
                            ), CodeConst.REQUEST_CODE_GALLERY_MULTIPLE_CROP
                        )
                    }
                } else {
                    it.cropConfig?.getCropBitmapListener()?.onCropBitmapFailure()
                    it.takePhotoResultListener?.onTakeFailure()
                }
            }
            return
        }
    }
}