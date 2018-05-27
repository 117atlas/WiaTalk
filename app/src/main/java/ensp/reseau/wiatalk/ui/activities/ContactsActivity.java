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
import android.widget.Toast;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.tmodels.User;
import ensp.reseau.wiatalk.ui.UiUtils;
import ensp.reseau.wiatalk.ui.adapters.ContactsAdapter;
import ensp.reseau.wiatalk.ui.adapters.IPhoneContactsCharged;
import ensp.reseau.wiatalk.ui.fragment.ContactsOptionsBottomSheetFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

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

    @Override
    public void onOptionChoosen(int option) {
        switch (option){
            case ContactsOptionsBottomSheetFragment.OPTION_MESSAGE:{
                UiUtils.switchActivity(this, DiscussionActivity.class, true, null);
            } break;
            case ContactsOptionsBottomSheetFragment.OPTION_VOCAL_CALL:{
                Toast.makeText(this, "VOCAL CALL", Toast.LENGTH_SHORT).show();
            } break;
            case ContactsOptionsBottomSheetFragment.OPTION_VIDEO_CALL:{
                Toast.makeText(this, "VIDEO CALL", Toast.LENGTH_SHORT).show();
            } break;
        }
    }
}
