package ensp.reseau.wiatalk.ui.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.models.Message;
import ensp.reseau.wiatalk.ui.adapters.IMessageClickHandler;
import ensp.reseau.wiatalk.ui.adapters.MessagesAdapter;

public class DiscussionActivity extends AppCompatActivity implements IMessageClickHandler{

    private LinearLayout toolbarContent;
    private CircleImageView pp;
    private TextView discName;
    private TextView discInfos;
    private Toolbar toolbar;

    private NestedScrollView scroll;
    private RecyclerView messages;
    private LinearLayoutManager messagesllManager;
    private TextView dateIndicator;
    private FloatingActionButton goDown;

    private RelativeLayout replyMessageContainer;
    private ImageView replyMessageLink;
    private TextView replyMessageSender;
    private TextView replyMessageMessage;
    private ImageView closereplyMessage;

    private ImageView emoji;
    private EditText text;
    private ImageView send;
    private LinearLayout fileContainer;
    private ImageView voicenote;
    private ImageView file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
        initializeWidgets();
        hideToolbarElements();
        scrollingManager();
        replyMessageManager();
        sendMessageManager();

        test();
    }

    private void initializeWidgets(){
        toolbarContent = findViewById(R.id.toolbar_content);
        pp = findViewById(R.id.pp);
        discName = findViewById(R.id.discussion_name);
        discInfos = findViewById(R.id.discussion_infos);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        scroll = findViewById(R.id.scroll);
        messages = findViewById(R.id.messages_list);
        messagesllManager = new LinearLayoutManager(this);
        dateIndicator = findViewById(R.id.date_indicator);
        goDown = findViewById(R.id.godown);

        replyMessageContainer = findViewById(R.id.reply_message_container);
        replyMessageLink = findViewById(R.id.reply_message_link);
        replyMessageSender = findViewById(R.id.reply_message_sender);
        replyMessageMessage = findViewById(R.id.reply_message_message);
        closereplyMessage = findViewById(R.id.close_reply_message);

        emoji = findViewById(R.id.emoji);
        text = findViewById(R.id.text);
        send = findViewById(R.id.next);
        fileContainer = findViewById(R.id.file_container);
        voicenote = findViewById(R.id.mic);
        file = findViewById(R.id.file);
    }

    private void hideToolbarElements(){
        toolbarContent.setVisibility(View.GONE);
    }

    private void scrollingManager(){
        scroll.setNestedScrollingEnabled(false);
        messages.setNestedScrollingEnabled(false);
        messages.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) dateIndicator.setVisibility(View.GONE);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (messagesllManager.findLastCompletelyVisibleItemPosition()==1) goDown.setVisibility(View.GONE);
                else goDown.setVisibility(View.VISIBLE);
                if ((dy>0 || dy<0) && dateIndicator.getVisibility()==View.GONE) dateIndicator.setVisibility(View.VISIBLE);
            }
        });
    }

    private void replyMessageManager(){
        replyMessageLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        closereplyMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replyMessageContainer.setVisibility(View.GONE);
            }
        });
    }

    private void sendMessageManager(){
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (text.getText().length()>0) fileContainer.setVisibility(View.GONE);
                else fileContainer.setVisibility(View.VISIBLE);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        voicenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void test(){
        messages.setLayoutManager(messagesllManager);
        MessagesAdapter adapter = new MessagesAdapter(this);
        messages.setAdapter(adapter);
        adapter.setMessages(Message.random(57));
    }

    @Override
    public void click(int position) {
        Toast.makeText(this, "CLICK " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void longClick(int position) {
        Toast.makeText(this, "LONG CLICK " + position, Toast.LENGTH_SHORT).show();
    }
}
