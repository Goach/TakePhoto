package com.goach.takephoto.manager

import android.os.Parcel
import android.os.Parcelable
import com.goach.takephoto.const.Const

/**
 * @author: Goach.zhong
 * @date: 2021-01-16 10:30
 * @description: 选择多张图片配置
 **/
class MultipleConfig() : Parcelable {
    enum class Order {
        ASC,
        DESC
    }

    var maxNumber: Int? = Const.DEFAULT_MAX_NUMBER
    var photoSort: Int? = Order.DESC.ordinal

    constructor(parcel: Parcel) : this() {
        maxNumber = parcel.readValue(Int::class.java.classLoader) as? Int
        photoSort = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    /**
     * 设置可选多少张图片
     */
    fun setMaxNum(maxNumber: Int?): MultipleConfig {
        this.maxNumber = maxNumber
        return this
    }

    /**
     * 设置照片正序/倒序
     * @param order Order.Positive 正序 Order.Reverse 倒序，默认倒序
     */
    fun setOrder(order: Order): MultipleConfig {
        this.photoSort = order.ordinal
        return this
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(maxNumber)
        parcel.writeValue(photoSort)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MultipleConfig> {
        override fun createFromParcel(parcel: Parcel): MultipleConfig {
            return MultipleConfig(parcel)
        }

        override fun newArray(size: Int): Array<MultipleConfig?> {
            return arrayOfNulls(size)
        }
    }

}