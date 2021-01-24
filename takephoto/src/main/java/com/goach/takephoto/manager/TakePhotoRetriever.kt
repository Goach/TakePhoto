package com.goach.takephoto.manager

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.goach.takephoto.SupportTakePhotoFragment
import com.goach.takephoto.utils.Util

/**
 * @author: Goach.zhong
 * @date: 2021-01-10 13:57
 * @description:
 **/
class TakePhotoRetriever {
    companion object {
        const val FRAGMENT_TAG = "com.goach.takephoto.manager"
    }

    private val pendingSupportTakePhotoFragments: MutableMap<FragmentManager, SupportTakePhotoFragment> =
        mutableMapOf()

    fun get(ctx: Context?): TakePhotoManager {
        return if (ctx == null) {
            throw IllegalArgumentException("You cannot start a take photo on a null Context")
        } else if (ctx is Application) {
            throw IllegalArgumentException("You cannot start a take photo on a ApplicationContext")
        } else if (Util.isOnBackgroundThread()) {
            throw IllegalArgumentException("You cannot start a take photo on a thread")
        } else if (ctx is FragmentActivity) {
            get(ctx)
        } else if (ctx is Activity) {
            get(ctx)
        } else if (ctx is ContextWrapper && ctx.baseContext.applicationContext != null) {
            get(ctx.baseContext)
        } else {
            throw IllegalArgumentException("This context should be activity or FragmentActivity")
        }
    }

    fun get(act: FragmentActivity): TakePhotoManager {
        if (Util.isOnBackgroundThread()) {
            throw IllegalArgumentException("You cannot start a take photo on a thread")
        }
        val fm: FragmentManager = act.supportFragmentManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && act.isDestroyed) {
            throw java.lang.IllegalArgumentException("You cannot start a take photo for a destroyed activity")
        }
        return supportFragmentGet(act, fm, null, !act.isFinishing)
    }

    fun get(fragment: Fragment): TakePhotoManager {
        if (Util.isOnBackgroundThread()) {
            throw IllegalArgumentException("You cannot start a take photo on a thread")
        }
        val fm: FragmentManager = fragment.childFragmentManager
        if (fragment.context == null) {
            throw java.lang.IllegalArgumentException("You cannot start a take photo on a fragment before it is attached or after it is destroyed")
        }
        return supportFragmentGet(fragment.context!!, fm, fragment, fragment.isVisible)
    }

    private fun supportFragmentGet(
        ctx: Context,
        fm: FragmentManager,
        parentHint: Fragment?,
        isParentVisible: Boolean
    ): TakePhotoManager {
        val supportTakePhotoFragment =
            getSupportRequestManagerFragment(fm, parentHint, isParentVisible)
        var takePhotoManager = supportTakePhotoFragment.getTakePhotoManager()
        if (takePhotoManager == null) {
            takePhotoManager = TakePhotoManager(ctx, supportTakePhotoFragment)
            supportTakePhotoFragment.setTakePhotoManager(takePhotoManager)
        }
        return takePhotoManager
    }

    private fun getSupportRequestManagerFragment(
        fm: FragmentManager,
        parentHint: Fragment?,
        isParentVisible: Boolean
    ): SupportTakePhotoFragment {
        var current: SupportTakePhotoFragment? =
            fm.findFragmentByTag(FRAGMENT_TAG) as SupportTakePhotoFragment?
        if (current == null) {
            current = pendingSupportTakePhotoFragments[fm]
            if (current == null) {
                current = SupportTakePhotoFragment()
                //current.setParentFragmentHint(parentHint)
                pendingSupportTakePhotoFragments[fm] = current
                fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss()
            }
        }
        return current
    }
}