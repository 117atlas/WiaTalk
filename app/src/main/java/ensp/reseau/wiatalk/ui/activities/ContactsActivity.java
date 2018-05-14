package ensp.reseau.wiatalk.ui.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.models.User;
import ensp.reseau.wiatalk.models.utils.Contact;
import ensp.reseau.wiatalk.ui.adapters.ContactsAdapter;
import ensp.reseau.wiatalk.ui.adapters.IPhoneContactsCharged;

public class ContactsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private AppCompatButton inviteContacts;
    private LinearLayout noContactsContainer;
    private LinearLayout contactsListContainer;
    private RecyclerView contacts;

    private ContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
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
        contactsAdapter = new ContactsAdapter(this, ContactsAdapter.TYPE_LIST_CONTACTS_USERS, null);
        contacts.setAdapter(contactsAdapter);
        test();
    }

    private void test(){
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
    }
}
