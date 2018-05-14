package ensp.reseau.wiatalk.ui.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vanniktech.emoji.EmojiTextView;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.models.Group;
import ensp.reseau.wiatalk.models.User;
import ensp.reseau.wiatalk.ui.adapters.GroupSmallAdapter;

public class ContactInfosActivity extends AppCompatActivity {

    private ImageView pp;
    private Toolbar toolbar;
    private EmojiTextView userName;
    private TextView userMobile;
    private ImageButton message;
    private ImageButton vocalCall;
    private ImageButton videoCall;
    private AppCompatButton sharedFiles;
    private CardView commonsGroupContainer;
    private RecyclerView commonsGroup;

    private GroupSmallAdapter groupSmallAdapter;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_infos);
        //user = (User) getIntent().getSerializableExtra(User.class.getSimpleName());
        testUser();
        initializeWidgets();
        bind();
    }

    private void initializeWidgets(){
        pp = findViewById(R.id.pp);
        toolbar = findViewById(R.id.toolbar);
        userName = findViewById(R.id.username);
        userMobile = findViewById(R.id.usermobile);
        message = findViewById(R.id.message);
        vocalCall = findViewById(R.id.vocal_call);
        videoCall = findViewById(R.id.video_call);
        sharedFiles = findViewById(R.id.shared_files);
        commonsGroupContainer = findViewById(R.id.common_group_container);
        commonsGroup = findViewById(R.id.common_group_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        groupSmallAdapter = new GroupSmallAdapter(this);
        commonsGroup.setLayoutManager(new LinearLayoutManager(this));
        commonsGroup.setAdapter(groupSmallAdapter);

        commonsGroupContainer.setVisibility(View.VISIBLE);

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ContactInfosActivity.this, "MESSAGE", Toast.LENGTH_SHORT).show();
            }
        });
        vocalCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ContactInfosActivity.this, "VOCAL CALL", Toast.LENGTH_SHORT).show();
            }
        });
        videoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ContactInfosActivity.this, "VIDEO CALL", Toast.LENGTH_SHORT).show();
            }
        });
        sharedFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ContactInfosActivity.this, "SHARED FILES", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void testUser(){
        user = new User();
        user.setId("0"); user.setMobile("697266488");
        user.setPseudo("samar"); user.setContactName("Samaritain Sims");
        user.setPp("pp2.jpg");
    }

    private void bind(){
        String userNameText = user.getContactName() + " ~" + user.getPseudo();
        Spannable spannable = new SpannableString(userNameText);
        spannable.setSpan(new ForegroundColorSpan(Color.GRAY), userNameText.lastIndexOf("~"), userNameText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        userName.setText(spannable);

        userMobile.setText(user.getMobile());

        getSupportActionBar().setTitle(user.getContactName()==null?user.getMobile():user.getContactName());

        U.loadImage(this, pp, user.getPp());

        groupSmallAdapter.setGroups(Group.randomGroups(6));
    }
}
