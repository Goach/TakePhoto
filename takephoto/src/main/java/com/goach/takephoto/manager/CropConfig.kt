package com.goach.takephoto.manager

import android.graphics.Bitmap
import com.goach.takephoto.callback.CropBitmapListener
import java.io.File
import java.io.Serializable

/**
 * @author: Goach.zhong
 * @date: 2021-01-16 10:30
 * @description: 裁剪配置
 **/
class CropConfig : Serializable {

    private var outCropFile: File? = null

    private var outCropFileArr: List<File?>? = null

    private var aspectX: Int? = 1

    private var aspectY: Int? = 1

    private var outputX: Int? = 500

    private var outputY: Int? = 500

    private var isScale: Boolean? = true

    private var cropBitmapListener: CropBitmapListener? = null

    private var outputFormat: Bitmap.CompressFormat? = Bitmap.CompressFormat.JPEG

    private var noFaceDetection: Boolean? = true

    /**
     * 输出裁剪图片的文件
     */
    fun setOutCropFile(outCropFile: File?): CropConfig {
        this.outCropFile = outCropFile
        return this
    }

    fun getOutCropFile(): File? {
        return outCropFile
    }

    /**
     * 选择多张图片的文件路径，多少张图片多少个File
     */
    fun setMultipleCropFile(outCropFileArr: List<File?>) : CropConfig {
        this.outCropFileArr = outCropFileArr
        return this
    }

    fun getMultipleCropFile():List<File?>?{
        return outCropFileArr
    }

    /**
     * 裁剪框宽比例
     */
    fun setAspectX(aspectX: Int?): CropConfig {
        this.aspectX = aspectX
        return this
    }


    fun getAspectX(): Int? {
        return aspectX
    }

    /**
     * 裁剪框高比例
     */
    fun setAspectY(aspectY: Int?): CropConfig {
        this.aspectY = aspectY
        return this
    }

    fun getAspectY(): Int? {
        return aspectY
    }

    /**
     * 输出的宽度
     */
    fun setOutputX(outputX: Int?): CropConfig {
        this.outputX = outputX
        return this
    }

    fun getOutputX(): Int? {
        return outputX
    }

    /**
     * 输出的高度
     */
    fun setOutputY(outputY: Int?): CropConfig {
        this.outputY = outputY
        return this
    }

    fun getOutputY(): Int? {
        return outputY
    }

    /**
     * 是否保存比例
     */
    fun setScale(scale: Boolean): CropConfig {
        this.isScale = scale
        return this
    }

    fun getScale(): Boolean? {
        return isScale
    }

    /**
     * 输出Bitmap的回调
     */
    fun setCropBitmapListener(cropBitmapListener: CropBitmapListener?): CropConfig {
        this.cropBitmapListener = cropBitmapListener
        return this
    }

    fun getCropBitmapListener(): CropBitmapListener? {
        return cropBitmapListener
    }

    /**
     * 输出图片的格式
     */
    fun setOutputFormat(outputFormat: Bitmap.CompressFormat?): CropConfig {
        this.outputFormat = outputFormat
        return this
    }

    fun getOutputFormat(): Bitmap.CompressFormat? {
        return outputFormat
    }

    /**
     * 是否需要头像识别
     */
    fun setNoFaceDetection(noFaceDetection: Boolean): CropConfig {
        this.noFaceDetection = noFaceDetection
        return this
    }

    fun getNoFaceDetection(): Boolean? {
        return noFaceDetection
    }
}