package ensp.reseau.wiatalk.ui;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Sim'S on 04/05/2018.
 */

public class UiUtils {
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
}
