package ensp.reseau.wiatalk.ui.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.models.utils.Audio;
import ensp.reseau.wiatalk.ui.adapters.AudiosAdapter;
import ensp.reseau.wiatalk.ui.adapters.DocumentsAdapter;
import ensp.reseau.wiatalk.ui.adapters.IFileChoosenHandler;

public class AudiosActivity extends AppCompatActivity implements IFileChoosenHandler{

    private Toolbar toolbar;
    private ImageButton close;
    private TextView numberItemsSelected;
    private ImageButton send;
    private RecyclerView audiosList;

    private ArrayList<String> selectedAudios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audios);
        initializeComponents();
        showAudios();
    }

    private void initializeComponents(){
        toolbar = findViewById(R.id.toolbar);
        audiosList = findViewById(R.id.audio_list);
        close = findViewById(R.id.back);
        numberItemsSelected = findViewById(R.id.number_items);
        send = findViewById(R.id.send);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.audios_activity));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        numberItemsSelected.setText(String.valueOf(selectedAudios.size()));
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = "";
                for (String d: selectedAudios) s = s + "\n" + d;
                Toast.makeText(AudiosActivity.this, s, Toast.LENGTH_SHORT).show();
                Log.i("SELECTED", s);
                onBackPressed();
                finish();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        audiosList.setLayoutManager(new LinearLayoutManager(this));
        audiosList.setAdapter(new AudiosAdapter(this));
    }

    public void showAudios(){
        if (U.checkPermission(this)) ((AudiosAdapter)audiosList.getAdapter()).setAudios(getMediaFileList());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case U.READ_EXTERNAL_STORAGE_REQ_PERM_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Permission granted
                    showAudios();
                }else {
                    // Permission denied
                }
            }
        }
    }

    protected ArrayList<Audio> getMediaFileList(){
        ContentResolver contentResolver = this.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(
                uri, // Uri
                null, // Projection
                null, // Selection
                null, // Selection args
                null // Sor order
        );
        if (cursor == null) {
            Log.e(AudiosActivity.class.getSimpleName(), "\n" +"Query failed, handle error.");
            return null;
        } else if (!cursor.moveToFirst()) {
            Log.e(AudiosActivity.class.getSimpleName(), "\n" +"Nno music found on the sd card.");
            return null;
        } else {
            ArrayList<Audio> audios = new ArrayList<>();
            int path = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            int title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int length = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            do {
                Audio audio = new Audio();
                audio.setFilePath(cursor.getString(path));
                audio.setFileName(audio.getFilePath().substring(audio.getFilePath().lastIndexOf("/")+1, audio.getFilePath().length()));
                audio.setTitle(cursor.getString(title));
                audio.setArtist(cursor.getString(artist));
                audio.setLength(""+cursor.getString(length));

                /*long thisId = cursor.getLong(id);
                mResult.append("\n\n" +thisId);
                // Get the current audio title
                String thisTitle = cursor.getString(title);
                mResult.append("\n" +thisTitle);*/
                // Process current music here
                audios.add(audio);
            } while (cursor.moveToNext());
            return audios;
        }
    }

    @Override
    public void fileChoosen(int position, boolean add) {
        if (add && !selectedAudios.contains(((AudiosAdapter)audiosList.getAdapter()).getAudios().get(position).getFilePath()))
            selectedAudios.add(((AudiosAdapter)audiosList.getAdapter()).getAudios().get(position).getFilePath());
        else
            selectedAudios.remove(((AudiosAdapter)audiosList.getAdapter()).getAudios().get(position).getFilePath());
        numberItemsSelected.setText(String.valueOf(selectedAudios.size()));
    }
}
