package ensp.reseau.wiatalk.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import ensp.reseau.wiatalk.R;

public class ChangeGroupNameActivity extends AppCompatActivity {
    public static final int CHANGE_GROUP_NAME_CODE = 233;
    private Toolbar toolbar;
    private EmojiEditText groupName;
    private AppCompatButton close;
    private AppCompatButton next;
    private ImageView emoji;
    private LinearLayout root;

    private EmojiPopup emojiPopup;

    private String _groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_group_name);

        _groupName = getIntent().getStringExtra(ChangeGroupNameActivity.class.getSimpleName());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.change_group_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        groupName = findViewById(R.id.group_name);
        groupName.setText(_groupName);
        close = findViewById(R.id.close);
        next = findViewById(R.id.next);
        emoji = findViewById(R.id.emoji);
        root = findViewById(R.id.root);
        emojiPopup = EmojiPopup.Builder.fromRootView(root).build(groupName);
        next.setClickable(groupName.getText().toString().length()>0);

        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emojiPopup.isShowing()) {
                    emojiPopup.dismiss();
                    emoji.setImageResource(R.drawable.ic_insert_emoticon);
                }
                else {
                    emojiPopup.toggle();
                    emoji.setImageResource(R.drawable.ic_keyboard);
                }
            }
        });

        groupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>0) next.setClickable(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(ChangeGroupNameActivity.class.getSimpleName(), groupName.getText().toString());
                setResult(RESULT_OK, intent);
                onBackPressed();
                finish();
            }
        });
    }
}
