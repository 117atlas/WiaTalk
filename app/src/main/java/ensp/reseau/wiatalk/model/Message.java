package ensp.reseau.wiatalk.model;

import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.app.WiaTalkApp;
import ensp.reseau.wiatalk.ui.activities.DiscussionActivity;

/**
 * Created by Sim'S on 17/05/2018.
 */

public class Message implements Serializable {

    public static final int TYPE_PENDING = 0;
    public static final int TPPE_SENT = 1;
    public static final int TYPE_RECEIVED = 2;
    public static final int TYPE_READ = 3;

    @SerializedName("_id") @Expose private String _id;
    @SerializedName("author") @Expose private String senderId;
    @SerializedName("sent_date") @Expose private long send_timestamp;
    @SerializedName("server_received_date") @Expose private long server_reception_timestamp;
    @SerializedName("group") @Expose private String groupId;
    @SerializedName("status") @Expose private int status;
    @SerializedName("received_by") @Expose private List<String> receivedIds;
    @SerializedName("read_by") @Expose private List<String> readIds;
    @SerializedName("reply") @Expose private String replyId;
    @SerializedName("content") @Expose private String text;
    @SerializedName("file") @Expose private MessageFile file;
    @SerializedName("voice_note") @Expose private String voicenote;
    @SerializedName("type") @Expose private boolean signalisationMessage;

    @Expose(serialize = false, deserialize = false) private List<User> received;
    @Expose(serialize = false, deserialize = false) private List<User> read;
    @Expose(serialize = false, deserialize = false) private String fileId;
    @Expose(serialize = false, deserialize = false) private long myReceptionTimestamp;
    @Expose(serialize = false, deserialize = false) private String voiceNotePath;
    @Expose(serialize = false, deserialize = false) private String receivedIdsList;
    @Expose(serialize = false, deserialize = false) private String readListIds;
    @Expose(serialize = false, deserialize = false) private boolean isNew = true;
    @Expose(serialize = false, deserialize = false) private User sender;
    @Expose(serialize = false, deserialize = false) private Group _group;
    @Expose(serialize = false, deserialize = false) private Message _reply;

    public void arrangeForLocalStorage(){
        fileId = file==null?null:file.get_id();

        receivedIdsList = "";
        if (receivedIds!=null) for (String s: receivedIds) receivedIdsList = receivedIdsList + s + "|";
        if (!receivedIdsList.isEmpty()) receivedIdsList = receivedIdsList.substring(0, receivedIdsList.length()-1);

        readListIds = "";
        if (readIds!=null) for (String s: readIds) readListIds = readListIds + s + "|";
        if (!readListIds.isEmpty()) readListIds = readListIds.substring(0, readListIds.length()-1);

        if (myReceptionTimestamp==0) myReceptionTimestamp = Calendar.getInstance().getTimeInMillis();
    }

    public void setFeedbacks(){
        receivedIds = U.Split(receivedIdsList, '|');
        readIds = U.Split(readListIds, '|');
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public void setReceived(List<User> received) {
        this.received = received;
    }

    public void setRead(List<User> read) {
        this.read = read;
    }

    public String getReceivedIdsList() {
        return receivedIdsList;
    }

    public void setReceivedIdsList(String receivedIdsList) {
        this.receivedIdsList = receivedIdsList;
        if (receivedIdsList!=null) receivedIds = ModelUtils.split(this.receivedIdsList, "|");
    }

    public String getReadListIds() {
        return readListIds;
    }

    public void setReadListIds(String readListIds) {
        this.readListIds = readListIds;
        if (readListIds!=null) readIds = ModelUtils.split(this.readListIds, "|");
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getMyReceptionTimestamp() {
        return myReceptionTimestamp;
    }

    public void setMyReceptionTimestamp(long myReceptionTimestamp) {
        this.myReceptionTimestamp = myReceptionTimestamp;
    }

    public String getVoiceNotePath() {
        return voiceNotePath;
    }

    public void setVoiceNotePath(String voiceNotePath) {
        this.voiceNotePath = voiceNotePath;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getReceivedIds() {
        return receivedIds;
    }

    public void setReceivedIds(List<String> receivedIds) {
        this.receivedIds = receivedIds;
        receivedIdsList = "";
        for (String s: receivedIds) receivedIdsList = receivedIdsList + s + "|";
        receivedIdsList = receivedIdsList.substring(0, receivedIdsList.length()-1);
    }

    public List<String> getReadIds() {
        return readIds;
    }

    public void setReadIds(List<String> readIds) {
        this.readIds = readIds;
        readListIds = "";
        for (String s: readIds) readListIds = readListIds + s + "|";
        readListIds = readListIds.substring(0, readListIds.length()-1);
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Message(){}

    public boolean isSignalisationMessage() {
        return signalisationMessage;
    }

    public void setSignalisationMessage(boolean signalisationMessage) {
        this.signalisationMessage = signalisationMessage;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public long getSend_timestamp() {
        return send_timestamp;
    }

    public void setSend_timestamp(long send_timestamp) {
        this.send_timestamp = send_timestamp;
    }

    public long getServer_reception_timestamp() {
        return server_reception_timestamp;
    }

    public void setServer_reception_timestamp(long server_reception_timestamp) {
        this.server_reception_timestamp = server_reception_timestamp;
    }

    public Group getGroup() {
        return _group;
    }

    public void setGroup(Group group) {
        this._group = group;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Message getReply() {
        return _reply;
    }

    public void setReply(Message reply) {
        this._reply = reply;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageFile getFile() {
        return file;
    }

    public void setFile(MessageFile file) {
        this.file = file;
    }

    public String getVoicenote() {
        return voicenote;
    }

    public void setVoicenote(String voicenote) {
        this.voicenote = voicenote;
    }

    public List<User> getReceived() {
        return received;
    }

    public List<User> getRead() {
        return read;
    }

    public void addReceived(User user){
        if (received==null) received = new ArrayList<>();
        received.add(user);
    }

    public void addRead(User user){
        if (read==null) read = new ArrayList<>();
        read.add(user);
    }



    public Message copy(){
        Message message = new Message();
        message.setSenderId(senderId);
        message.setText(text);
        message.setGroupId(groupId);
        message.setSend_timestamp(send_timestamp);
        message.setStatus(0);
        message.setReplyId(replyId);
        message.setMyReceptionTimestamp(message.getSend_timestamp());
        message.setSignalisationMessage(false);
        return message;
    }
}
