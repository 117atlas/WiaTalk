package ensp.reseau.wiatalk.app;

import android.app.Application;
import android.content.Context;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;

import ensp.reseau.wiatalk.localstorage.LocalStorageUser;
import ensp.reseau.wiatalk.model.User;

/**
 * Created by Sim'S on 12/05/2018.
 */

public class WiaTalkApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EmojiManager.install(new IosEmojiProvider());
    }

    public static User getMe(Context context){
        return LocalStorageUser.getMe(context);
    }
}
