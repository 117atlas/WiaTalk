package ensp.reseau.wiatalk.files;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import ensp.reseau.wiatalk.app.WiaTalkApp;
import ensp.reseau.wiatalk.model.Message;
import ensp.reseau.wiatalk.model.MessageFile;

public class MessageFilesUtils {
    private static final String WiaTalkPhoto = File.separator+"WiaTalkData"+File.separator+"WiaTalk Photos";
    private static final String WiaTalkVideo = File.separator+"WiaTalkData"+File.separator+"WiaTalk Videos";
    private static final String WiaTalkVN = File.separator+"WiaTalkData"+File.separator+"WiaTalk VN";
    private static final String WiaTalkAudio = File.separator+"WiaTalkData"+File.separator+"WiaTalk Audios";
    private static final String WiaTalkDocument = File.separator+"WiaTalkData"+File.separator+"WiaTalk Documents";

    public static File voiceNote(String messageId){
        final String parentDir = Environment.getExternalStorageDirectory().getAbsolutePath()+WiaTalkVN;
        File parent = new File(parentDir);
        if (!parent.exists()){
            if (!parent.mkdirs()){
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + parentDir + " directory");
                return null;
            }
        }
        File vn = new File(parentDir+File.separator+messageId+".m4a");
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

    public static File photo(MessageFile messageFile){
        final String parentDir = Environment.getExternalStorageDirectory().getAbsolutePath()+WiaTalkPhoto;
        File parent = new File(parentDir);
        if (!parent.exists()){
            if (!parent.mkdirs()){
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + parentDir + " directory");
                return null;
            }
        }
        File photo = new File(parentDir+File.separator+messageFile.get_id()+"."+getExtension(messageFile.getUrl()));
        if (photo.exists()) photo.delete();
        try {
            photo.createNewFile();
            return photo;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("MKDIR ERR", "Oops! Failed create "
                    + photo.getAbsolutePath() + " directory");
            return null;
        }
    }

    public static File video(MessageFile messageFile){
        final String parentDir = Environment.getExternalStorageDirectory().getAbsolutePath()+WiaTalkVideo;
        File parent = new File(parentDir);
        if (!parent.exists()){
            if (!parent.mkdirs()){
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + parentDir + " directory");
                return null;
            }
        }
        File video = new File(parentDir+File.separator+messageFile.get_id()+"."+getExtension(messageFile.getUrl()));
        if (video.exists()) video.delete();
        try {
            video.createNewFile();
            return video;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("MKDIR ERR", "Oops! Failed create "
                    + video.getAbsolutePath() + " directory");
            return null;
        }
    }

    public static File audio(MessageFile messageFile){
        final String parentDir = Environment.getExternalStorageDirectory().getAbsolutePath()+ WiaTalkAudio;
        File parent = new File(parentDir);
        if (!parent.exists()){
            if (!parent.mkdirs()){
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + parentDir + " directory");
                return null;
            }
        }
        File audio = new File(parentDir+File.separator+messageFile.get_id()+"."+getExtension(messageFile.getUrl()));
        if (audio.exists()) audio.delete();
        try {
            audio.createNewFile();
            return audio;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("MKDIR ERR", "Oops! Failed create "
                    + audio.getAbsolutePath() + " directory");
            return null;
        }
    }

    public static File document(MessageFile messageFile){
        final String parentDir = Environment.getExternalStorageDirectory().getAbsolutePath()+WiaTalkDocument;
        File parent = new File(parentDir);
        if (!parent.exists()){
            if (!parent.mkdirs()){
                Log.d("MKDIR ERR", "Oops! Failed create "
                        + parentDir + " directory");
                return null;
            }
        }
        File doc = new File(parentDir+File.separator+messageFile.get_id()+"."+getExtension(messageFile.getUrl()));
        if (doc.exists()) doc.delete();
        try {
            doc.createNewFile();
            return doc;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("MKDIR ERR", "Oops! Failed create "
                    + doc.getAbsolutePath() + " directory");
            return null;
        }
    }

    public static File getMessageFile(MessageFile messageFile){
        if (messageFile.getType()==MessageFile.TYPE_PHOTO) return photo(messageFile);
        else if (messageFile.getType()==MessageFile.TYPE_VIDEO) return video(messageFile);
        else if (messageFile.getType()==MessageFile.TYPE_AUDIO) return audio(messageFile);
        else return document(messageFile);
    }

    private static String getExtension(String url){
        if (url==null) return null;
        return url.substring(url.lastIndexOf(".")+1, url.length());
    }
}
