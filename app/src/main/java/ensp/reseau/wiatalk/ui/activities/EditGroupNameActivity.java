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
import com.vanniktech.emoji.EmojiTextView;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.models.Group;

public class EditGroupNameActivity extends AppCompatActivity {
    private LinearLayout root;
    private ImageButton next;
    private EmojiEditText groupName;
    private ImageButton emoji;

    private Group group;

    private EmojiPopup emojiPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group_name);

        testGroup();

        //group = (Group) getIntent().getSerializableExtra(Group.class.getSimpleName());

        root = findViewById(R.id.root);

        next = findViewById(R.id.next);
        groupName = findViewById(R.id.group_name);
        emoji = findViewById(R.id.emoji);
        emojiPopup = EmojiPopup.Builder.fromRootView(root).build(groupName);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditGroupNameActivity.this, "NEXT", Toast.LENGTH_SHORT).show();
            }
        });
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emojiPopup.isShowing()) emojiPopup.dismiss();
                else emojiPopup.toggle();
            }
        });

        groupName.addTextChangedListener(new TextWatcher() {
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
        groupName.setText(group.getNom());
        if (groupName.getText().toString().isEmpty()) next.setEnabled(false);
        else next.setEnabled(true);
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

}
