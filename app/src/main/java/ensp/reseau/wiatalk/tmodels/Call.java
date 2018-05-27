package ensp.reseau.wiatalk.tmodels;

import java.util.ArrayList;

/**
 * Created by Sim'S on 07/05/2018.
 */

public class Call {
    public static final int TYPE_RECEIVED = 1;
    public static final int TYPE_MADE = 2;

    private String pp;
    private String contact;
    private int type;
    private long date;

    public Call() {
    }

    public Call(String pp, String contact, int type, long date) {
        this.pp = pp;
        this.contact = contact;
        this.type = type;
        this.date = date;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public static ArrayList<Call> random(int size){
        if (size==0) return null;
        ArrayList<Call> calls = new ArrayList<>();
        for (int i=0; i<size; i++){
            Call call = new Call();
            int randompp = (int)Math.round(Math.random()*10);
            call.setPp(randompp>5?null:"pp"+((randompp%5)+1)+".jpg");
            call.setContact("Contact " + (i+1));
            call.setType(Math.random()>0.5?TYPE_MADE:TYPE_RECEIVED);
            call.setDate(Math.round(Math.random()*new Long("100000000000").longValue()));
            calls.add(call);
        }
        return calls;
    }
}
