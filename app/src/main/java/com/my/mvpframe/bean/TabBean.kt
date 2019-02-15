package com.my.mvpframe.bean

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import com.my.mvpframe.widget.CommonTabLayout

/**
 * Create by jzhan on 2019/1/31
 */
data class TabBean(
        var name: String,
        @DrawableRes
        var sImage: Int,
        @DrawableRes
        var unsImage: Int
):CommonTabLayout.CustomTabEntity {
        override val tabTitle: String
                get() = name
        override val tabSelectedIcon: Int
                get() = sImage
        override val tabUnselectedIcon: Int
                get() = unsImage
}