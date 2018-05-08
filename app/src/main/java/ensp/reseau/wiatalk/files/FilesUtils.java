package ensp.reseau.wiatalk.files;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Sim'S on 04/05/2018.
 */

public class FilesUtils {
    private static final String TopSalesProfilesImgs = File.separator+"TopSalesData"+File.separator+"ProfileImg";

    public static Uri getOutputImageFileURI(int userID){
        return Uri.fromFile(getOutputImageFile(userID));
    }

    public static File getOutputImageFile(int userID){
        // External sdcard location
        final String WiaTalkMyProfileImg = TopSalesProfilesImgs + File.separator + "Me";
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                WiaTalkMyProfileImg);
        //File mediaStorageDir = new File(TopSalesProfilesImgs, LaLaLaWWorkerProfileFolder);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + TopSalesProfilesImgs + " directory");
                return null;
            }
        }
        // Create a media file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new java.util.Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + String.valueOf(userID)+".jpg");
        if (mediaFile.exists()) mediaFile.delete();
        return mediaFile;
    }

    public static String copyToTopSalesImagesDirectory(String imageFilePath, int userID) throws FileNotFoundException, IOException {
        File file = new File(imageFilePath);

        final String WiaTalkMyProfileImg = TopSalesProfilesImgs;
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                WiaTalkMyProfileImg);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + TopSalesProfilesImgs + " directory");
                return null;
            }
        }

        File copy = new File(mediaStorageDir.getPath()+File.separator+userID+".jpg");
        if (copy.exists()) copy.delete();
        if (!copy.exists()) copy.createNewFile();

        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(copy);
        byte[] bytes = new byte[1024];
        int bytesRead = 0;
        try{
            while ((bytesRead = fis.read(bytes)) > 0){
                fos.write(bytes, 0, bytesRead);
            }
        } finally {
            fis.close();
            fos.close();
        }
        return copy.getPath();
    }

    public static String UriToPath(Uri imageFileUri, Context context){
        Cursor cursor = context.getContentResolver().query(imageFileUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = context.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }
}
