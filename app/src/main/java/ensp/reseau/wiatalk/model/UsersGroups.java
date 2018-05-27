package ensp.reseau.wiatalk.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sim'S on 17/05/2018.
 */

public class UsersGroups implements Serializable {
    @SerializedName("member") private User member;
    @SerializedName("entrance_date") private long entrance_date;
    @SerializedName("exit_date") private long exit_date;
    @SerializedName("is_in_group") private boolean is_in_group;

    private String groupId;
    private String memberId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public UsersGroups() {
    }

    public UsersGroups(User member, long entrance_date, long exit_date, boolean is_in_group) {
        this.member = member;
        this.entrance_date = entrance_date;
        this.exit_date = exit_date;
        this.is_in_group = is_in_group;
    }

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public long getEntrance_date() {
        return entrance_date;
    }

    public void setEntrance_date(long entrance_date) {
        this.entrance_date = entrance_date;
    }

    public long getExit_date() {
        return exit_date;
    }

    public void setExit_date(long exit_date) {
        this.exit_date = exit_date;
    }

    public boolean isIs_in_group() {
        return is_in_group;
    }

    public void setIs_in_group(boolean is_in_group) {
        this.is_in_group = is_in_group;
    }
}
