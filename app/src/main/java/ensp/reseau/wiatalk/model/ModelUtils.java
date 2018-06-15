package ensp.reseau.wiatalk.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

public class ModelUtils {
    public static ArrayList<String> split(String string, String c){
        ArrayList<String> res = new ArrayList<>();
        String tmp = string.toString();
        while (string.indexOf(c)>0){
            res.add(tmp.substring(0, tmp.indexOf(c)));
            tmp = tmp.substring(tmp.indexOf(c)+1, tmp.length());
        }
        res.add(tmp);
        return res;
    }

    public static User getIbUser(User me, Group group){
        return group.getMembers().get(0).getMember().equals(me)?group.getMembers().get(1).getMember():group.getMembers().get(0).getMember();
    }

    public static class PhoneContact{
        private String name;
        private String mobile;

        public PhoneContact() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }

    public static ArrayList<PhoneContact> getPhoneContacts(Context context){
        ArrayList<PhoneContact> phoneContacts= new ArrayList<>();

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            int i = 1;
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.i("Phone Contacts", "Name: " + name);
                        Log.i("Phone Contacts", "Phone Number: " + phoneNo);
                        if (phoneNo!=null && !phoneNo.isEmpty()) {
                            phoneNo = phoneNo.replace(" ", "").replace("-", "").replace("+237", "")
                                    .replace("00237", "").replace("(", "").replace(")", "");
                            if (phoneNo.length()==8) phoneNo = "6"+phoneNo;
                            if (!phoneContacts.contains(phoneNo)) {
                                PhoneContact phoneContact = new PhoneContact();
                                phoneContact.setMobile(phoneNo);
                                phoneContact.setName(name);
                                phoneContacts.add(phoneContact);
                            }
                        }
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
        return phoneContacts;
    }
}
