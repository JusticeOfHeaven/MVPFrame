package com.my.mvpframe.module_base.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Create by jzhan on 2019/3/19
 */
class BaseResponse<T : Parcelable>() : Parcelable {
    var code: Int = 0
    var message: String = ""
    var data: T? = null

    constructor(parcel: Parcel) : this() {
        code = parcel.readInt()
        message = parcel.readString()
        data = parcel.readParcelable(this.data!!::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(code)
        parcel.writeString(message)
        parcel.writeParcelable(data, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BaseResponse<Parcelable>> {
        override fun createFromParcel(parcel: Parcel): BaseResponse<Parcelable> {
            return BaseResponse(parcel)
        }

        override fun newArray(size: Int): Array<BaseResponse<Parcelable>?> {
            return arrayOfNulls(size)
        }
    }
}