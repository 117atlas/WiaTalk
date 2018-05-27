package ensp.reseau.wiatalk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ensp.reseau.wiatalk.model.Message;

/**
 * Created by Sim'S on 17/05/2018.
 */

public class Group implements Serializable{

    public static final int TYPE_IB = 1;
    public static final int TYPE_GROUP = 2;

    @SerializedName("_id") @Expose private String _id;
    @SerializedName("name") @Expose private String name;
    @SerializedName("type") @Expose private int type;
    @SerializedName("picture") @Expose private String pp;
    @SerializedName("picture_change_timestamp") @Expose private long pp_change_timestamp;
    @SerializedName("messages") @Expose private List<Message> messages;
    @SerializedName("members") @Expose private List<UsersGroups> members;
    @SerializedName("admins") @Expose private List<AdminsGroups> admins;
    @SerializedName("creator") @Expose private User creator;
    @SerializedName("creation_date") @Expose private long creation_date;

    private List<String> messagesIds;
    private List<String> membersIds;
    private List<String> adminsIds;
    private String creatorId;
    private int newMessages;
    private String ppPath;

    public String getPpPath() {
        return ppPath;
    }

    public void setPpPath(String ppPath) {
        this.ppPath = ppPath;
    }

    public int getNewMessages() {
        return newMessages;
    }

    public void setNewMessages(int newMessages) {
        this.newMessages = newMessages;
    }

    private Message lastMessage;

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<String> getMessagesIds() {
        return messagesIds;
    }

    public void setMessagesIds(List<String> messagesIds) {
        this.messagesIds = messagesIds;
    }

    public List<String> getMembersIds() {
        return membersIds;
    }

    public void setMembersIds(List<String> membersIds) {
        this.membersIds = membersIds;
    }

    public List<String> getAdminsIds() {
        return adminsIds;
    }

    public void setAdminsIds(List<String> adminsIds) {
        this.adminsIds = adminsIds;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Group() {
    }

    public Group(String _id, String name, int type, String pp, List<Message> messages, List<UsersGroups> members, List<AdminsGroups> admins, User creator, long creation_date) {
        this._id = _id;
        this.name = name;
        this.type = type;
        this.pp = pp;
        this.messages = messages;
        this.members = members;
        this.admins = admins;
        this.creator = creator;
        this.creation_date = creation_date;
    }

    public long getPp_change_timestamp() {
        return pp_change_timestamp;
    }

    public void setPp_change_timestamp(long pp_change_timestamp) {
        this.pp_change_timestamp = pp_change_timestamp;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<UsersGroups> getMembers() {
        return members;
    }

    public void setMembers(List<UsersGroups> members) {
        this.members = members;
    }

    public List<AdminsGroups> getAdmins() {
        return admins;
    }

    public void setAdmins(List<AdminsGroups> admins) {
        this.admins = admins;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public long getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(long creation_date) {
        this.creation_date = creation_date;
    }
}
