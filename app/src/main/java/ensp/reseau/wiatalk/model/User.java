package ensp.reseau.wiatalk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;
import java.util.List;

/**
 * Created by Sim'S on 17/05/2018.
 */

public class User implements Serializable {

    @SerializedName("_id") @Expose private String _id;
    @SerializedName("phone") @Expose private String mobile;
    @SerializedName("pseudo") @Expose private String pseudo;
    @SerializedName("picture") @Expose private String pp;
    @SerializedName("picture_change_timestamp") @Expose private long pp_change_timestamp;
    @SerializedName("groups") @Expose private List<Group> groups;
    @SerializedName("active") @Expose private boolean active;

    private List<String> groupsIds;
    private String ppPath;

    public String getPpPath() {
        return ppPath;
    }

    public void setPpPath(String ppPath) {
        this.ppPath = ppPath;
    }

    public User() {
    }

    public User(String _id, String mobile, String pseudo, String pp) {
        this._id = _id;
        this.mobile = mobile;
        this.pseudo = pseudo;
        this.pp = pp;
    }

    public long getPp_change_timestamp() {
        return pp_change_timestamp;
    }

    public void setPp_change_timestamp(long pp_change_timestamp) {
        this.pp_change_timestamp = pp_change_timestamp;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String get_Id() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public List<String> getGroupsIds() {
        return groupsIds;
    }

    public void setGroupsIds(List<String> groupsIds) {
        this.groupsIds = groupsIds;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", mobile='" + mobile + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", pp='" + pp + '\'' +
                ", groups=" + groups +
                ", active=" + active +
                ", groupsIds=" + groupsIds +
                '}';
    }
}
