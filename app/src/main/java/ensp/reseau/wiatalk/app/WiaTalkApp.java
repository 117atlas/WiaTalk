package ensp.reseau.wiatalk.app;

import android.app.Application;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;

/**
 * Created by Sim'S on 12/05/2018.
 */

public class WiaTalkApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EmojiManager.install(new IosEmojiProvider());
    }
}
