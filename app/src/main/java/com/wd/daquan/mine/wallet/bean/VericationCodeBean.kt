package com.wd.daquan.mine.wallet.bean

import android.os.Parcel
import android.os.Parcelable

class VericationCodeBean() : Parcelable {
    var verify_token: String? = ""

    constructor(parcel: Parcel) : this() {
        verify_token = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(verify_token)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VericationCodeBean> {
        override fun createFromParcel(parcel: Parcel): VericationCodeBean {
            return VericationCodeBean(parcel)
        }

        override fun newArray(size: Int): Array<VericationCodeBean?> {
            return arrayOfNulls(size)
        }
    }
}