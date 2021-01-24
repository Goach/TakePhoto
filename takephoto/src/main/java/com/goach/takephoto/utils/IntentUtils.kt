package com.goach.takephoto.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.goach.takephoto.manager.CropConfig
import java.io.File


/**
 * @author: Goach.zhong
 * @date: 2021-01-10 12:25
 * @description:
 **/
object IntentUtils {
    /**
     * 手机拍照获取图片
     */
    fun getCaptureIntent(act: Activity, outPutFile: File?): Intent {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(act.packageManager)?.also {
                outPutFile?.also {
                    val photoURI: Uri =
                        FileProvider.getUriForFile(act, act.packageName + ".fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                }
            }
        }
    }

    /**
     * 裁剪Intent
     * @param targetUri 裁剪的图片
     * @param outCropFile 裁剪完输出的文件
     */
    fun getCropIntent(
        act: Activity,
        targetUri: Uri,
        outCropFile: File,
        cropConfig: CropConfig?
    ): Intent {
        val outPutUri: Uri =
            FileProvider.getUriForFile(act, act.packageName + ".fileprovider", outCropFile)
        return Intent("com.android.camera.action.CROP").also { cropIntent ->
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            cropIntent.setDataAndType(targetUri, "image/*")
            cropIntent.putExtra("crop", "true")
            cropIntent.putExtra("aspectX", cropConfig?.getAspectX() ?: 1)
            cropIntent.putExtra("aspectY", cropConfig?.getAspectY() ?: 1)
            cropIntent.putExtra("outputX", cropConfig?.getOutputX() ?: 500)
            cropIntent.putExtra("outputY", cropConfig?.getOutputY() ?: 500)
            cropIntent.putExtra("scale", cropConfig?.getScale() ?: true)//是否保留比例
            cropIntent.putExtra("return-data", true)
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri)
            cropIntent.putExtra("outputFormat", cropConfig?.getOutputFormat()?.toString())//输出图片的格式
            cropIntent.putExtra("noFaceDetection", cropConfig?.getNoFaceDetection()) // 头像识别

            val resInfoList: List<ResolveInfo> =
                act.packageManager.queryIntentActivities(
                    cropIntent,
                    PackageManager.MATCH_DEFAULT_ONLY
                )
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                act.grantUriPermission(
                    packageName,
                    outPutUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }
    }

    /**
     * 从图库里面获取图片
     */
    fun geGalleryIntent():Intent{
        return Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
        }
    }

}