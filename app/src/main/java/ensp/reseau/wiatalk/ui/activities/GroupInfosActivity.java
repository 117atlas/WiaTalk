package ensp.reseau.wiatalk.ui.activities;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.tmodels.Group;
import ensp.reseau.wiatalk.tmodels.User;
import ensp.reseau.wiatalk.ui.adapters.ContactsAdapter;
import ensp.reseau.wiatalk.ui.adapters.IPhoneContactsCharged;

public class GroupInfosActivity extends AppCompatActivity {

    private ImageView pp;
    private Toolbar toolbar;
    private EmojiTextView groupName;
    private ImageButton editGroupName;
    private TextView groupCreationInfos;
    private AppCompatButton sharedFiles;
    private RecyclerView groupMembers;
    private AppCompatButton quitGroup;

    private ContactsAdapter contactsAdapter;

    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_infos);
        //group = (Group) getIntent().getSerializableExtra(Group.class.getSimpleName());
        testGroup();
        initializeWidgets();
        bind();
    }

    private void initializeWidgets(){
        pp = findViewById(R.id.pp);
        toolbar = findViewById(R.id.toolbar);
        groupName = findViewById(R.id.group_name);
        editGroupName = findViewById(R.id.edit_group_name);
        groupCreationInfos = findViewById(R.id.group_creation_date);
        sharedFiles = findViewById(R.id.group_files);
        quitGroup = findViewById(R.id.quit);
        groupMembers = findViewById(R.id.group_members);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        quitGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GroupInfosActivity.this, "QUIT GROUP", Toast.LENGTH_SHORT).show();
            }
        });
        sharedFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GroupInfosActivity.this, "SHARED FILES", Toast.LENGTH_SHORT).show();
            }
        });
        editGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GroupInfosActivity.this, "EDIT GROUP NAME", Toast.LENGTH_SHORT).show();
            }
        });

        contactsAdapter = new ContactsAdapter(this, ContactsAdapter.TYPE_LIST_CONTACTS_USERS_IN_GROUP, null);
        groupMembers.setLayoutManager(new LinearLayoutManager(this));
        groupMembers.setAdapter(contactsAdapter);
    }

    private void testGroup(){
        group = new Group();
        group.setId("0");
        group.setType(Group.TYPE_GROUP);
        group.setPp("pp4.jpg");
        group.setNom("Bandolero");
        group.setCreatorId("0");
        group.setCreationDate(SystemClock.currentThreadTimeMillis());
    }

    private void bind(){
        U.showImageAsset(this, group.getPp(), pp);
        getSupportActionBar().setTitle(group.getNom());
        groupName.setText(group.getNom());

        User.usersPhoneContacts(this, new IPhoneContactsCharged() {
            @Override
            public void onCharged(ArrayList<User> users) {
                contactsAdapter.setUsers(users);
            }
        });
    }
}
