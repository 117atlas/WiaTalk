package ensp.reseau.wiatalk.ui.activities;

import android.graphics.Color;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vanniktech.emoji.EmojiTextView;

import java.io.File;
import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.localstorage.LocalStorageDiscussions;
import ensp.reseau.wiatalk.localstorage.LocalStorageUser;
import ensp.reseau.wiatalk.model.Group;
import ensp.reseau.wiatalk.model.ModelUtils;
import ensp.reseau.wiatalk.model.User;
import ensp.reseau.wiatalk.network.NetworkUtils;
import ensp.reseau.wiatalk.ui.UiUtils;
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

        user = (User) getIntent().getSerializableExtra(User.class.getSimpleName());

        //testUser();
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
        String userNameText = user.getMobile() + " ~" + user.getPseudo();
        Spannable spannable = new SpannableString(userNameText);
        spannable.setSpan(new ForegroundColorSpan(Color.GRAY), userNameText.lastIndexOf("~"), userNameText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        userName.setText(spannable);

        userMobile.setText(user.getMobile());

        getSupportActionBar().setTitle(user.getContactName()==null?user.getMobile():user.getContactName());

        if (user.getPp()!=null){
            if (user.getPpPath()==null || !new File(user.getPpPath()).exists()) {
                downloadPp(user);
            }
            else UiUtils.showImage(this, pp, user.getPpPath());
        }

        groupSmallAdapter.setGroups(LocalStorageDiscussions.getCommonGroups(user.get_Id(), this));

        new AsyncTask<Void, Void, ModelUtils.PhoneContact>(){
            @Override
            protected ModelUtils.PhoneContact doInBackground(Void... voids) {
                ArrayList<ModelUtils.PhoneContact> phoneContacts = ModelUtils.getPhoneContacts(ContactInfosActivity.this);
                for (ModelUtils.PhoneContact phoneContact: phoneContacts) if (phoneContact.getMobile().equals(user.getMobile())) return phoneContact;
                return null;
            }
            @Override
            protected void onPostExecute(ModelUtils.PhoneContact phoneContact) {
                super.onPostExecute(phoneContact);
                if (phoneContact!=null) {
                    user.setContactName(phoneContact.getName());
                    String userNameText = user.getContactName() + " ~" + user.getPseudo();
                    Spannable spannable = new SpannableString(userNameText);
                    spannable.setSpan(new ForegroundColorSpan(Color.GRAY), userNameText.lastIndexOf("~"), userNameText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    userName.setText(spannable);
                }
            }
        }.execute();
    }

    private void downloadPp(final User user){
        NetworkUtils.downloadPp(this, user.get_id(), user.getPp(), new NetworkUtils.IFileDownload() {
            @Override
            public void onFileDownloaded(boolean error, String path) {
                if (!error) {
                    user.setPpPath(path);
                    user.setOld_pp_change_timestamp(user.getPp_change_timestamp());
                    LocalStorageUser.storeUser(user, ContactInfosActivity.this, false);
                    UiUtils.showImage(ContactInfosActivity.this, pp, path);
                    Log.d("DL PP GRP", "Contacts Infos Activity - Complete");
                }
                else{
                    UiUtils.showImage(ContactInfosActivity.this, pp, user.getPp(), true);
                    Log.e("DL PP GRP", "Contacts Infos Activity - Error");
                }
            }
        });
    }

}
