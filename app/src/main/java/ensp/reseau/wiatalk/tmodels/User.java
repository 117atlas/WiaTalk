package ensp.reseau.wiatalk.tmodels;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

import ensp.reseau.wiatalk.ui.adapters.IPhoneContactsCharged;

/**
 * Created by Sim'S on 12/05/2018.
 */

public class User {
    private String id;
    private String mobile;
    private String pseudo;
    private String pp;
    private String contactName;
    private boolean active;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public User() {
    }

    public User(String id, String mobile, String pseudo, String pp) {
        this.id = id;
        this.mobile = mobile;
        this.pseudo = pseudo;
        this.pp = pp;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return mobile != null ? mobile.equals(user.mobile) : user.mobile == null;
    }

    @Override
    public int hashCode() {
        return mobile != null ? mobile.hashCode() : 0;
    }

    public static void usersPhoneContacts(Context context, IPhoneContactsCharged iPhoneContactsCharged){
        ArrayList<User> users= new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            int i = 1;
            while (cur != null && cur.moveToNext()) {
                User user = new User();
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                user.setId(id);
                user.setContactName(name);
                user.setPseudo("Utilisateur "+i);
                int randompp = (int)Math.round(Math.random()*10);
                user.setPp(randompp>5?null:"pp"+((randompp%5)+1)+".jpg");
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.i("Phone Contacts", "Name: " + name);
                        Log.i("Phone Contacts", "Phone Number: " + phoneNo);
                        user.setMobile(phoneNo);
                    }
                    pCur.close();
                }
                if (user.getMobile()!=null && !user.getMobile().isEmpty()) {
                    users.add(user);
                    i++;
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
        iPhoneContactsCharged.onCharged(users);
    }
}
