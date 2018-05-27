package ensp.reseau.wiatalk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sim'S on 17/05/2018.
 */

public class MessageFile implements Serializable {

    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_VIDEO = 2;
    public static final int TYPE_AUDIO = 3;
    public static final int TYPE_DOCUMENT = 4;

    @SerializedName("_id") @Expose private String _id;
    @SerializedName("type") @Expose private int type;
    @SerializedName("url") @Expose private String url;

    private String localPath;

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public MessageFile() {
    }

    public MessageFile(String _id, int type, String url) {
        this._id = _id;
        this.type = type;
        this.url = url;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
