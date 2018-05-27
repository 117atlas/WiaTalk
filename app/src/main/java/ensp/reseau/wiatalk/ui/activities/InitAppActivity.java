package ensp.reseau.wiatalk.ui.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.app.WiaTalkApp;
import ensp.reseau.wiatalk.network.BaseResponse;
import ensp.reseau.wiatalk.network.NetworkAPI;
import ensp.reseau.wiatalk.network.UserContacts;
import ensp.reseau.wiatalk.network.UserInterface;
import ensp.reseau.wiatalk.ui.fragment.ChoosePpFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InitAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_app);
        if (checkPermission()) initApp();
    }

    public void initApp(){
        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
        UserContacts userContacts = new UserContacts();
        userContacts.setUserId(WiaTalkApp.getMe(this).get_Id());
        userContacts.setContacts(getPhoneContacts());
        Call<BaseResponse> initialize = userInterface.initializeIB(userContacts);
        initialize.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response==null){
                    Log.e("INIT APP", "Response null");
                }
                else{
                    if (response.body()==null) Log.e("INIT APP", "Response body null");
                    else{
                        Log.i("INIT APP", response.body().getMessage() + "  --  " + response.body().isError());
                        if (!response.body().isError()){
                            //NEXT ACTIVITY
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private String normalizePhoneContacts(ArrayList<String> phoneContacts){
        String s = "";
        for (String contact: phoneContacts) s = s + contact + "|";
        s = s.substring(0, s.length()-1);
        return s;
    }

    private ArrayList<String> getPhoneContacts(){
        ArrayList<String> phoneContacts= new ArrayList<>();
        ContentResolver cr = getContentResolver();
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
                                    .replace("00237", "");
                            if (phoneNo.length()==8) phoneNo = "6"+phoneNo;
                            if (!phoneContacts.contains(phoneNo)) phoneContacts.add(phoneNo);
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

    private boolean checkPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
                    // Show an alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Read external storage permission is required.");
                    builder.setTitle("Please grant permission");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    (InitAppActivity.this),
                                    new String[]{Manifest.permission.READ_CONTACTS},
                                    117
                            );
                        }
                    });
                    builder.setNeutralButton("Cancel",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    // Request permission
                    ActivityCompat.requestPermissions(
                            ((AppCompatActivity)InitAppActivity.this),
                            new String[]{Manifest.permission.READ_CONTACTS},
                            117
                    );
                }
                return false;
            }else {
                // Permission already granted
                return true;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 117: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initApp();
                } else {
                    Toast.makeText(this, getString(R.string.read_contacts_permissions_denied), Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
