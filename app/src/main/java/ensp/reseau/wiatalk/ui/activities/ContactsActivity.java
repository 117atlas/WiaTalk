package ensp.reseau.wiatalk.ui.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.app.WiaTalkApp;
import ensp.reseau.wiatalk.localstorage.LocalStorageDiscussions;
import ensp.reseau.wiatalk.localstorage.LocalStorageUser;
import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.User;
import ensp.reseau.wiatalk.network.BaseResponse;
import ensp.reseau.wiatalk.network.NetworkAPI;
import ensp.reseau.wiatalk.network.UserContacts;
import ensp.reseau.wiatalk.network.UserInterface;
import ensp.reseau.wiatalk.ui.UiUtils;
import ensp.reseau.wiatalk.ui.adapters.ContactsAdapter;
import ensp.reseau.wiatalk.ui.adapters.IPhoneContactsCharged;
import ensp.reseau.wiatalk.ui.fragment.ContactsOptionsBottomSheetFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsActivity extends AppCompatActivity implements ContactsOptionsBottomSheetFragment.IOptionsChoosen{

    public static final int NO_PURPOSE = 0;
    public static final int PURPOSE_MESSAGE = 1;
    public static final int PURPOSE_VOCAL_CALL = 2;
    public static final int PURPOSE_VIDEO_CALL = 3;

    private Toolbar toolbar;
    private AppCompatButton inviteContacts;
    private LinearLayout noContactsContainer;
    private LinearLayout contactsListContainer;
    private RecyclerView contacts;

    private int purpose;

    private ContactsAdapter contactsAdapter;

    private ArrayList<String> phoneContacts;
    private ArrayList<String> phoneContactsName;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        getPhoneContacts();
        purpose = getIntent().getIntExtra("PURPOSE", NO_PURPOSE);

        initializeWidgets();
    }

    private void initializeWidgets(){
        toolbar = findViewById(R.id.toolbar);
        inviteContacts = findViewById(R.id.invitefriends);
        noContactsContainer = findViewById(R.id.nocontactscontainer);
        contactsListContainer = findViewById(R.id.contacts_list_container);
        contacts = findViewById(R.id.contacts_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.contacts_label));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        inviteContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        contacts.setLayoutManager(new LinearLayoutManager(this));
        contactsAdapter = new ContactsAdapter(this, ContactsAdapter.TYPE_LIST_CONTACTS_USERS, null, purpose);
        contacts.setAdapter(contactsAdapter);
        bind();
    }

    /*private void test(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true); progressDialog.setCancelable(false);
        progressDialog.setMessage("Chargement des contacts");
        progressDialog.show();
        User.usersPhoneContacts(this, new IPhoneContactsCharged() {
            @Override
            public void onCharged(ArrayList<User> users) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                contactsAdapter.setUsers(users);
            }
        });
    }*/

    @Override
    public void onOptionChoosen(int option, User user) {
        switch (option){
            case ContactsOptionsBottomSheetFragment.OPTION_MESSAGE:{
                Intent intent = new Intent(this, DiscussionActivity.class);
                intent.putExtra(Group.class.getSimpleName(), LocalStorageDiscussions.getIB(user.get_Id(), this));
                startActivity(intent);
                finish();
                //UiUtils.switchActivity(this, DiscussionActivity.class, true, null);
            } break;
            case ContactsOptionsBottomSheetFragment.OPTION_VOCAL_CALL:{
                Toast.makeText(this, "VOCAL CALL", Toast.LENGTH_SHORT).show();
            } break;
            case ContactsOptionsBottomSheetFragment.OPTION_VIDEO_CALL:{
                Toast.makeText(this, "VIDEO CALL", Toast.LENGTH_SHORT).show();
            } break;
        }
    }

    private ArrayList<String> getPhoneContacts(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading_contacts));
        progressDialog.show();

        phoneContacts= new ArrayList<>();
        phoneContactsName = new ArrayList<>();

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
                                    .replace("00237", "").replace("(", "").replace(")", "");
                            if (phoneNo.length()==8) phoneNo = "6"+phoneNo;
                            if (!phoneContacts.contains(phoneNo)) {
                                phoneContacts.add(phoneNo);
                                phoneContactsName.add(name);
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
        if(progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
        return phoneContacts;
    }

    private void bind(){
        ArrayList<User> users = LocalStorageUser.getOtherUsers(this);
        int i=0;
        if (users!=null) {
            while (i<users.size()){
                User user = users.get(i);
                if (!phoneContacts.contains(user.getMobile())) users.remove(i);
                else{
                    int ind = phoneContacts.indexOf(user.getMobile());
                    user.setContactName(phoneContactsName.get(ind));
                    i++;
                }
            }
        }
        contactsAdapter.setUsers(users);
    }

    private void updateContactsWIthUsers(){
        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
        UserContacts userContacts = new UserContacts();
        userContacts.setUserId(WiaTalkApp.getMe(this).get_Id());
        userContacts.setContacts(getPhoneContacts());
        Call<BaseResponse> initialize = userInterface.initializeIB(userContacts);
        initialize.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response==null){
                    Log.e(ContactsActivity.class.getSimpleName(), "Response null");
                }
                else{
                    if (response.body()==null) Log.e(ContactsActivity.class.getSimpleName(), "Response body null");
                    else{
                        Log.i(ContactsActivity.class.getSimpleName(), response.body().getMessage() + "  --  " + response.body().isError());
                        if (!response.body().isError()){
                            //NEXT ACTIVITY
                            Log.i(ContactsActivity.class.getSimpleName(), "Contacts updated");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contacts_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh:{
                updateContactsWIthUsers();
            } break;
        }
        return super.onOptionsItemSelected(item);
    }
}
