package ensp.reseau.wiatalk.tmodels;

import java.util.ArrayList;

/**
 * Created by Sim'S on 08/05/2018.
 */

public class Message {
    public static final int TYPE_SENT = 1;
    public static final int TYPE_RECEIVED = -1;
    public static final int TYPE_SIGNAL = 0;

    private int type;
    private String sender;
    private boolean reply;

    public Message(int type, String sender, boolean reply) {
        this.type = type;
        this.sender = sender;
        this.reply = reply;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isReply() {
        return reply;
    }

    public void setReply(boolean reply) {
        this.reply = reply;
    }

    public Message() {
    }

    public Message(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static ArrayList<Message> random(int size){
        if (size==0) return null;
        String[] names = {"Armure", "Balai", "Craie", "Domotique", "Equitation", "Finale", "Gros", "Habilete"};
        ArrayList<Message> messages = new ArrayList<>();
        for (int i=0; i<size; i++){
            if (i==0 || i==size/3 || i==2*size/3) messages.add(new Message(TYPE_SIGNAL));
            double rmess = Math.random(), rreply = Math.random(); int randSender = (int)Math.round(Math.random()*8)%8;
            messages.add(new Message(rmess<0.23?TYPE_SENT:TYPE_RECEIVED, names[randSender], rreply<0.23?true:false));
        }
        return messages;
    }
}
