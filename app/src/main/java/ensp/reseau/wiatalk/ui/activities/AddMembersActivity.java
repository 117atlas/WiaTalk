package ensp.reseau.wiatalk.ui.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.app.WiaTalkApp;
import ensp.reseau.wiatalk.localstorage.LocalStorageDiscussions;
import ensp.reseau.wiatalk.localstorage.LocalStorageUser;
import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.User;
import ensp.reseau.wiatalk.model.UsersGroups;
import ensp.reseau.wiatalk.network.BaseResponse;
import ensp.reseau.wiatalk.network.GroupInterface;
import ensp.reseau.wiatalk.network.NetworkAPI;
import ensp.reseau.wiatalk.ui.UiUtils;
import ensp.reseau.wiatalk.ui.adapters.ContactsAdapter;
import ensp.reseau.wiatalk.ui.adapters.ContactsForAddInGroupAdapter;
import ensp.reseau.wiatalk.ui.adapters.IPhoneContactsCharged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMembersActivity extends AppCompatActivity implements IPhoneContactsCharged{

    private Toolbar toolbar;
    private TextView numberAddedMembers;
    private ImageButton next;

    private CardView addedMembersContainer;
    private RecyclerView addedMembers;
    private RecyclerView contactsList;

    private ContactsForAddInGroupAdapter forAddInGroupAdapter;
    private ContactsAdapter contactsAdapter;

    private ProgressDialog progressDialog;
    private ArrayList<String> phoneContacts;
    private ArrayList<String> phoneContactsName;

    private ArrayList<User> users;
    private ArrayList<UsersGroups> usersGroups;

    private Group group = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        group = (Group) getIntent().getSerializableExtra(Group.class.getSimpleName());

        initializeWidgets();
        getPhoneContacts();
        bind();
        System.out.println();
    }

    private void initializeWidgets(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        numberAddedMembers = findViewById(R.id.number_added);
        next = findViewById(R.id.next);
        numberAddedMembers.setText(getString(R.string.number_added).replace("???", "0"));

        next.setClickable(false);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (users!=null && users.size()>0){
                    //ADD MEMBERS
                    addMembers();
                }
            }
        });

        addedMembers = findViewById(R.id.added_members);
        addedMembersContainer = findViewById(R.id.added_members_container);
        contactsList = findViewById(R.id.contacts_list);

        addedMembersContainer.setVisibility(View.GONE);

        forAddInGroupAdapter = new ContactsForAddInGroupAdapter(this);
        contactsAdapter = new ContactsAdapter(this, ContactsAdapter.TYPE_LIST_CONTACTS_USERS_FOR_ADD_IN_GROUP, this);
        for (UsersGroups usersGroups: group.getMembers()) if (usersGroups.isIs_in_group()) contactsAdapter.addAlreadyIn(usersGroups.getMember());

        addedMembers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        contactsList.setLayoutManager(new LinearLayoutManager(this));
        addedMembers.setAdapter(forAddInGroupAdapter);
        contactsList.setAdapter(contactsAdapter);
    }

    @Override
    public void onCharged(ArrayList<User> selectedUsers) {
        if (selectedUsers.size()==0) {
            next.setClickable(false);
            addedMembersContainer.setVisibility(View.GONE);
        }
        else{
            next.setClickable(true);
            addedMembersContainer.setVisibility(View.VISIBLE);
            forAddInGroupAdapter.setUsers(selectedUsers);
        }
        this.users = selectedUsers;
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
        users = LocalStorageUser.getOtherUsers(this);
        int i=0;
        if (users!=null) {
            while (i<users.size()){
                User user = users.get(i);
                if (!phoneContacts.contains(user.getMobile())) users.remove(i);
                //if ()
                else{
                    int ind = phoneContacts.indexOf(user.getMobile());
                    user.setContactName(phoneContactsName.get(ind));
                    i++;
                }
            }
        }
        contactsAdapter.setUsers(users);
    }

    private void addMembers(){
        User me = null;
        try{
            me = WiaTalkApp.getMe(this);
        } catch (Exception e){
            e.printStackTrace();
        }
        if (me==null){
            Log.e("ADD MEMBERS", "Error getting me");
            return;
        }

        GroupInterface.AddMembersBody body = new GroupInterface.AddMembersBody();
        ArrayList<String> members = new ArrayList<>();
        members.add(me.get_Id());
        for (User user: users) members.add(user.get_Id());
        body.setMembers(members);

        GroupInterface groupInterface = NetworkAPI.getClient().create(GroupInterface.class);
        Call<GroupInterface.GetGroupResponse> addMemberCall = groupInterface.addMembersInGroup(body, group.get_id());

        final ProgressDialog progressDialog = UiUtils.loadingDialog(this);
        progressDialog.show();

        addMemberCall.enqueue(new Callback<GroupInterface.GetGroupResponse>() {
            @Override
            public void onResponse(Call<GroupInterface.GetGroupResponse> call, Response<GroupInterface.GetGroupResponse> response) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                if (response.body()==null){
                    Log.e("Add members", "Response body null");
                    return;
                }
                if (response.body().isError()){
                    Log.e("Add members", "Response body is error " + response.body().getMessage());
                    return;
                }
                Log.i("Add members", response.body().getMessage());
                Intent intent = new Intent();
                intent.putExtra(Group.class.getSimpleName(), response.body().getGroup());
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(Call<GroupInterface.GetGroupResponse> call, Throwable t) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                Log.e("Add members", t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
