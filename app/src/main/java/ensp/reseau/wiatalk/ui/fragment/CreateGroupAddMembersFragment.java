package ensp.reseau.wiatalk.ui.fragment;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.localstorage.LocalStorageUser;
import ensp.reseau.wiatalk.model.User;
import ensp.reseau.wiatalk.ui.activities.CreateGroupActivity;
import ensp.reseau.wiatalk.ui.adapters.ContactsAdapter;
import ensp.reseau.wiatalk.ui.adapters.ContactsForAddInGroupAdapter;
import ensp.reseau.wiatalk.ui.adapters.IPhoneContactsCharged;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateGroupAddMembersFragment extends Fragment implements IPhoneContactsCharged {

    private CardView addedMembersContainer;
    private RecyclerView addedMembers;
    private RecyclerView contactsList;

    private ContactsForAddInGroupAdapter forAddInGroupAdapter;
    private ContactsAdapter contactsAdapter;

    private ProgressDialog progressDialog;
    private ArrayList<String> phoneContacts;
    private ArrayList<String> phoneContactsName;

    public CreateGroupAddMembersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_group_add_members, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addedMembers = view.findViewById(R.id.added_members);
        addedMembersContainer = view.findViewById(R.id.added_members_container);
        contactsList = view.findViewById(R.id.contacts_list);

        addedMembersContainer.setVisibility(View.GONE);

        forAddInGroupAdapter = new ContactsForAddInGroupAdapter(getContext());
        contactsAdapter = new ContactsAdapter(getContext(), ContactsAdapter.TYPE_LIST_CONTACTS_USERS_FOR_ADD_IN_GROUP, this);

        addedMembers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        contactsList.setLayoutManager(new LinearLayoutManager(getContext()));
        addedMembers.setAdapter(forAddInGroupAdapter);
        contactsList.setAdapter(contactsAdapter);

        //phoneContacts();
        getPhoneContacts();
        bind();
    }

    /*public void phoneContacts(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true); progressDialog.setCancelable(false);
        progressDialog.setMessage("Chargement des contacts");
        progressDialog.show();
        /*User.usersPhoneContacts(getContext(), new IPhoneContactsCharged() {
            @Override
            public void onCharged(ArrayList<User> users) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                contactsAdapter.setUsers(users);
            }
        });*/
    //}*/

    private ArrayList<String> getPhoneContacts(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading_contacts));
        progressDialog.show();

        phoneContacts= new ArrayList<>();
        phoneContactsName = new ArrayList<>();

        ContentResolver cr = getContext().getContentResolver();
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
        ArrayList<User> users = LocalStorageUser.getOtherUsers(getContext());
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


    @Override
    public void onCharged(ArrayList<User> selectedUsers) {
        if (selectedUsers.size()==0) addedMembersContainer.setVisibility(View.GONE);
        else{
            addedMembersContainer.setVisibility(View.VISIBLE);
            forAddInGroupAdapter.setUsers(selectedUsers);
        }
        ((CreateGroupActivity)getActivity()).setSelectedUsers(selectedUsers);
    }
}
