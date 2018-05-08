package ensp.reseau.wiatalk.models;

import java.util.ArrayList;

/**
 * Created by Sim'S on 07/05/2018.
 */

public class Discussion {
    public static final int TYPE_CONTACT = 1;
    public static final int TYPE_GROUP = 2;

    public static final int STATUS_SENT = 1;
    public static final int STATUS_RECEIVED = 2;
    public static final int STATUS_READ = 3;
    public static final int STATUS_NULL = 0;

    private String pp;
    private String contact;
    private String group;
    private int type;
    private boolean mute;
    private int unreadMessages;

    private int lastMessageStatus;
    private long lastMessageDate;
    private String lastMessageString;

    public Discussion() {
    }

    public Discussion(String pp, String contact, String group, int type, boolean mute, int unreadMessages, long lastMessageDate, String lastMessageString, int lastMessageStatus) {
        this.pp = pp;
        this.contact = contact;
        this.group = group;
        this.type = type;
        this.mute = mute;
        this.unreadMessages = unreadMessages;
        this.lastMessageDate = lastMessageDate;
        this.lastMessageString = lastMessageString;
        this.lastMessageStatus = lastMessageStatus;
    }

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(int unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public long getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(long lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public String getLastMessageString() {
        return lastMessageString;
    }

    public void setLastMessageString(String lastMessageString) {
        this.lastMessageString = lastMessageString;
    }

    public int getLastMessageStatus() {
        return lastMessageStatus;
    }

    public void setLastMessageStatus(int lastMessageStatus) {
        this.lastMessageStatus = lastMessageStatus;
    }

    public static ArrayList<Discussion> random(int size){
        if (size<=0) return null;
        ArrayList<Discussion> discussions = new ArrayList<>();
        for (int i=0; i<size; i++){
            Discussion discussion = new Discussion();
            discussion.setContact("Contact Numero " + (i+1));
            int randompp = (int)Math.round(Math.random()*10);
            discussion.setPp(randompp>5?null:"pp"+((randompp%5)+1)+".jpg");
            discussion.setGroup("Discussion " + (i+1));
            discussion.setType(Math.random()>0.5?TYPE_GROUP:TYPE_CONTACT);
            discussion.setMute(Math.random()>0.5?true:false);
            double randStatus = Math.random();
            discussion.setLastMessageStatus(randStatus>0.75?STATUS_READ:(randStatus>0.5?STATUS_RECEIVED:(randStatus>0.25?STATUS_SENT:STATUS_NULL)));
            discussion.setLastMessageDate(System.currentTimeMillis());
            discussion.setUnreadMessages(discussion.getLastMessageStatus()==STATUS_NULL?(int)Math.round(Math.random()*80):0);
            discussion.setLastMessageString("Dernier message envoye dans cette discussion WIATalk");

            discussions.add(discussion);
        }
        return discussions;
    }
}
