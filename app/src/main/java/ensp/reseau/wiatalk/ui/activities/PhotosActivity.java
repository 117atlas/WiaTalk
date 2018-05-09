package ensp.reseau.wiatalk.ui.activities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;

public class PhotosActivity extends AppCompatActivity {
    private GridView photos;
    private ArrayList<String> selectedPhotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photos = findViewById(R.id.gallery_photos);
        GridPhotosAdapter adapter = new GridPhotosAdapter(this);
        photos.setAdapter(adapter);
        photos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GridPhotosAdapter adapter = (GridPhotosAdapter)adapterView.getAdapter();
                Log.d("PHOTOS ITEM CLICK", "PIC " + i);
                /*if (photoItemView.isSelected()) {
                    selectedPhotos.remove((int)l);
                    photoItemView.deselect();
                    Log.d("DESELECT", "Position " + l);
                }
                else{
                    selectedPhotos.add(adapter.getPhotos().get((int)l));
                    photoItemView.select();
                    Log.d("SELECT", "Position " + l);
                }*/
            }
        });
        adapter.setPhotos();
    }

    private class GridPhotosAdapter extends BaseAdapter{
        private Context context;
        private ArrayList<String> photos;

        public ArrayList<String> getPhotos() {
            return photos;
        }

        public GridPhotosAdapter(Context context) {
            this.context = context;
        }

        public void setPhotos(){
            photos = getAllShownImagesPath(context);
            Log.d("PHOTOS", ""+photos.size());
            notifyDataSetChanged();
        }

        public int getCount() {
            return photos==null?0:photos.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            PhotoItemView rView;
            if (convertView!=null && convertView instanceof PhotoItemView) rView = (PhotoItemView) convertView;
            else {
                rView = new PhotoItemView(context);
                rView.buildView(parent);
            }
            rView.bind(position, photos);
            return rView.root;
            /*ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView
                        .setLayoutParams(new GridView.LayoutParams(270, 270));

            } else {
                picturesView = (ImageView) convertView;
            }

            Glide.with(context).load(photos.get(position))
                    .placeholder(R.mipmap.logo).centerCrop()
                    .into(picturesView);

            return picturesView;*/
        }
    }

    private class PhotoItemView extends View{
        private ImageView photo;
        private CheckBox select;
        private Context context;
        private View root;
        private int currentPosition;

        public View getRoot() {
            return root;
        }

        public PhotoItemView(Context context) {
            super(context);
            this.context = context;
        }

        public void buildView(ViewGroup parent){
            LayoutInflater inflater = LayoutInflater.from(context);
            root = inflater.inflate(R.layout.photo_itemview, parent, false);
            photo = root.findViewById(R.id.photo);
            select = root.findViewById(R.id.select);
            select.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        public void buildView(View root){
            this.root = root;
            photo = root.findViewById(R.id.photo);
            select = root.findViewById(R.id.select);
        }

        public void bind(int position, ArrayList<String> photos){
            currentPosition = position;
            Glide.with(context).load(photos.get(position))
                    .placeholder(R.mipmap.logo).centerCrop()
                    .into(photo);
        }

        public void select(){
            select.setSelected(true);
        }

        public void deselect(){
            select.setSelected(false);
        }

        public boolean isSelected(){
            return select!=null&&select.isSelected();
        }

    }

    private ArrayList<String> getAllShownImagesPath(Context context) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
        cursor = context.getContentResolver().query(uri, projection, null,
                null, MediaStore.Images.Media.DATE_ADDED);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }
}
