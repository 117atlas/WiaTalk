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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.models.User;
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

        phoneContacts();
    }

    public void phoneContacts(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true); progressDialog.setCancelable(false);
        progressDialog.setMessage("Chargement des contacts");
        progressDialog.show();
        User.usersPhoneContacts(getContext(), new IPhoneContactsCharged() {
            @Override
            public void onCharged(ArrayList<User> users) {
                if (progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
                contactsAdapter.setUsers(users);
            }
        });
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
