package ensp.reseau.wiatalk.files;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
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
    private static final String WiaTalkProfilesImgs = File.separator+"WiaTalkData"+File.separator+"ProfileImg";
    private static final String WiaTalkPhoMess = File.separator+"WiaTalkData"+File.separator+"PhoMessage";
    private static final String WiaTalkVidMess = File.separator+"WiaTalkData"+File.separator+"VidMessage";

    public static Uri getOutputImageFileURI(int userID){
        return Uri.fromFile(getOutputImageFile(userID));
    }

    public static Uri getOutputImageFileUriForGroupCreated(int id){
        return Uri.fromFile(getOutputImageFile(id, File.separator+"WiaTalkData"+File.separator+"GroupeCreatedpp"));
    }

    public static Uri getMyPpUri(String id, Context context){
        return Uri.fromFile(getMyPpFile(id, context));
    }

    public static Uri getOthersPpsUri(String id, Context context){
        return Uri.fromFile(getOthersPpFile(id, context));
    }

    public static File getMyPpFile(String id, Context context){
        // External sdcard location
        final String WiaTalkMyPpImg =  File.separator + "MyPp";
        File mediaStorageDir = new File(context.getFilesDir(), WiaTalkMyPpImg);
        //File mediaStorageDir = new File(TopSalesProfilesImgs, LaLaLaWWorkerProfileFolder);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + WiaTalkMyPpImg + " directory");
                return null;
            }
        }
        // Create a media file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new java.util.Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + String.valueOf(id)+".jpg");
        if (mediaFile.exists()) mediaFile.delete();
        return mediaFile;
    }

    public static File getOthersPpFile(String id, Context context){
        // External sdcard location
        final String WiaTalkMyPpImg =  File.separator + "OthersPps";
        File mediaStorageDir = new File(context.getFilesDir(), WiaTalkMyPpImg);
        //File mediaStorageDir = new File(TopSalesProfilesImgs, LaLaLaWWorkerProfileFolder);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + WiaTalkMyPpImg + " directory");
                return null;
            }
        }
        // Create a media file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new java.util.Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + String.valueOf(id)+".jpg");
        //if (mediaFile.exists()) mediaFile.delete();
        return mediaFile;
    }

    public static File newVoiceNote(){
        final String parentDir = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"WiaTalk";
        File parent = new File(parentDir);
        if (!parent.exists()){
            if (!parent.mkdirs()){
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + parentDir + " directory");
                return null;
            }
        }
        File vn = new File(parentDir+File.separator+"VN"+System.nanoTime()+".vn.m4a");
        if (vn.exists()) vn.delete();
        try {
            vn.createNewFile();
            return vn;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("MKDIR ERR", "Oops! Failed create "
                    + vn.getAbsolutePath() + " directory");
            return null;
        }
    }

    public static File getOutputImageFile(int userID, String rootFolder){
        // External sdcard location
        final String WiaTalkMyProfileImg = rootFolder + File.separator + "Me";
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                WiaTalkMyProfileImg);
        //File mediaStorageDir = new File(TopSalesProfilesImgs, LaLaLaWWorkerProfileFolder);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + WiaTalkMyProfileImg + " directory");
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

    public static File getOutputImageFile(int userID){
        // External sdcard location
        final String WiaTalkMyProfileImg = WiaTalkProfilesImgs + File.separator + "Me";
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                WiaTalkMyProfileImg);
        //File mediaStorageDir = new File(TopSalesProfilesImgs, LaLaLaWWorkerProfileFolder);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + WiaTalkMyProfileImg + " directory");
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

    public static File getOutputVideoFile(int userID){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                WiaTalkVidMess);
        //File mediaStorageDir = new File(TopSalesProfilesImgs, LaLaLaWWorkerProfileFolder);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + WiaTalkVidMess + " directory");
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

    public static File getOutputImageFileCamMessage(int userID){
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                WiaTalkPhoMess);
        //File mediaStorageDir = new File(TopSalesProfilesImgs, LaLaLaWWorkerProfileFolder);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + WiaTalkPhoMess + " directory");
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

    public static String copyToWiaTalkImagesDirectory(String imageFilePath, int userID) throws FileNotFoundException, IOException {
        File file = new File(imageFilePath);

        final String WiaTalkMyProfileImg = WiaTalkProfilesImgs;
        /*File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                WiaTalkMyProfileImg);*/
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                File.separator+"WiaTalkData"+File.separator+"GroupeCreatedpp");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + WiaTalkMyProfileImg + " directory");
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

    public static String copyToMyPp(String imageFilePath, String id, Context context) throws FileNotFoundException, IOException {
        File file = new File(imageFilePath);

        final String WiaTalkMyPpImg =  File.separator + "MyPp";
        File mediaStorageDir = new File(context.getFilesDir(), WiaTalkMyPpImg);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + WiaTalkMyPpImg + " directory");
                return null;
            }
        }

        File copy = new File(mediaStorageDir.getPath()+File.separator+id+".jpg");
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

    public static String copyToOthersPp(String imageFilePath, String id, Context context) throws FileNotFoundException, IOException {
        File file = new File(imageFilePath);

        final String WiaTalkMyPpImg =  File.separator + "OthersPps";
        File mediaStorageDir = new File(context.getFilesDir(), WiaTalkMyPpImg);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + WiaTalkMyPpImg + " directory");
                return null;
            }
        }

        File copy = new File(mediaStorageDir.getPath()+File.separator+id+".jpg");
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

    public static String copyToWiaTalkPhoMessDirectory(String imageFilePath) throws FileNotFoundException, IOException {
        File file = new File(imageFilePath);

        final String WiaTalkMyProfileImg = WiaTalkPhoMess;
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                WiaTalkMyProfileImg);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + WiaTalkPhoMess + " directory");
                return null;
            }
        }

        File copy = new File(mediaStorageDir.getPath()+File.separator+SystemClock.currentThreadTimeMillis()+".jpg");
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

    public static String copyToWiaTalkVidMessDirectory(String videoFilePath) throws FileNotFoundException, IOException {
        File file = new File(videoFilePath);

        final String _WiaTalkVidMess = WiaTalkVidMess;
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                _WiaTalkVidMess);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + WiaTalkVidMess + " directory");
                return null;
            }
        }

        File copy = new File(mediaStorageDir.getPath()+File.separator+ SystemClock.currentThreadTimeMillis()+".mp4");
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
