package ensp.reseau.wiatalk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sim'S on 17/05/2018.
 */

public class AdminsGroups {
    @SerializedName("admin") private User admin;
    @SerializedName("nomination_date") private long nomination_date;

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

    public AdminsGroups() {
    }

    public AdminsGroups(User admin, long nomination_date) {
        this.admin = admin;
        this.nomination_date = nomination_date;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public long getNomination_date() {
        return nomination_date;
    }

    public void setNomination_date(long nomination_date) {
        this.nomination_date = nomination_date;
    }
}
