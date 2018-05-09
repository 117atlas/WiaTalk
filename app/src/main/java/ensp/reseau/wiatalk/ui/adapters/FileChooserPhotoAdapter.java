package ensp.reseau.wiatalk.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.U;

/**
 * Created by Sim'S on 09/05/2018.
 */

public class FileChooserPhotoAdapter extends RecyclerView.Adapter<FileChooserPhotoAdapter.FileChooserPhotoViewHolder> {
    private Context context;
    private ArrayList<String> photos;

    public FileChooserPhotoAdapter(Context context){this.context = context;}

    public void setPhotos(ArrayList<String> photos) {
        //this.photos = photos;
        this.photos = getAllShownImagesPath();
        notifyDataSetChanged();
    }

    public void add(String p){
        if (photos==null) photos = new ArrayList<>();
        photos.add(p);
        notifyDataSetChanged();
    }

    @Override
    public FileChooserPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new FileChooserPhotoViewHolder(inflater.inflate(R.layout.filechooser_photo_itemview, parent, false));
    }

    @Override
    public void onBindViewHolder(FileChooserPhotoViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return photos==null?0:photos.size();
    }

    class FileChooserPhotoViewHolder extends RecyclerView.ViewHolder{
        private ImageView photo;
        private int currentPosition;
        public FileChooserPhotoViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //((IOnePhotoChoosenHandler)context).onePhotoChoosen(photos.get(currentPosition));
                }
            });
        }
        public void bind(int position){
            currentPosition = position;
            //U.loadImage(context, photo, "");
            Glide.with(context).load(photos.get(position))
                    .placeholder(R.mipmap.logo).centerCrop()
                    .into(photo);
        }
    }

    private ArrayList<String> getAllShownImagesPath() {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
        cursor = context.getContentResolver().query(uri, projection, null,
                null, null);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }
}
