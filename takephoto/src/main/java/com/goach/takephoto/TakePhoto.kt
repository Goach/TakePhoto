package com.goach.takephoto

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.goach.takephoto.manager.TakePhotoManager
import com.goach.takephoto.manager.TakePhotoRetriever

/**
 * @author: Goach.zhong
 * @date: 2021-01-10 12:07
 * @description:
 **/
class TakePhoto {
    companion object {
        fun with(ctx: Context): TakePhotoManager {
            return getRetriever().get(ctx)
        }

        fun with(act: Activity): TakePhotoManager {
            return getRetriever().get(act)
        }

        fun with(act: FragmentActivity): TakePhotoManager {
            return getRetriever().get(act)
        }

        fun with(fragment: Fragment): TakePhotoManager {
            return getRetriever().get(fragment)
        }

        private fun getRetriever(): TakePhotoRetriever {
            return Holder.instance.getTakePhotoRetriever()
        }
    }

    object Holder {
        val instance = TakePhoto()
    }

    private val takePhotoRetriever: TakePhotoRetriever = TakePhotoRetriever()

    fun getTakePhotoRetriever(): TakePhotoRetriever {
        return takePhotoRetriever
    }

}