package ensp.reseau.wiatalk.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import ensp.reseau.wiatalk.R;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AppCompatButton myprofile;
    private Switch notifications;
    private Switch downloadAll;
    private Switch downloadPhotos;
    private Switch downloadVideos;
    private Switch downloadAudios;
    private Switch downloadDocuments;
    private Switch storeDataInLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initializeWidgets();
    }

    private void initializeWidgets(){
        toolbar = findViewById(R.id.toolbar);
        myprofile = findViewById(R.id.myprofile);
        notifications = findViewById(R.id.notifications);
        downloadAll = findViewById(R.id.auto_download);
        downloadAudios = findViewById(R.id.auto_download_audios);
        downloadDocuments = findViewById(R.id.auto_download_documents);
        downloadPhotos = findViewById(R.id.auto_download_photos);
        downloadVideos = findViewById(R.id.auto_download_videos);
        storeDataInLocal = findViewById(R.id.storage_data);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        View.OnClickListener switchsListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.notifications:{
                        Toast.makeText(SettingsActivity.this, "NOTIFS " + notifications.isChecked(), Toast.LENGTH_SHORT).show();
                    } break;
                    case R.id.auto_download:{
                        Toast.makeText(SettingsActivity.this, "AUTO DOWN " + downloadAll.isChecked(), Toast.LENGTH_SHORT).show();
                    } break;
                    case R.id.auto_download_audios:{
                        Toast.makeText(SettingsActivity.this, "AUTO DOWN AUDIOS " + downloadAudios.isChecked(), Toast.LENGTH_SHORT).show();
                    } break;
                    case R.id.auto_download_documents:{
                        Toast.makeText(SettingsActivity.this, "AUTO DOWN DOCS " + downloadDocuments.isChecked(), Toast.LENGTH_SHORT).show();
                    } break;
                    case R.id.auto_download_photos:{
                        Toast.makeText(SettingsActivity.this, "AUTO DOWN PHOTOS " + downloadPhotos.isChecked(), Toast.LENGTH_SHORT).show();
                    } break;
                    case R.id.auto_download_videos:{
                        Toast.makeText(SettingsActivity.this, "AUTO DOWN VIDEOS " + downloadVideos.isChecked(), Toast.LENGTH_SHORT).show();
                    } break;
                    case R.id.storage_data:{
                        Toast.makeText(SettingsActivity.this, "STORAGE DATA " + storeDataInLocal.isChecked(), Toast.LENGTH_SHORT).show();
                    } break;
                }
            }
        };
        notifications.setOnClickListener(switchsListener);
        downloadAll.setOnClickListener(switchsListener);
        downloadAudios.setOnClickListener(switchsListener);
        downloadDocuments.setOnClickListener(switchsListener);
        downloadPhotos.setOnClickListener(switchsListener);
        downloadVideos.setOnClickListener(switchsListener);
        storeDataInLocal.setOnClickListener(switchsListener);

        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "MY PROFILE", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
