package ensp.reseau.wiatalk.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.piasy.rxandroidaudio.AudioRecorder;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.ios.IosEmojiProvider;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.files.FilesUtils;
import ensp.reseau.wiatalk.models.Message;
import ensp.reseau.wiatalk.ui.UiUtils;
import ensp.reseau.wiatalk.ui.adapters.ICameraHandler;
import ensp.reseau.wiatalk.ui.adapters.IMessageClickHandler;
import ensp.reseau.wiatalk.ui.adapters.MessagesAdapter;
import ensp.reseau.wiatalk.ui.fragment.ChoosePpFragment;
import ensp.reseau.wiatalk.ui.fragment.FileChooserBottomFragment;

import static android.content.ContentValues.TAG;

public class DiscussionActivity extends AppCompatActivity implements IMessageClickHandler, ICameraHandler{

    private CoordinatorLayout root;

    private LinearLayout toolbarContent;
    private CircleImageView pp;
    private TextView discName;
    private TextView discInfos;
    private Toolbar toolbar;

    //private NestedScrollView scroll;
    private RecyclerView messages;
    private LinearLayoutManager messagesllManager;
    private TextView dateIndicator;
    private FloatingActionButton goDown;

    private RelativeLayout replyMessageContainer;
    private ImageView replyMessageLink;
    private TextView replyMessageSender;
    private TextView replyMessageMessage;
    private ImageView closereplyMessage;

    private RelativeLayout textmessageContainer;
    private ImageView emoji;
    private EmojiEditText text;
    private ImageView send;
    private LinearLayout fileContainer;
    private ImageView voicenote;
    private ImageView file;
    private EmojiPopup emojiPopup;

    private LinearLayout recordContainer;
    private ImageView closeRecordContainer;
    private TextView recordedTime;
    private ImageView sendRecordedAudio;
    private AudioRecorder mAudioRecorder;
    private File mAudioFile;
    private boolean isRecording = false;


    private boolean longClick = false;
    private Menu activityMenu;
    private ArrayList<Integer> longClickSelectedItems = new ArrayList<>();

    private Uri imageFileUri;
    private Uri videoFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EmojiManager.install(new IosEmojiProvider());

        setContentView(R.layout.activity_discussion);

        initializeWidgets();
        scrollingManager();
        replyMessageManager();
        sendMessageManager();
        initRecordContainer();

        test();
    }

    private void initializeWidgets(){
        root = findViewById(R.id.root);

        toolbarContent = findViewById(R.id.toolbar_content);
        pp = findViewById(R.id.pp);
        discName = findViewById(R.id.discussion_name);
        discInfos = findViewById(R.id.discussion_infos);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.requestFocus();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbarContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtils.switchActivity(DiscussionActivity.this, GroupInfosActivity.class, false, null);
            }
        });

        //scroll = findViewById(R.id.scroll);
        messages = findViewById(R.id.messages_list);
        messagesllManager = new LinearLayoutManager(this);
        dateIndicator = findViewById(R.id.date_indicator);
        goDown = findViewById(R.id.godown);

        replyMessageContainer = findViewById(R.id.reply_message_container);
        replyMessageLink = findViewById(R.id.reply_message_link);
        replyMessageSender = findViewById(R.id.reply_message_sender);
        replyMessageMessage = findViewById(R.id.reply_message_message);
        closereplyMessage = findViewById(R.id.close_reply_message);

        textmessageContainer = findViewById(R.id.textmessage_container);
        emoji = findViewById(R.id.emoji);
        text = findViewById(R.id.text);
        send = findViewById(R.id.send);
        fileContainer = findViewById(R.id.file_container);
        voicenote = findViewById(R.id.mic);
        file = findViewById(R.id.file);
        emojiPopup = EmojiPopup.Builder.fromRootView(root).build(text);

        recordContainer = findViewById(R.id.record_container);
        closeRecordContainer = findViewById(R.id.close_record);
        recordedTime = findViewById(R.id.record_duration);
        sendRecordedAudio = findViewById(R.id.send_record);
    }

    private void hideToolbarElements(){
        toolbarContent.setVisibility(View.GONE);
    }

    private void scrollingManager(){
        //scroll.setNestedScrollingEnabled(false);
        messages.setNestedScrollingEnabled(false);
        messages.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) dateIndicator.setVisibility(View.GONE);
                Log.d("STATE", "" + newState + "  " + RecyclerView.SCROLL_STATE_IDLE);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("DY", ""+dy);
                if (messagesllManager.findLastCompletelyVisibleItemPosition()==((MessagesAdapter)messages.getAdapter()).getMessages().size()-1) goDown.setVisibility(View.GONE);
                else goDown.setVisibility(View.VISIBLE);

                if ((dy>0 || dy<0) && dateIndicator.getVisibility()==View.GONE) dateIndicator.setVisibility(View.VISIBLE);
            }
        });
        goDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.scrollToPosition(((MessagesAdapter)messages.getAdapter()).getItemCount()-1);
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
        replyMessageContainer.setVisibility(View.GONE);
    }

    private void sendMessageManager(){
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emojiPopup.isShowing()) {
                    emoji.setImageResource(R.drawable.ic_keyboard);
                    emojiPopup.dismiss();
                }
                else {
                    emoji.setImageResource(R.drawable.ic_insert_emoticon);
                    emojiPopup.toggle();
                }
            }
        });
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (text.getText().length()>0) {
                    hideFileContainer();
                }
                else {
                    showFileContainer();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileChooserBottomFragment fileChooserBottomFragment = new FileChooserBottomFragment();
                fileChooserBottomFragment.show(getSupportFragmentManager(), FileChooserBottomFragment.class.getSimpleName());
            }
        });
        voicenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        showFileContainer();
    }

    private void hideFileContainer(){
        send.setVisibility(View.VISIBLE);
        fileContainer.setVisibility(View.GONE);
    }

    private void showFileContainer(){
        send.setVisibility(View.GONE);
        fileContainer.setVisibility(View.VISIBLE);
    }

    private void initRecordContainer(){
        textmessageContainer.setVisibility(View.VISIBLE);
        recordContainer.setVisibility(View.GONE);
        voicenote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                textmessageContainer.setVisibility(View.GONE);
                recordContainer.setVisibility(View.VISIBLE);
                launchRecord();
                return true;
            }
        });
        closeRecordContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecord();
                if (mAudioFile!=null && mAudioFile.exists()) mAudioFile.delete();
                textmessageContainer.setVisibility(View.VISIBLE);
                recordContainer.setVisibility(View.GONE);
            }
        });
        sendRecordedAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecord();
                //PLAY RECORDED
                textmessageContainer.setVisibility(View.VISIBLE);
                recordContainer.setVisibility(View.GONE);
            }
        });
    }

    private void test(){
        messages.setLayoutManager(messagesllManager);
        MessagesAdapter adapter = new MessagesAdapter(this);
        messages.setAdapter(adapter);
        adapter.setMessages(Message.random(57));
        messagesllManager.scrollToPosition(messages.getAdapter().getItemCount()-1);
    }

    @Override
    public void click(int position) {
        Toast.makeText(this, "CLICK " + position, Toast.LENGTH_SHORT).show();
        if (longClick){
            if (longClickSelectedItems.contains(position)){
                longClickSelectedItems.remove(new Integer(position));
                if (longClickSelectedItems.size()==0) longClick = false;
            }
            else{
                longClickSelectedItems.add(position);
            }
            ((MessagesAdapter)messages.getAdapter()).selectItems(longClickSelectedItems);
            onCreateOptionsMenu(activityMenu);
        }
    }

    @Override
    public void longClick(int position) {
        Toast.makeText(this, "LONG CLICK " + position, Toast.LENGTH_SHORT).show();
        longClick = true;
        longClickSelectedItems.add(position);
        ((MessagesAdapter)messages.getAdapter()).selectItems(longClickSelectedItems);
        onCreateOptionsMenu(activityMenu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        activityMenu = menu;
        activityMenu.clear();
        menuManager();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.reply:{
                replyMessageContainer.setVisibility(View.VISIBLE);
                longClickSelectedItems = new ArrayList<>();
                longClick = false;
                ((MessagesAdapter)messages.getAdapter()).selectItems(longClickSelectedItems);
                onCreateOptionsMenu(activityMenu);
            } break;
            case R.id.copy:{
                Toast.makeText(this, "COPY MESSAGE", Toast.LENGTH_SHORT).show();
            } break;
            case R.id.forward:{
                Toast.makeText(this, "FORWARD MESSAGE", Toast.LENGTH_SHORT).show();
            } break;
            case R.id.delete:{
                Toast.makeText(this, "DELETE MESSAGE", Toast.LENGTH_SHORT).show();
            } break;
            case R.id.app_bar_search:{
                Toast.makeText(this, "SEARCH MESSAGE", Toast.LENGTH_SHORT).show();
            } break;
            case R.id.empty:{
                Toast.makeText(this, "EMPTY DISCUSSION", Toast.LENGTH_SHORT).show();
            } break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deselectAll(){
        if (longClickSelectedItems!=null && longClickSelectedItems.size()>0){
            for (int i=0; i<longClickSelectedItems.size(); i++) {
                System.out.println(longClickSelectedItems.get(i).intValue());
                click(longClickSelectedItems.get(i).intValue());
            }
        }
    }

    private void menuManager(){
        if (longClick) {
            toolbarContent.setVisibility(View.GONE);
            toolbar.setTitle(String.valueOf(longClickSelectedItems.size()));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    longClickSelectedItems = new ArrayList<>();
                    longClick = false;
                    ((MessagesAdapter)messages.getAdapter()).selectItems(longClickSelectedItems);
                    onCreateOptionsMenu(activityMenu);
                }
            });
            getMenuInflater().inflate(R.menu.discussion_menu_select, activityMenu);

            if (longClickSelectedItems.size()>1) activityMenu.getItem(0).setVisible(false);
            else activityMenu.getItem(0).setVisible(true);
        }
        else {
            toolbarContent.setVisibility(View.VISIBLE);
            getMenuInflater().inflate(R.menu.discussion_menu, activityMenu);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    @Override
    public void cameraHandler(int type) {
        switch (type){
            case TYPE_PHOTO:{
                photoChoice();
            } break;
            case TYPE_VIDEO:{
                videoChoice();
            } break;
        }
    }

    private void photoChoice(){
        ArrayList<String> permsDenied = new ArrayList<>();
        boolean perms = checkPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, permsDenied);
        if (!perms){
            ActivityCompat.requestPermissions(this, U.toArray(permsDenied), TYPE_PHOTO);
            return;
        }
        else{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageFileUri = FilesUtils.getOutputImageFileURI(0);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
            startActivityForResult(intent, TYPE_PHOTO);
        }
    }

    private void videoChoice(){
        ArrayList<String> permsDenied = new ArrayList<>();
        boolean perms = checkPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.CAPTURE_VIDEO_OUTPUT, Manifest.permission.WRITE_EXTERNAL_STORAGE}, permsDenied);
        if (!perms){
            ActivityCompat.requestPermissions(this, U.toArray(permsDenied), TYPE_VIDEO);
            return;
        }
        else{
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoFileUri = FilesUtils.getOutputImageFileURI(0);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoFileUri);
            startActivityForResult(intent, TYPE_VIDEO);
        }
    }

    private boolean checkPermissions(String[] permissions, final ArrayList<String> permissionsDenied){
        if (Build.VERSION.SDK_INT >= 23) {
            boolean res = true;
            for (int i=0; i<permissions.length; i++){
                if (this.checkSelfPermission(permissions[i])!= PackageManager.PERMISSION_GRANTED){
                    res = false;
                    permissionsDenied.add(permissions[i]);
                }
            }
            return res;
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case TYPE_VIDEO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    videoChoice();
                } else {
                    Toast.makeText(this, getString(R.string.gallery_permissions_denied), Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case TYPE_PHOTO : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    photoChoice();
                } else {
                    Toast.makeText(this, getString(R.string.camera_permissions_denied), Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TYPE_PHOTO){
            if (resultCode == RESULT_OK){
                String imageFilePath = imageFileUri.getPath();
                //SET LOCAL PHOTO, UPLOAD AND SHOW IN VIEW
                //showPp();
                Toast.makeText(this, imageFilePath, Toast.LENGTH_SHORT).show();
            }
            else{
            }
        }
        else if (requestCode == TYPE_VIDEO){
            if (resultCode == RESULT_OK){
                String videoFilePath = videoFileUri.getPath();
                //SET LOCAL PHOTO, UPLOAD AND SHOW IN VIEW
                //showPp();
                Toast.makeText(this, videoFilePath, Toast.LENGTH_SHORT).show();
            }
            else{

            }
        }
        else if (requestCode == U.SELECT_GALLERY_REQ_CODES){
            if (resultCode == RESULT_OK && data!=null){
                String s = data.getStringExtra("LEGEND") + "\n\n";
                ArrayList<String> selected = (ArrayList<String>) data.getSerializableExtra("SELECTED");
                for (String ss: selected) s = s + ss + "\n";
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == U.SELECT_AUDIOS_REQ_CODES){
            if (resultCode == RESULT_OK && data!=null){
                String s = data.getStringExtra("AUDIOS") + "\n";
                ArrayList<String> selected = (ArrayList<String>) data.getSerializableExtra("SELECTED_AUDIOS");
                for (String ss: selected) s = s + ss + "\n";
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == U.SELECT_DOCS_REQ_CODES){
            if (resultCode == RESULT_OK && data!=null){
                String s = data.getStringExtra("DOCUMENTS") + "\n";
                ArrayList<String> selected = (ArrayList<String>) data.getSerializableExtra("SELECTED_DOCUMENTS");
                for (String ss: selected) s = s + ss + "\n";
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void launchRecord(){
        mAudioRecorder = AudioRecorder.getInstance();
        mAudioFile = FilesUtils.newVoiceNote();
        mAudioRecorder.prepareRecord(MediaRecorder.AudioSource.MIC,
                MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.AudioEncoder.AAC,
                mAudioFile);
        isRecording = true;
        mAudioRecorder.startRecord();
        launchTimeThread();
    }

    private void stopRecord(){
        if (mAudioRecorder!=null) mAudioRecorder.stopRecord();
        isRecording = false;
    }

    private void launchTimeThread(){
        new Thread(new Runnable() {
            int ttime;
            @Override
            public void run() {
                while(isRecording){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ttime++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String sec = String.valueOf(ttime%60);
                            String min = String.valueOf(ttime/60);
                            if (sec.length()==1) sec = "0"+sec;
                            if (min.length()==1) min = "0"+min;
                            recordedTime.setText(min+":"+sec);
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recordedTime.setText("00:00");
                    }
                });
            }
        }).start();
    }

}
