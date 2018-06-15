package ensp.reseau.wiatalk.localstorage;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ensp.reseau.wiatalk.model.MessageFile;
import ensp.reseau.wiatalk.tmodels.utils.Contact;

public class MessageFileUtils {

    private static final String WiaTalkSentPhoMess = File.separator+"WiaTalkData"+File.separator+"WiaTalk Photos"+File.separator+"Sent";
    private static final String WiaTalkSentVidMess = File.separator+"WiaTalkData"+File.separator+"WiaTalk Videos"+File.separator+"Sent";
    private static final String WiaTalkSentAudMess = File.separator+"WiaTalkData"+File.separator+"WiaTalk Audios"+File.separator+"Sent";
    private static final String WiaTalkSentDocMess = File.separator+"WiaTalkData"+File.separator+"WiaTalk Documents"+File.separator+"Sent";

    private static final String WiaTalkPhoMess = File.separator+"WiaTalkData"+File.separator+"WiaTalk Photos";
    private static final String WiaTalkVidMess = File.separator+"WiaTalkData"+File.separator+"WiaTalk Videos";
    private static final String WiaTalkAudMess = File.separator+"WiaTalkData"+File.separator+"WiaTalk Audios";
    private static final String WiaTalkDocMess = File.separator+"WiaTalkData"+File.separator+"WiaTalk Documents";

    public static File copySentVideoFile(String path){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                WiaTalkSentVidMess);
        return copySentFile(mediaStorageDir, path);
    }

    public static File copySentPhotoFile(String path){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                WiaTalkSentPhoMess);
        return copySentFile(mediaStorageDir, path);
    }

    public static File copySentAudioFile(String path){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                WiaTalkSentAudMess);
        return copySentFile(mediaStorageDir, path);
    }

    public static File copySentDocumentFile(String path){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                WiaTalkSentDocMess);
        return copySentFile(mediaStorageDir, path);
    }

    public static File copySentFile(File mediaStorageDir, String path){
        //File mediaStorageDir = new File(TopSalesProfilesImgs, LaLaLaWWorkerProfileFolder);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + mediaStorageDir + " directory");
                return null;
            }
        }
        // Create a media file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new java.util.Date());
        File destFile = new File(mediaStorageDir.getPath() + File.separator
                + path.substring(path.lastIndexOf(File.separator)+1, path.length()));
        //if (mediaFile.exists()) mediaFile.delete();
        if (copy(new File(path), destFile)) return destFile;
        else return null;
    }
    
    public static boolean copy(File src, File dest) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try{
            if (dest.exists()) dest.delete();
            if (!dest.exists()) dest.createNewFile();

            fis = new FileInputStream(src);
            fos = new FileOutputStream(dest);
            byte[] bytes = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = fis.read(bytes)) > 0){
                fos.write(bytes, 0, bytesRead);
            }
            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                if (fis!=null) fis.close();
                if (fos!=null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static File receivedVideoFile(MessageFile messageFile){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                WiaTalkVidMess);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + mediaStorageDir + " directory");
                return null;
            }
        }
        // Create a media file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new java.util.Date());
        File destFile = new File(mediaStorageDir.getPath() + File.separator
                + messageFile.get_id()+messageFile.getOriginalName().substring(messageFile.getOriginalName().lastIndexOf(".")+1, messageFile.getOriginalName().length()));
        if (destFile.exists()) destFile.delete();
        //if (copy(new File(path), destFile)) return destFile;
        return destFile;
    }

    public static File receivedPhotoFile(MessageFile messageFile){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                WiaTalkPhoMess);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + mediaStorageDir + " directory");
                return null;
            }
        }
        // Create a media file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new java.util.Date());
        File destFile = new File(mediaStorageDir.getPath() + File.separator
                + messageFile.get_id()+messageFile.getOriginalName().substring(messageFile.getOriginalName().lastIndexOf(".")+1, messageFile.getOriginalName().length()));
        if (destFile.exists()) destFile.delete();
        //if (copy(new File(path), destFile)) return destFile;
        return destFile;
    }

    public static File receivedAudioFile(MessageFile messageFile){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                WiaTalkAudMess);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + mediaStorageDir + " directory");
                return null;
            }
        }
        // Create a media file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new java.util.Date());
        File destFile = new File(mediaStorageDir.getPath() + File.separator
                + messageFile.get_id()+messageFile.getOriginalName().substring(messageFile.getOriginalName().lastIndexOf(".")+1, messageFile.getOriginalName().length()));
        if (destFile.exists()) destFile.delete();
        //if (copy(new File(path), destFile)) return destFile;
        return destFile;
    }

    public static File receivedDocumentFile(MessageFile messageFile){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.getRootDirectory().getAbsolutePath()),
                WiaTalkDocMess);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + mediaStorageDir + " directory");
                return null;
            }
        }
        // Create a media file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new java.util.Date());
        File destFile = new File(mediaStorageDir.getPath() + File.separator
                + messageFile.get_id()+messageFile.getOriginalName().substring(messageFile.getOriginalName().lastIndexOf(".")+1, messageFile.getOriginalName().length()));
        if (destFile.exists()) destFile.delete();
        //if (copy(new File(path), destFile)) return destFile;
        return destFile;
    }

    public static int getAudioFileDuration(String audioPath){
        String mediaPath = Uri.fromFile(new File(audioPath)).getPath();
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mediaPath);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        mmr.release();
        return Integer.parseInt(duration);
    }
}
