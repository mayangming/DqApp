package com.wd.daquan.mine.wallet.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * @Author: 方志
 * @Time: 2019/5/15 10:08
 * @Description:银行卡信息
 */
class BankCardBean() : Parcelable {
    //验证银行卡返回字段
    var bank: String? = ""//银行名称
    var bank_code: String? = ""//银行英文字母简称
    var card_type: String? = ""//卡类型[储蓄卡或信用卡]

    //获取银行卡列表
    var cardid: String? = ""//卡序号
    var cardno: String? = ""//卡号
    var bankname: String? = ""//银行名称
    var bankcode: String? = ""//银行英文字母编号
    var create_time: String? = ""//绑定时间

    constructor(parcel: Parcel) : this() {
        bank = parcel.readString()
        bank_code = parcel.readString()
        card_type = parcel.readString()
        cardid = parcel.readString()
        cardno = parcel.readString()
        bankname = parcel.readString()
        bankcode = parcel.readString()
        create_time = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bank)
        parcel.writeString(bank_code)
        parcel.writeString(card_type)
        parcel.writeString(cardid)
        parcel.writeString(cardno)
        parcel.writeString(bankname)
        parcel.writeString(bankcode)
        parcel.writeString(create_time)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "BankCardBean(bank=$bank, bank_code=$bank_code, card_type=$card_type, cardid=$cardid, cardno=$cardno, bankname=$bankname, bankcode=$bankcode, create_time=$create_time)"
    }

    companion object CREATOR : Parcelable.Creator<BankCardBean> {
        override fun createFromParcel(parcel: Parcel): BankCardBean {
            return BankCardBean(parcel)
        }

        override fun newArray(size: Int): Array<BankCardBean?> {
            return arrayOfNulls(size)
        }
    }


}


