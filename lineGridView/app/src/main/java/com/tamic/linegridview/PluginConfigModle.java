package com.tamic.linegridview;

import android.os.Parcel;
import android.os.Parcelable;

import com.tamic.linegridview.base.BaseBean;

import java.io.Serializable;

/**
 * 插件配置模型
 * Created by LIUYONGKUI726 on 2016-01-28.
 */
public class PluginConfigModle extends BaseBean{


    /*插件ID*/
    private String ID;
    /*插件类型*/
    private int type;
    /*插件名*/
    private String pluginName;
    /*描述*/
    private String description;
    /*包名*/
    private String packageName;
    /*版本号*/
    private int version;
    /*主界面*/
    private String mainActivity;
    /*是否为独立插件*/
    private String isEnabled;
    /*插件图标*/
    private String appIcon;
    /*插件下载地址*/
    private String downUrl;


    /**
     * Construrtor
     */
    public PluginConfigModle() {
    }


    /**
     * @param ID
     * @param type
     * @param pluginName
     * @param packageName
     * @param description
     * @param version
     * @param mainActivity
     * @param isEnabled
     * @param appIcon
     * @param downUrl
     */
    public PluginConfigModle(String ID, int type, String pluginName,
                             String packageName, String description, int version,
                             String mainActivity, String isEnabled, String appIcon,
                             String downUrl) {
        this.ID = ID;
        this.type = type;
        this.pluginName = pluginName;
        this.packageName = packageName;
        this.description = description;
        this.version = version;
        this.mainActivity = mainActivity;
        this.isEnabled = isEnabled;
        this.appIcon = appIcon;
        this.downUrl = downUrl;
    }



    protected PluginConfigModle(Parcel in) {
        ID = in.readString();
        type = in.readInt();
        pluginName = in.readString();
        description = in.readString();
        packageName = in.readString();
        version = in.readInt();
        mainActivity = in.readString();
        isEnabled = in.readString();
        appIcon = in.readString();
        downUrl = in.readString();
    }

   /* public static final Creator<PluginConfigModle> CREATOR = new Creator<PluginConfigModle>() {
        @Override
        public PluginConfigModle createFromParcel(Parcel in) {
            return new PluginConfigModle(in);
        }

        @Override
        public PluginConfigModle[] newArray(int size) {
            return new PluginConfigModle[size];
        }
    };*/

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(String mainActivity) {
        this.mainActivity = mainActivity;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

   /* @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeInt(type);
        dest.writeString(pluginName);
        dest.writeString(description);
        dest.writeString(packageName);
        dest.writeInt(version);
        dest.writeString(mainActivity);
        dest.writeString(isEnabled);
        dest.writeString(appIcon);
        dest.writeString(downUrl);
    }*/

    @Override
    public String toString() {
        return "PluginConfigModle{" +
                "ID='" + ID + '\'' +
                ", type=" + type +
                ", pluginName='" + pluginName + '\'' +
                ", description='" + description + '\'' +
                ", packageName='" + packageName + '\'' +
                ", version=" + version +
                ", mainActivity='" + mainActivity + '\'' +
                ", isEnabled='" + isEnabled + '\'' +
                ", appIcon='" + appIcon + '\'' +
                ", downUrl='" + downUrl + '\'' +
                '}';
    }
}
