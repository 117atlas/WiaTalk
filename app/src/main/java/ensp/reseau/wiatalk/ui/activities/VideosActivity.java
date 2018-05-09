package ensp.reseau.wiatalk.ui.activities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.VideoView;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;

public class VideosActivity extends AppCompatActivity {
    private GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
    }

    class GridVideosAdapter extends BaseAdapter{
        private Context context;
        private ArrayList<String> videos;

        public GridVideosAdapter(Context context) {
            this.context = context;
            videos = getAllShownImagesPath(context);
        }

        @Override
        public int getCount() {
            return videos==null?0:videos.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            /*VideoView videoView;
            if (convertView == null) {
                videoView = new VideoView(context);
                videoView.set
                videoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                videoView
                        .setLayoutParams(new GridView.LayoutParams(270, 270));

            } else {
                videoView = (ImageView) convertView;
            }

            Glide.with(context).load(photos.get(position))
                    .placeholder(R.mipmap.logo).centerCrop()
                    .into(videoView);

            return videoView;*/
            return null;
        }
    }

    private ArrayList<String> getAllShownImagesPath(Context context) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllVideos = new ArrayList<String>();
        String absolutePathOfVideo = null;
        uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME };
        cursor = context.getContentResolver().query(uri, projection, null,
                null, MediaStore.Video.Media.DATE_ADDED);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfVideo = cursor.getString(column_index_data);
            listOfAllVideos.add(absolutePathOfVideo);
        }
        return listOfAllVideos;
    }
}
