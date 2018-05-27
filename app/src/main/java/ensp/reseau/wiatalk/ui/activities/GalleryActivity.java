package ensp.reseau.wiatalk.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.tmodels.utils.Bucket;

public class GalleryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton send;
    private TextView legend;
    private TextView numberSelectedItems;
    private GridView medias;

    private Bucket bucket;
    private ArrayList<String> selectedMedias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        bucket = (Bucket) getIntent().getSerializableExtra(Bucket.class.getSimpleName());
        initializeWidgets();
    }

    private void initializeWidgets(){
        toolbar = findViewById(R.id.toolbar);
        send = findViewById(R.id.send);
        legend = findViewById(R.id.legend);
        numberSelectedItems = findViewById(R.id.number_items);
        medias = findViewById(R.id.medias);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(bucket==null?getString(R.string.app_name):bucket.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedMedias.size()==0) {
                    //onBackPressed();
                    Toast.makeText(GalleryActivity.this, "No Media Selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Intent output = new Intent();
                    output.putExtra("SELECTED", selectedMedias);
                    output.putExtra("LEGEND", legend.getText().toString());
                    setResult(RESULT_OK, output);
                    finish();
                }
            }
        });

        numberSelectedItems.setText(String.valueOf(selectedMedias.size()));
        medias.setAdapter(new GridMediaAdapter());

        medias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (bucket.isPhoto()) ((GridMediaAdapter)medias.getAdapter()).select(i, view);
                else {
                    String path = ((GridMediaAdapter)medias.getAdapter()).getMedias().get(i);
                    /*VideoViewerFragment videoViewerFragment = VideoViewerFragment.newInstance(path);
                    videoViewerFragment.show(getSupportFragmentManager(), VideoViewerFragment.class.getSimpleName());*/
                    Intent intent = new Intent(GalleryActivity.this, VideoViewActivity.class);
                    intent.putExtra("PATH", path);
                    startActivity(intent);
                }
            }
        });
        showMedias();
    }

    public void showMedias(){
        if (U.checkPermission(this)) ((GridMediaAdapter)medias.getAdapter()).setMedias(bucket.isPhoto()?getImagesByBucket():getVideosByBucket());
    }

    private class GridMediaAdapter extends BaseAdapter{
        private ArrayList<String> medias;

        public ArrayList<String> getMedias() {
            return medias;
        }

        public void setMedias(ArrayList<String> medias) {
            this.medias = medias;
            notifyDataSetChanged();
        }

        public void add(String s){
            if (medias==null) medias = new ArrayList<>();
            medias.add(s);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return medias==null?0:medias.size();
        }

        @Override
        public Object getItem(int i) {
            return medias.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(GalleryActivity.this);
            if (view == null) view = inflater.inflate(R.layout.gallery_grid_itemview, null);

            ImageView mediaImage = view.findViewById(R.id.media);
            ImageView vidIndicator = view.findViewById(R.id.video_indicator);
            final CheckBox select = view.findViewById(R.id.select);
            final int position = i;

            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectManager(position, select);
                }
            });

            if (bucket.isPhoto()){
                vidIndicator.setVisibility(View.INVISIBLE);
                Glide.with(GalleryActivity.this).load(medias.get(i))
                        .placeholder(R.mipmap.logo).centerCrop()
                        .into(mediaImage);
            }
            else{
                vidIndicator.setVisibility(View.VISIBLE);
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(medias.get(i), MediaStore.Video.Thumbnails.MICRO_KIND);
                mediaImage.setImageBitmap(thumb);
                mediaImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            return view;
        }

        public void select(int position, View view){
            final CheckBox select = view.findViewById(R.id.select);
            select.setChecked(!select.isChecked());
            selectManager(position, select);
        }

        public void selectManager (int position, CheckBox select){
            if (select.isChecked() && !selectedMedias.contains(medias.get(position))) selectedMedias.add(medias.get(position));
            else if (!select.isChecked() && selectedMedias.contains(medias.get(position))) selectedMedias.remove(medias.get(position));
            numberSelectedItems.setText(String.valueOf(selectedMedias.size()));
        }
    }

    public ArrayList<String> getImagesByBucket(){

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME+" =?";
        String orderBy = MediaStore.Images.Media.DATE_ADDED+" DESC";

        ArrayList<String> images = new ArrayList<>();

        Cursor cursor = this.getContentResolver().query(uri, projection, selection,new String[]{bucket.getPath()}, orderBy);

        if(cursor != null){
            File file;
            while (cursor.moveToNext()){
                String path = cursor.getString(cursor.getColumnIndex(projection[0]));
                file = new File(path);
                if (file.exists() && !images.contains(path)) {
                    images.add(path);
                }
            }
            cursor.close();
        }
        return images;
    }

    public ArrayList<String> getVideosByBucket(){

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {MediaStore.Video.Media.DATA};
        String selection = MediaStore.Video.Media.BUCKET_DISPLAY_NAME+" =?";
        String orderBy = MediaStore.Video.Media.DATE_ADDED+" DESC";

        ArrayList<String> videos = new ArrayList<>();

        Cursor cursor = this.getContentResolver().query(uri, projection, selection,new String[]{bucket.getPath()}, orderBy);

        if(cursor != null){
            File file;
            while (cursor.moveToNext()){
                String path = cursor.getString(cursor.getColumnIndex(projection[0]));
                file = new File(path);
                if (file.exists() && !videos.contains(path)) {
                    videos.add(path);
                }
            }
            cursor.close();
        }
        return videos;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case U.READ_EXTERNAL_STORAGE_REQ_PERM_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Permission granted
                    showMedias();
                }else {
                    // Permission denied
                }
            }
        }
    }
}
