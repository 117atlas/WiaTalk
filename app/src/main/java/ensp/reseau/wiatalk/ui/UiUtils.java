package ensp.reseau.wiatalk.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import ensp.reseau.wiatalk.R;
import ensp.reseau.wiatalk.model.Message;

/**
 * Created by Sim'S on 04/05/2018.
 */

public class UiUtils {

    public static final int[] chatColors = {Color.rgb(0, 255, 128),
            Color.rgb(0, 128, 255),
            Color.rgb(255, 0, 255),
            Color.rgb(0, 128, 192),
            Color.rgb(0, 255, 64),
            Color.rgb(255, 0, 0),
            Color.rgb(128, 64, 64),
            Color.rgb(255, 128, 64),
            Color.rgb(0, 128, 128),
            Color.rgb(0, 64, 128),
            Color.rgb(128, 0, 64),
            Color.rgb(128, 0, 255),
            Color.rgb(128, 0, 128),
            Color.rgb(0, 0, 160),
            Color.rgb(0, 128, 64),
            Color.rgb(0, 128, 0),
            Color.rgb(255, 128, 0),
            Color.rgb(128, 0, 0),
            Color.rgb(128, 64, 0),
            Color.rgb(0, 64, 64),
            Color.rgb(0, 64, 0),
            Color.rgb(128, 128, 64),
            Color.rgb(128, 128, 0)};

    public static final int myColor = Color.rgb(230, 81, 0);

    public static Spannable shortMessageView(Message message){
        String replyfile = "";
        String[] types = {"Photo", "Video", "Audio", "Document"};
        if (message.getFile()!=null) replyfile = "["+types[message.getFile().getType()-1]+"] ";
        Spannable spannable = new SpannableString(replyfile+message.getText());
        spannable.setSpan(new ForegroundColorSpan(Color.MAGENTA), spannable.toString().indexOf(replyfile), spannable.toString().indexOf(replyfile)+replyfile.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static void switchActivity(Activity from, Class dest, boolean finish, IntentExtra... extras){
        Intent intent = new Intent(from, dest);
        if (extras!=null) {
            for (IntentExtra ie : extras) {
                if (ie.getSerializableValue() != null) intent.putExtra(ie.getTAG(), ie.getSerializableValue());
                if (ie.getIntValue() != null) intent.putExtra(ie.getTAG(), ie.getIntValue().intValue());
                if (ie.getStringValue() != null) intent.putExtra(ie.getTAG(), ie.getStringValue());
            }
        }
        from.startActivity(intent);
        if (finish) from.finish();
    }

    public static void showImage(Context context, ImageView imageView, String path){
        Glide.with(context).load(path==null?"":new File(path))
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    public static void showImage(Context context, ImageView imageView, String url, boolean urlb){
        Glide.with(context).load(url)
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static ProgressDialog loadingDialog(Context context){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getString(R.string.loading));
        return progressDialog;
    }
}
