package ensp.reseau.wiatalk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sim'S on 17/05/2018.
 */

public class Message implements Serializable {

    public static final int TYPE_PENDING = 0;
    public static final int TPPE_SENT = 1;
    public static final int TYPE_RECEIVED = 2;
    public static final int TYPE_READ = 3;

    @SerializedName("_id") @Expose private String _id;
    @SerializedName("author") @Expose private User sender;
    @SerializedName("send_date") @Expose private long send_timestamp;
    @SerializedName("server_received_date") @Expose private long server_reception_timestamp;
    @SerializedName("group") @Expose private Group group;
    @SerializedName("status") @Expose private int status;
    @SerializedName("received_by") @Expose private List<String> received;
    @SerializedName("read_by") @Expose private List<String> read;
    @SerializedName("reply") @Expose private Message reply;
    @SerializedName("content") @Expose private String text;
    @SerializedName("file") @Expose private MessageFile file;
    @SerializedName("voice_note") @Expose private String voicenote;
    @SerializedName("type") @Expose private boolean signalisationMessage;

    private String senderId;
    private String groupId;
    private List<String> receivedIds;
    private List<String> readIds;
    private String replyId;
    private String fileId;
    private String voicenoteLocal;
    private long myReceptionTimestamp;
    private String voiceNotePath;
    private String receivedIdsList;
    private String readListIds;
    private boolean isNew;

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public void setReceived(List<String> received) {
        this.received = received;
    }

    public void setRead(List<String> read) {
        this.read = read;
    }

    public String getReceivedIdsList() {
        return receivedIdsList;
    }

    public void setReceivedIdsList(String receivedIdsList) {
        this.receivedIdsList = receivedIdsList;
    }

    public String getReadListIds() {
        return readListIds;
    }

    public void setReadListIds(String readListIds) {
        this.readListIds = readListIds;
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

    public String getVoicenoteLocal() {
        return voicenoteLocal;
    }

    public void setVoicenoteLocal(String voicenoteLocal) {
        this.voicenoteLocal = voicenoteLocal;
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
    }

    public List<String> getReadIds() {
        return readIds;
    }

    public void setReadIds(List<String> readIds) {
        this.readIds = readIds;
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

    public Message(String _id, User sender, long send_timestamp, long server_reception_timestamp, Group group, int status,
                   List<String> received, List<String> read, Message reply, String text, MessageFile file, String voicenote) {
        this._id = _id;
        this.sender = sender;
        this.send_timestamp = send_timestamp;
        this.server_reception_timestamp = server_reception_timestamp;
        this.group = group;
        this.status = status;
        this.received = received;
        this.read = read;
        this.reply = reply;
        this.text = text;
        this.file = file;
        this.voicenote = voicenote;
    }

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
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Message getReply() {
        return reply;
    }

    public void setReply(Message reply) {
        this.reply = reply;
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

    public List<String> getReceived() {
        return received;
    }

    public List<String> getRead() {
        return read;
    }
}
