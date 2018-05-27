package ensp.reseau.wiatalk.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;
import ensp.reseau.wiatalk.tmodels.utils.Bucket;

public class GalleryBucketActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private GridView gridBuckets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_bucket);
        toolbar = findViewById(R.id.toolbar);
        gridBuckets = findViewById(R.id.gallery_bucket);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.gallery_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        GridBucketAdapter adapter = new GridBucketAdapter();
        gridBuckets.setAdapter(adapter);
        showBuckets();
        gridBuckets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(GalleryBucketActivity.this, GalleryActivity.class);
                intent.putExtra(Bucket.class.getSimpleName(), ((GridBucketAdapter)gridBuckets.getAdapter()).buckets.get(i));
                GalleryBucketActivity.this.startActivityForResult(intent, U.SELECT_GALLERY_REQ_CODES);
            }
        });
    }

    private void showBuckets(){
        if (U.checkPermission(this)) ((GridBucketAdapter)gridBuckets.getAdapter()).setBuckets(getMediaBuckets());
    }

    private class GridBucketAdapter extends BaseAdapter{

        private ArrayList<Bucket> buckets;

        public void setBuckets(ArrayList<Bucket> buckets) {
            this.buckets = buckets;
            notifyDataSetChanged();
        }

        public void add(Bucket bucket){
            if (buckets==null) buckets = new ArrayList<>();
            buckets.add(bucket);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return buckets==null?0:buckets.size();
        }

        @Override
        public Object getItem(int i) {
            return buckets.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup container) {
            LayoutInflater inflater = LayoutInflater.from(GalleryBucketActivity.this);
            if (convertView==null) convertView = inflater.inflate(R.layout.gallery_grid_bucket_itemview, null);

            Bucket bucket = buckets.get(i);

            ImageView bucketLastMedia = convertView.findViewById(R.id.bucket_last_media);
            TextView bucketNameImage = convertView.findViewById(R.id.bucket_name_image);
            TextView bucketNameVideo = convertView.findViewById(R.id.bucket_name_video);

            if (bucket.isPhoto()) {
                bucketNameImage.setVisibility(View.VISIBLE);
                bucketNameVideo.setVisibility(View.INVISIBLE);
                bucketNameImage.setText(bucket.getName());
            }
            else {
                bucketNameImage.setVisibility(View.INVISIBLE);
                bucketNameVideo.setVisibility(View.VISIBLE);
                bucketNameVideo.setText(bucket.getName());
            }

            Glide.with(GalleryBucketActivity.this).load(bucket.getFirstImageContainedPath())
                    .placeholder(R.mipmap.logo).centerCrop()
                    .into(bucketLastMedia);

            return convertView;
        }
    }

    public ArrayList<Bucket> getImageBuckets(){
        ArrayList<Bucket> buckets = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA};

        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            File file;
            while (cursor.moveToNext()){
                String bucketPath = cursor.getString(cursor.getColumnIndex(projection[0]));
                String fisrtImage = cursor.getString(cursor.getColumnIndex(projection[1]));
                String bucketName = bucketPath.substring(bucketPath.lastIndexOf("/")+1, bucketPath.length());
                file = new File(fisrtImage);
                Bucket bucket = new Bucket(bucketName, bucketPath, fisrtImage);
                if (file.exists() && !buckets.contains(bucket)) {
                    buckets.add(bucket);
                }
            }
            cursor.close();
        }
        return buckets;
    }

    public ArrayList<Bucket> getVideoBuckets(){
        ArrayList<Bucket> buckets = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA};

        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            File file;
            while (cursor.moveToNext()){
                String bucketPath = cursor.getString(cursor.getColumnIndex(projection[0]));
                String fisrtVideo = cursor.getString(cursor.getColumnIndex(projection[1]));
                String bucketName = bucketPath.substring(bucketPath.lastIndexOf("/")+1, bucketPath.length());
                file = new File(fisrtVideo);
                Bucket bucket = new Bucket(bucketName, bucketPath, fisrtVideo, false);
                if (file.exists() && !buckets.contains(bucket)) {
                    buckets.add(bucket);
                }
            }
            cursor.close();
        }
        return buckets;
    }

    public ArrayList<Bucket> getMediaBuckets(){
        ArrayList<Bucket> allMedias = new ArrayList<>();
        ArrayList<Bucket> images = getImageBuckets();
        ArrayList<Bucket> videos = getVideoBuckets();
        for (Bucket bucket: images) allMedias.add(bucket);
        for (Bucket bucket: videos) allMedias.add(bucket);
        return allMedias;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case U.READ_EXTERNAL_STORAGE_REQ_PERM_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // Permission granted
                    showBuckets();
                }else {
                    // Permission denied
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == U.SELECT_GALLERY_REQ_CODES && resultCode==RESULT_OK && data!=null){
            String s = data.getStringExtra("LEGEND") + "\n\n";
            ArrayList<String> selected = (ArrayList<String>) data.getSerializableExtra("SELECTED");
            for (String ss: selected) s = s + ss + "\n";
            Log.i("SELECTED", s);
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
