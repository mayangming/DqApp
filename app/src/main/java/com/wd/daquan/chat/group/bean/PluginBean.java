package com.wd.daquan.chat.group.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Author: 方志
 * @Time: 2019/1/7 11:28
 * @Description:
 */
public class PluginBean implements Parcelable {
    /**
     * plugin_id : PG42nsndfdt43
     * plugin_name : 小助手
     * logo :
     * description : 我是简介
     * plugin_type : 1
     * update_time : 1546594812
     * company_name : 深圳市天天爱科技有限公司
     */

    public String plugin_id;
    public String plugin_name;
    public String logo;
    public String description;
    public String plugin_type;
    public long update_time;
    public String company_name;

    public String getPlugin_id() {
        return plugin_id;
    }

    public String getPlugin_name() {
        return plugin_name;
    }


    public String getLogo() {
        return logo;
    }

    public String getDescription() {
        return description;
    }

    public String getPlugin_type() {
        return plugin_type == null ? "" : plugin_type;
    }

    public String getCompany_name() {
        return company_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.plugin_id);
        dest.writeString(this.plugin_name);
        dest.writeString(this.logo);
        dest.writeString(this.description);
        dest.writeString(this.plugin_type);
        dest.writeLong(this.update_time);
        dest.writeString(this.company_name);
    }

    public PluginBean() {
    }

    protected PluginBean(Parcel in) {
        this.plugin_id = in.readString();
        this.plugin_name = in.readString();
        this.logo = in.readString();
        this.description = in.readString();
        this.plugin_type = in.readString();
        this.update_time = in.readLong();
        this.company_name = in.readString();
    }

    public static final Creator<PluginBean> CREATOR = new Creator<PluginBean>() {
        @Override
        public PluginBean createFromParcel(Parcel source) {
            return new PluginBean(source);
        }

        @Override
        public PluginBean[] newArray(int size) {
            return new PluginBean[size];
        }
    };

    @Override
    public String toString() {
        return "PluginBean{" +
                "plugin_id='" + plugin_id + '\'' +
                ", plugin_name='" + plugin_name + '\'' +
                ", logo='" + logo + '\'' +
                ", description='" + description + '\'' +
                ", plugin_type=" + plugin_type +
                ", update_time=" + update_time +
                ", company_name='" + company_name + '\'' +
                '}';
    }
}
