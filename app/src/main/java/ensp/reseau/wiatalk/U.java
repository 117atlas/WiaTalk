package ensp.reseau.wiatalk;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.system.Os;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import ensp.reseau.wiatalk.ui.activities.GalleryActivity;

/**
 * Created by Sim'S on 04/05/2018.
 */

public class U {
    public static final int READ_EXTERNAL_STORAGE_REQ_PERM_CODE = 177;

    public static final int SELECT_GALLERY_REQ_CODES = 223;
    public static final int SELECT_GALLERY_REQ_CODES_2 = 222;
    public static final int SELECT_AUDIOS_REQ_CODES = 224;
    public static final int SELECT_DOCS_REQ_CODES = 225;

    public static String[] toArray(ArrayList<String> list){
        if (list==null) return null;
        String[] res = new String[list.size()];
        for (int i=0; i<list.size(); i++){
            res[i] = list.get(i);
        }
        return res;
    }

    public static void showImageAsset(Context context, String asset, ImageView imageView){
        Glide.with(context).load("file:///android_asset/"+asset)
                .placeholder(R.mipmap.logo).centerCrop()
                .into(imageView);
    }

    public static ArrayList<String> Split(String s, char c){
        if (s==null) return null;
        ArrayList res = new ArrayList();
        String tmp = s.toString();
        while (tmp.contains(String.valueOf(c))){
            int ind = tmp.indexOf(c);
            res.add(tmp.substring(0, ind));
            tmp = tmp.substring(ind+1, tmp.length());
        }
        res.add(tmp);
        return res;
    }

    public static String Initiales(String name){
        if (name==null) return null;
        if (!name.contains(" ")) return name.substring(0, 1);
        ArrayList<String> res = Split(name, ' ');
        if (res.size()==2) return res.get(0).substring(0, 1)+res.get(1).substring(0, 1);
        else {
            int i = 0;
            while (i<res.size()){
                if ((res.get(i).toLowerCase().equals("le") || res.get(i).toLowerCase().equals("la") ||
                        res.get(i).toLowerCase().equals("les") || res.get(i).toLowerCase().equals("the")) && res.size()>1) res.remove(i);
                else i++;
            }
            if (res.size()==1) return res.get(0).substring(0, 1);
            return res.get(0).substring(0, 1)+res.get(1).substring(0, 1);
        }
    }

    public static String NormalizeDate(long timestamp, Context context){
        Date d = new Date(timestamp);
        String[] days = context.getResources().getStringArray(R.array.days);
        Date now = new Date(SystemClock.currentThreadTimeMillis());
        if (d.getDate()==now.getDate()) return d.getHours()+" : "+d.getMinutes();
        if (d.getDate()==now.getDate()-1) return context.getString(R.string.yesterday);
        if (d.getDate()>now.getDate()-7) {
            return days[d.getDay()];
        }
        else return d.getDate()+"/"+(d.getMonth()+1)+"/"+(d.getYear()+1900);
    }

    public static void loadImage(Context context, ImageView imageView, String asset){
        Glide.with(context).load("file:///android_asset/"+asset)
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void showImage(Context context, ImageView imageView, File file){
        Glide.with(context).load(Uri.fromFile(file))
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }


    public static  boolean checkPermission(final Context context){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                if(((AppCompatActivity)context).shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    // Show an alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Read external storage permission is required.");
                    builder.setTitle("Please grant permission");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    ((AppCompatActivity)context),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    READ_EXTERNAL_STORAGE_REQ_PERM_CODE
                            );
                        }
                    });
                    builder.setNeutralButton("Cancel",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    // Request permission
                    ActivityCompat.requestPermissions(
                            ((AppCompatActivity)context),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQ_PERM_CODE
                    );
                }
                return false;
            }else {
                // Permission already granted
                return true;
            }
        }
        return true;
    }
}
