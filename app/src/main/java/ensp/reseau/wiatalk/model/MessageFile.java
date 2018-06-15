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
    @SerializedName("original_name") @Expose private String originalName;
    @SerializedName("size") @Expose private double size;
    @SerializedName("length") @Expose private double length;
    @SerializedName("thumbnail") @Expose private String thumbnail;

    private String localPath;
    private String localThumbnail;

    public String getLocalThumbnail() {
        return localThumbnail;
    }

    public void setLocalThumbnail(String localThumbnail) {
        this.localThumbnail = localThumbnail;
    }

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

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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
