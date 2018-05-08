package ensp.reseau.wiatalk;

import android.content.Context;
import android.os.SystemClock;
import android.system.Os;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sim'S on 04/05/2018.
 */

public class U {
    public static String[] toArray(ArrayList<String> list){
        if (list==null) return null;
        String[] res = new String[list.size()];
        for (int i=0; i<list.size(); i++){
            res[i] = list.get(i);
        }
        return res;
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
}
