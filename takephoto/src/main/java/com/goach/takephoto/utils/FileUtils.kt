package com.goach.takephoto.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import androidx.core.content.FileProvider
import java.io.File


/**
 * @author: Goach.zhong
 * @date: 2021-01-10 12:21
 * @description:
 **/
object FileUtils {
    const val FILE_NAME_PREFIX = "take_photo_"
    const val FILE_CROP_NAME_PREFIX = "take_photo_crop_"

    fun createFileDirFile(ctx: Context, fileName: String): File {
        val fileDir = ctx.filesDir
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }
        val resultFile = File(fileDir, fileName)
        if (resultFile.exists()) {
            resultFile.delete()
        }
        resultFile.createNewFile()
        return resultFile
    }

    /**
     * 通过文件获取Uri
     */
    fun getUriForFile(ctx: Context,file:File):Uri{
        return FileProvider.getUriForFile(ctx,"${ctx.packageName}.fileprovider", file)
    }

    /**
     * 通过Uri获取文件路径
     */
    fun getFileForUri(activity: Activity?, uri: Uri?): String? {
        if (activity == null || uri == null) {
            return ""
        }
        var photoPath: String? = ""
        val scheme = uri.scheme
        if (TextUtils.equals(ContentResolver.SCHEME_CONTENT, scheme)) {
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? =
                activity.contentResolver.query(uri, filePathColumn, null, null, null)
            cursor?.let {
                it.moveToFirst()
                val columnIndex: Int = it.getColumnIndex(filePathColumn[0])
                if (columnIndex >= 0) {
                    photoPath = it.getString(columnIndex)
                } else if (TextUtils.equals(
                        uri.authority,
                        activity.packageName + ".fileprovider"
                    )
                ) {
                    photoPath = uri.path
                }
            }
            cursor?.close()
        } else if (TextUtils.equals(ContentResolver.SCHEME_FILE, scheme)) {
            photoPath = uri.path?.replace("my_images/", "")
        }
        return if (TextUtils.isEmpty(photoPath)) "" else photoPath
    }
}