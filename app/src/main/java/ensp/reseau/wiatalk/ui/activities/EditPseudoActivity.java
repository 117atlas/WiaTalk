package ensp.reseau.wiatalk.ui.activities;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.models.Group;
import ensp.reseau.wiatalk.models.User;

public class EditPseudoActivity extends AppCompatActivity {
    private LinearLayout root;
    private ImageButton next;
    private EmojiEditText username;
    private ImageButton emoji;

    private User user;

    private EmojiPopup emojiPopup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pseudo);

        randomUser();

        //group = (Group) getIntent().getSerializableExtra(Group.class.getSimpleName());

        root = findViewById(R.id.root);

        next = findViewById(R.id.next);
        username = findViewById(R.id.username);
        emoji = findViewById(R.id.emoji);
        emojiPopup = EmojiPopup.Builder.fromRootView(root).build(username);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditPseudoActivity.this, "NEXT", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emojiPopup.isShowing()) emojiPopup.dismiss();
                else emojiPopup.toggle();
            }
        });

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                next.setEnabled(charSequence.length()>0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        bind();
    }

    private void bind(){
        username.setText(user.getPseudo());
        if (username.getText().toString().isEmpty()) next.setEnabled(false);
        else next.setEnabled(true);
    }

    private void randomUser(){
        user = new User();
        user.setId("0"); user.setMobile("697266488");
        user.setPseudo("samar"); user.setContactName("Samaritain Sims");
        user.setPp("pp2.jpg");
    }
}
